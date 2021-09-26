package com.github.chengzhy.basiccode.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis工具类
 * @author chengzhy
 * @date 2021/9/23 14:52
 */
@Component
public final class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /** TODO */

}
