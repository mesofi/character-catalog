/*
 * Copyright (C) Mesofi - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential Written by
 * Armando Rivas, Oct 15, 2023.
 */
package com.mesofi.collection.charactercatalog.test;

import java.lang.reflect.Method;

import org.slf4j.Logger;

/**
 * Logging utilities.
 * 
 * @author armandorivasarzaluz
 */
public class LoggingUtils {

    /**
     * Enable DEBUG level.
     * 
     * @param log The logger.
     */
    public static void enableLog(Logger log) {
        enableLog(log, "DEBUG");
    }

    /**
     * Enable any log level, like DEBUG, TRACE etc.
     * 
     * @param log            The logger.
     * @param loggerLevelNew The new logger.
     */
    public static void enableLog(Logger log, String loggerLevelNew) {
        try {
            Class<?> levelLogBackClass = Class.forName("ch.qos.logback.classic.Level");
            Method toLevelMethod = levelLogBackClass.getDeclaredMethod("toLevel", String.class);
            Object traceLevel = toLevelMethod.invoke(null, loggerLevelNew);
            Method loggerSetLevelMethod = log.getClass().getDeclaredMethod("setLevel", levelLogBackClass);
            loggerSetLevelMethod.invoke(log, traceLevel);
        } catch (Exception e) {
            log.warn("Problem setting logger level to:{}, msg: {}", loggerLevelNew, e.getMessage());
        }
    }
}
