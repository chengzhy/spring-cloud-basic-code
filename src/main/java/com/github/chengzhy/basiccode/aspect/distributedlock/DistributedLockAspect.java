package com.github.chengzhy.basiccode.aspect.distributedlock;

import com.github.chengzhy.basiccode.aspect.distributedlock.annotation.DistributedRedisLock;
import com.github.chengzhy.basiccode.aspect.distributedlock.annotation.RedisLockKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 * @author chengzhy
 * @date 2021/8/9 9:32
 */
@Aspect
@Component
@Slf4j
public class DistributedLockAspect {

    private static final String REDIS_LOCK_KEY_PREFIX = "DistributedRedisLock:";

    private final RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 通过redisson设置分布式锁
     * <p>具有{@linkplain DistributedRedisLock @DistributedRedisLock}注解的方法设置分布式锁
     *
     * @author chengzhy
     * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
     * @date 2021/8/9 9:32
     * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
     */
    @Around("@annotation(com.github.chengzhy.basiccode.aspect.distributedlock.annotation.DistributedRedisLock)")
    public Object redisLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DistributedRedisLock distributedRedisLock = methodSignature.getMethod()
                .getAnnotation(DistributedRedisLock.class);
        return distributedRedisLock.lockType().lock(joinPoint, redissonClient, getRedisLockKey(joinPoint));
    }

    /**
     * 获取redis分布式锁key
     *
     * @author chengzhy
     * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
     * @date 2021/6/23 14:20
     * @return
     */
    private String getRedisLockKey(@NonNull ProceedingJoinPoint joinPoint) {
        StringBuilder lockKey = new StringBuilder(REDIS_LOCK_KEY_PREFIX);
        // @DistributedRedisLock
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DistributedRedisLock distributedRedisLock = methodSignature.getMethod()
                .getAnnotation(DistributedRedisLock.class);
        if (StringUtils.isBlank(distributedRedisLock.lockKey())
                && distributedRedisLock.defaultKey()) {
            // 设置默认key
            lockKey.append(methodSignature.getDeclaringTypeName())
                    .append(".")
                    .append(methodSignature.getName());
        } else {
            lockKey.append(distributedRedisLock.lockKey());
        }
        // @RedisLockKey
        Object[] args = joinPoint.getArgs();
        if (args!=null && args.length>0) {
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
            SortedMap<Integer, String> keys = new TreeMap<>();
            for (int i=0; i<annotations.length; i++) {
                Annotation[] paramAnnotations = annotations[i];
                for (Annotation annotation : paramAnnotations) {
                    if (RedisLockKey.class.equals(annotation.annotationType())) {
                        if (args[i] != null) {
                            RedisLockKey redisLockKey = (RedisLockKey) annotation;
                            keys.put(redisLockKey.order(), args[i].toString());
                        }
                        break;
                    }
                }
            }
            for (String key : keys.values()) {
                lockKey.append(".").append(key);
            }
        }
        return lockKey.toString();
    }

    public enum RedissonLockEnum {
        /**
         * 可重入锁
         */
        REENTRANT_LOCK {
            @Override
            public Object lock(ProceedingJoinPoint joinPoint, RedissonClient redissonClient,
                               String lockKey) throws Throwable {
                RLock rLock = redissonClient.getLock(lockKey);
                return lock(joinPoint, rLock);
            }
        },
        /**
         * 公平锁
         */
        FAIR_LOCK {
            @Override
            public Object lock(ProceedingJoinPoint joinPoint, RedissonClient redissonClient,
                               String lockKey) throws Throwable {
                RLock rLock = redissonClient.getFairLock(lockKey);
                return lock(joinPoint, rLock);
            }
        };

        /**
         * 加锁抽象方法
         *
         * @author chengzhy
         * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
         * @param redissonClient {@link RedissonClient}
         * @param lockKey 分布式锁key {@code String}
         * @date 2021/9/9 17:00
         * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
         * @throws Throwable 程序错误
         */
        public abstract Object lock(@NonNull ProceedingJoinPoint joinPoint, @NonNull RedissonClient redissonClient,
                                    @NonNull String lockKey) throws Throwable;

        /**
         * RLock加锁操作
         *
         * @author chengzhy
         * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
         * @param rLock {@link RLock}
         * @date 2021/8/9 9:32
         * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
         */
        protected Object lock(@NonNull ProceedingJoinPoint joinPoint, @NonNull RLock rLock) throws Throwable {
            boolean lockSuccess = false;
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            DistributedRedisLock distributedRedisLock = methodSignature.getMethod()
                    .getAnnotation(DistributedRedisLock.class);
            try {
                if (distributedRedisLock.tryLock()) {
                    // tryLock加锁方式
                    lockSuccess = (distributedRedisLock.waitTime() == -1L) ? rLock.tryLock()
                            : (distributedRedisLock.leaseTime() == -1L) ? rLock.tryLock(distributedRedisLock.waitTime(), TimeUnit.SECONDS)
                            : rLock.tryLock(distributedRedisLock.waitTime(), distributedRedisLock.leaseTime(), TimeUnit.SECONDS);
                } else {
                    // 普通加锁方式
                    if (distributedRedisLock.leaseTime() == -1L) {
                        rLock.lock();
                    } else {
                        rLock.lock(distributedRedisLock.leaseTime(), TimeUnit.SECONDS);
                    }
                    lockSuccess = true;
                }
            } catch (RedisException e) {
                log.error(e.getMessage(), e);
                return joinPoint.proceed();
            } catch (IllegalStateException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            if (lockSuccess) {
                try {
                    return joinPoint.proceed();
                } finally {
                    try {
                        if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                            rLock.unlock();
                        }
                    } catch (RedisException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            return null;
        }
    }

}
