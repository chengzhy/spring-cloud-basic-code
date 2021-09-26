package com.github.chengzhy.basiccode.aspect.distributedlock.annotation;

import com.github.chengzhy.basiccode.aspect.distributedlock.DistributedLockAspect;

import java.lang.annotation.*;

/**
 * 分布式Redis锁注解(待完善)
 * @author chengzhy
 * @date 2021/8/9 9:32
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistRedisLock {

    /**
     * 分布式锁key
     * <p>设为默认key时，lockKey=类所在包信息 + 方法名
     *
     * @author chengzhy
     * @date 2021/8/9 9:32
     * @return 分布式锁key格式为 "DistRedisLock:" + lockKey + @RedisLockKey，默认为""
     */
    String lockKey() default "";

    /**
     * 是否使用默认key弱标志位
     * <p>使用条件：只有当{@code StringUtils.isBlank(lockKey) && defaultKey=true}时才使用默认key<br>
     * 其它情况都使用lockKey的值
     *
     * @author chengzhy
     * @date 2021/8/9 9:32
     * @return 是|否，默认为是
     */
    boolean defaultKey() default true;

    /**
     * redisson锁类型
     *
     * @author chengzhy
     * @date 2021/8/9 9:32
     * @return redisson锁类型，默认为REENTRANT_LOCK
     */
    DistributedLockAspect.RedissonLock lockType() default DistributedLockAspect.RedissonLock.REENTRANT_LOCK;

    /**
     * 是否使用尝试加锁方式
     *
     * @author chengzhy
     * @date 2021/8/9 9:32
     * @return 是|否，默认为否
     */
    boolean tryLock() default false;

    /**
     * 锁有效时间 单位：秒
     *
     * @author chengzhy
     * @date 2021/9/24 11:29
     * @return 锁有效时间
     */
    long leaseTime() default -1L;

    /**
     * 尝试加锁方式参数：等待加锁的时间(最多等待该时间) 单位：秒
     *
     * @author chengzhy
     * @date 2021/8/9 9:32
     * @return 等待加锁的时间
     */
    long waitTime() default -1L;

}
