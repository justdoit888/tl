package com.jhtx.tl.common.util;


import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 日志工具类
 *
 * @author gaorusen
 */
public class LogUtil {
    public static String getStackTrace(Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
    }
}
