package com.jhtx.tl.common.util;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DistributedIdUtil {
    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    // 配置参数
    private static final long WORKER_ID_BITS = 10L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    private final long epoch;

    public DistributedIdUtil() {
        this(1577836800000L); // 默认起始时间：2020-01-01
    }

    public DistributedIdUtil(long epoch) {
        this.epoch = epoch;
        this.workerId = initWorkerId();
    }

    private long initWorkerId() {
        // 优先读取配置
        Long configId = getConfiguredWorkerId();
        if (configId != null) return configId;

        // 自动生成逻辑
        try {
            long networkId = generateNetworkBasedId();
            long processId = getProcessId();
            long threadId = Thread.currentThread().getId();

            long combinedValue = networkId ^ processId ^ threadId;
            long workerId = Math.abs(combinedValue % (MAX_WORKER_ID + 1));

            System.out.println("Auto-generated Worker ID: " + workerId);
            return workerId;
        } catch (Exception e) {
            throw new RuntimeException("Worker ID自动生成失败，请手动配置", e);
        }
    }

    private Long getConfiguredWorkerId() {
        try {
            // 检查系统属性和环境变量
            String workerId = System.getProperty("distributed.id.worker");
            if (workerId == null) workerId = System.getenv("DISTRIBUTED_ID_WORKER");
            return workerId != null ? Long.parseLong(workerId) : null;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("非法的Worker ID配置");
        }
    }

    private long generateNetworkBasedId() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp()) continue;

            // 优先使用MAC地址
            byte[] mac = iface.getHardwareAddress();
            if (mac != null) {
                long macHash = 0;
                for (byte b : mac) {
                    macHash = (macHash << 8) | (b & 0xFF);
                }
                return macHash;
            }

            // 次选IP地址
            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    return bytesToLong(addr.getAddress());
                }
            }
        }
        throw new SocketException("无可用网络标识");
    }

    private long bytesToLong(byte[] bytes) {
        long value = 0;
        for (byte b : bytes) {
            value = (value << 8) | (b & 0xFF);
        }
        return value;
    }

    private long getProcessId() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        if (processName.contains("@")) {
            return Long.parseLong(processName.split("@")[0]);
        }
        return ThreadLocalRandom.current().nextLong(MAX_WORKER_ID);
    }

    /**
     * 分布式ID生成
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            handleClockBackwards(timestamp);
            return nextId();
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis();
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - epoch) << TIMESTAMP_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long tilNextMillis() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            Thread.yield();
            timestamp = timeGen();
        }
        return timestamp;
    }

    private void handleClockBackwards(long currentTimestamp) {
        long offset = lastTimestamp - currentTimestamp;
        if (offset <= 5000) { // 允许5秒内的时钟回拨
            try {
                waitClockSync(offset);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("时钟回拨等待中断");
            }
        } else {
            throw new ClockMovedBackwardsException(
                    String.format("时钟回拨拒绝生成ID（偏移量：%dms）", offset));
        }
    }

    private void waitClockSync(long offset) throws InterruptedException {
        long sleepTime = offset * 2;
        TimeUnit.MILLISECONDS.sleep(sleepTime);
        System.err.println("时钟回拨补偿完成，等待时间：" + sleepTime + "ms");
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    static class ClockMovedBackwardsException extends RuntimeException {
        public ClockMovedBackwardsException(String message) {
            super(message);
        }
    }

}
