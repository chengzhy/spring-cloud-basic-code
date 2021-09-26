package com.github.chengzhy.basiccode.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 可视线程池
 * @author chengzhy
 * @date 2021/9/9 9:20
 */
@SuppressWarnings("serial")
@Slf4j
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    public VisiableThreadPoolTaskExecutor() {
        super();
    }

    private void showThreadPoolInfo() {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        if (Objects.nonNull(threadPoolExecutor)) {
            log.info("{}, 已提交的任务数[{}], 已完成的任务数[{}], 正在执行任务的线程数[{}], 队列中的任务数[{}], 线程池中当前线程数[{}], 创建过的最大线程数[{}]",
                    getThreadNamePrefix(),
                    threadPoolExecutor.getTaskCount(),
                    threadPoolExecutor.getCompletedTaskCount(),
                    threadPoolExecutor.getActiveCount(),
                    threadPoolExecutor.getQueue().size(),
                    threadPoolExecutor.getPoolSize(),
                    threadPoolExecutor.getLargestPoolSize());
        }
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo();
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo();
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo();
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo();
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo();
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo();
        return super.submitListenable(task);
    }

}
