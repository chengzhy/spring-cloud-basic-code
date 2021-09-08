package com.github.chengzhy.basiccode.aspect.distributedlock;

import com.github.chengzhy.basiccode.aspect.distributedlock.annotation.DistRedisLock;
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
 **/
@Aspect
@Component
@Slf4j
public class DistributedLockAspect {

    private static final String REDIS_LOCK_KEY_PREFIX = "DistRedisLock:";

    private final RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 通过redisson设置分布式锁
     * <p>具有{@linkplain DistRedisLock @DistRedisLock}注解的方法设置分布式锁
     *
     * @author chengzhy
     * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
     * @date 2021/8/9 9:32
     * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
     */
    @Around("@annotation(com.github.chengzhy.basiccode.aspect.distributedlock.annotation.DistRedisLock)")
    public Object redisLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DistRedisLock distRedisLock = methodSignature.getMethod()
                .getAnnotation(DistRedisLock.class);
        String lockKey = getRedisLockKey(joinPoint);
        // 加锁
        switch (distRedisLock.lockType()) {
            case REENTRANT_LOCK:
                return redisReentrantLock(joinPoint, lockKey);
            case FAIR_LOCK:
                return redisFairLock(joinPoint, lockKey);
            default:
                return redisReentrantLock(joinPoint, lockKey);
        }
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
        // @DistRedisLock
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DistRedisLock distRedisLock = methodSignature.getMethod()
                .getAnnotation(DistRedisLock.class);
        if (StringUtils.isBlank(distRedisLock.lockKey())
                && distRedisLock.defaultKey()) {
            // 设置默认key
            lockKey.append(methodSignature.getDeclaringTypeName())
                    .append(".")
                    .append(methodSignature.getName());
        } else {
            lockKey.append(distRedisLock.lockKey());
        }
        // @RedisLockKey
        Object[] args = joinPoint.getArgs();
        if (args!=null && args.length>0) {
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
            SortedMap<Integer, String> keys = new TreeMap<>();
            for (int i=0; i< annotations.length; i++) {
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

    /**
     * 可重入锁加锁方式
     *
     * @author chengzhy
     * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
     * @param lockKey 分布式锁key {@code String}
     * @date 2021/8/9 9:32
     * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
     */
    private Object redisReentrantLock(@NonNull ProceedingJoinPoint joinPoint,
                                      @NonNull String lockKey) throws Throwable {
        return lock(joinPoint, redissonClient.getLock(lockKey));
    }

    /**
     * 公平锁加锁方式
     *
     * @author chengzhy
     * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
     * @param lockKey 分布式锁key {@code String}
     * @date 2021/8/9 9:32
     * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
     */
    private Object redisFairLock(@NonNull ProceedingJoinPoint joinPoint,
                                 @NonNull String lockKey) throws Throwable {
        return lock(joinPoint, redissonClient.getFairLock(lockKey));
    }

    /**
     * RLock加锁操作
     *
     * @author chengzhy
     * @param joinPoint 程序连接点 {@code ProceedingJoinPoint}
     * @param lock {@link RLock}
     * @date 2021/8/9 9:32
     * @return {@code Object} ({@code joinPoint.proceed()} 或 {@code null})
     */
    private Object lock(@NonNull ProceedingJoinPoint joinPoint, @NonNull RLock lock) throws Throwable {
        boolean lockSuccess;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        DistRedisLock distRedisLock = methodSignature.getMethod()
                .getAnnotation(DistRedisLock.class);
        try {
            if (distRedisLock.tryLock()) {
                // tryLock加锁方式
                lockSuccess = distRedisLock.waitTime() == -1L ? lock.tryLock() :
                        lock.tryLock(distRedisLock.waitTime(), TimeUnit.SECONDS);
            } else {
                // 普通加锁方式
                lock.lock();
                lockSuccess = true;
            }
        } catch (RedisException e) {
            log.error(e.getMessage(), e);
            return joinPoint.proceed();
        }
        if (lockSuccess) {
            Object result = joinPoint.proceed();
            try {
                lock.unlock();
            } catch (RedisException e) {
                log.error(e.getMessage(), e);
            }
            return result;
        }
        return null;
    }

}
