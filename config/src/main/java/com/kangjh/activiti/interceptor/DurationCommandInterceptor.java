package com.kangjh.activiti.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;

/**
 * 执行时间
 *
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class DurationCommandInterceptor extends AbstractCommandInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DurationCommandInterceptor.class);

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {
        LocalTime start = LocalTime.now();
        try {
            return this.getNext().execute(config, command);
        } finally {
            LocalTime end = LocalTime.now();
            long l = Duration.between(start, end).toMillis();
            LOGGER.info("{} 执行时长 {} 毫秒", command.getClass().getSimpleName(), l);
        }
    }
}
