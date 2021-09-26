package com.github.chengzhy.basiccode.aspect.distributedlock.annotation;

import java.lang.annotation.*;

/**
 * 分布式Redis锁key注解 (与{@link DistRedisLock @DistRedisLock}注解结合使用)
 *
 * @author chengzhy
 * @date 2021/8/9 9:32
 */
@Documented
@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLockKey {

    /**
     * key的顺序
     *
     * @author chengzhy
     * @date 2021/8/9 9:32
     * @return key的顺序
     */
    int order() default 0;

}
