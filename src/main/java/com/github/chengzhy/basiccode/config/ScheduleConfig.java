package com.github.chengzhy.basiccode.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * {@linkplain  org.springframework.scheduling.annotation.Scheduled @Scheduled}
 * 定时任务配置
 *
 * @author chengzhy
 * @date 2021/12/23 10:58
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    private final Executor scheduledExecutor;

    public ScheduleConfig(Executor scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }

    /**
     * 配置定时任务所使用的线程池
     *
     * @author chengzhy
     * @param scheduledTaskRegistrar scheduledTaskRegistrar
     * @date 2021/12/23 11:26
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduledExecutor);
    }

}
