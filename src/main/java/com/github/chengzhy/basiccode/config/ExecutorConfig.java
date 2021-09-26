package com.github.chengzhy.basiccode.config;

import com.github.chengzhy.basiccode.async.VisiableThreadPoolTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @author chengzhy
 * @date 2021/8/4 8:55
 */
@Configuration
@Slf4j
public class ExecutorConfig extends AsyncConfigurerSupport {

    /**
     * 当前机器cpu核数
     */
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 默认线程池
     *
     * @author chengzhy
     * @date 2021/8/4 8:56
     * @return Executor
     */
    @Bean
    public Executor defaultExecutor() {
        ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(CPU_NUM);
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(CPU_NUM << 1);
        // 缓冲队列：用来缓冲执行任务的队列
        executor.setQueueCapacity(1024);
        // 允许线程的空闲时间60秒：当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("defaultExecutor-");
        // 缓冲队列满了之后的拒绝策略：由调用线程处理（一般是主线程）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return defaultExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("异步方法{}出现异常！", method, ex);
        };
    }

}
