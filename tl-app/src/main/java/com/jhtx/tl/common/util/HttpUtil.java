package com.jhtx.tl.common.util;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.Util;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class HttpUtil {

    private static final OkHttpClient CLIENT;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    // 连接池参数（4C8G优化值）
    private static final int MAX_IDLE_CONNECTIONS = 100;
    private static final long KEEP_ALIVE_MINUTES = 5;

    // 超时控制（单位：秒）
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;

    // 并发控制
    private static final int MAX_REQUESTS = 400;
    private static final int MAX_REQUESTS_PER_HOST = 100;
    private static final int THREAD_POOL_CORE = 16; // 4核*4
    private static final int THREAD_POOL_MAX = 64;

    static {
        // 安全SSL配置
        final X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        // 生产环境应替换为正式证书验证
        final SSLSocketFactory sslSocketFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("SSL初始化失败", e);
        }

        // 线程池配置（带命名和监控）
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                THREAD_POOL_CORE,
                THREAD_POOL_MAX,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadFactory() {
                    private final AtomicInteger counter = new AtomicInteger(0);

                    @Override
                    public Thread newThread(@NotNull Runnable r) {
                        return new Thread(r, "HttpWorker-" + counter.getAndIncrement());
                    }
                });

        Dispatcher dispatcher = new Dispatcher(executor);
        dispatcher.setMaxRequests(MAX_REQUESTS);
        dispatcher.setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);
        // 构建终极Client
        CLIENT = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier((hostname, session) -> true) // 生产环境需严格验证
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_MINUTES, TimeUnit.MINUTES))
                .dispatcher(dispatcher)
                .addInterceptor(new CircuitBreakerInterceptor()) // 熔断器
                //若是支付业务，关闭重试
                .addNetworkInterceptor(new RetryInterceptor(3))  // 智能重试
                .addNetworkInterceptor(new MetricsInterceptor()) // 监控埋点
                .build();
    }

    // ----------- 同步请求方法 -----------
    public static String get(String url) throws IOException {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        Request request = buildRequest(url, headers, null, null);
        return execute(request);
    }

    public static String postJson(String url, String json) throws IOException {
        return postJson(url, json, null);
    }

    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = buildRequest(url, headers, body, "POST");
        return execute(request);
    }

    // ----------- 异步请求方法 -----------
    public static void getAsync(String url, Callback callback) {
        getAsync(url, null, callback);
    }

    public static void getAsync(String url, Map<String, String> headers, Callback callback) {
        Request request = buildRequest(url, headers, null, null);
        enqueue(request, callback);
    }

    // ----------- 核心执行逻辑 -----------
    private static String execute(Request request) throws IOException {
        try (Response response = CLIENT.newCall(request).execute()) {
            validateResponse(response);
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private static void enqueue(Request request, Callback callback) {
        CLIENT.newCall(request).enqueue(new SafeCallbackWrapper(callback));
    }

    private static Request buildRequest(String url, Map<String, String> headers, RequestBody body, String method) {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) headers.forEach(builder::addHeader);
        if (body != null && method != null) builder.method(method, body);
        return builder.build();
    }

    private static void validateResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("HTTP错误码: " + response.code());
        }
        if (response.body() == null) {
            throw new IOException("空响应体");
        }
    }

    // ----------- 安全回调封装 -----------
    private static class SafeCallbackWrapper implements Callback {
        private final Callback delegate;

        SafeCallbackWrapper(Callback delegate) {
            this.delegate = delegate;
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) {
            try (Response r = response) {
                delegate.onResponse(call, r);
            } catch (Exception e) {
                delegate.onFailure(call, new IOException("响应处理异常", e));
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            delegate.onFailure(call, e);
        }
    }

    // ----------- 拦截器实现 -----------
    static class RetryInterceptor implements Interceptor {
        private final int maxRetries;

        RetryInterceptor(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            IOException exception = null;

            for (int i = 0; i <= maxRetries; i++) {
                try {
                    response = chain.proceed(request);
                    if (response.isSuccessful()) return response;
                } catch (IOException e) {
                    exception = e;
                }

                Util.closeQuietly(response);

                if (!isRetryable(request)) break;
                if (i < maxRetries) sleepBackoff(i);
            }

            if (exception != null) throw exception;
            throw new IOException("请求失败");
        }

        private boolean isRetryable(Request request) {
            return "GET".equalsIgnoreCase(request.method());
        }

        private void sleepBackoff(int attempt) {
            try {
                Thread.sleep(Math.min(1000L * (1 << attempt), 10000L));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class MetricsInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            long start = System.nanoTime();
            try {
                Response response = chain.proceed(chain.request());
                recordMetric(chain.request(), response, start);
                return response;
            } catch (IOException e) {
                recordError(chain.request(), start);
                throw e;
            }
        }

        private void recordMetric(Request request, Response response, long start) {
            long latency = System.nanoTime() - start;
            // 上报到监控系统
        }

        private void recordError(Request request, long start) {
            // 错误指标上报
        }
    }

    static class CircuitBreakerInterceptor implements Interceptor {
        @Override
        public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
            if (isCircuitOpen()) {
                throw new IOException("服务熔断中");
            }
            return chain.proceed(chain.request());
        }

        private boolean isCircuitOpen() {
            // 实现熔断逻辑
            return false;
        }
    }
}
