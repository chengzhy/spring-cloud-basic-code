package com.github.chengzhy.basiccode.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * redis相关配置
 * @author chengzhy
 * @date 2021/8/4 11:02
 */
@Configuration
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * redis异常cache处理
     *
     * @author chengzhy
     * @date 2021/8/26 10:05
     * @return CacheErrorHandler
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("[cache get] redis error: key=[{}:{}]", cache.getName(), key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("[cache put] redis error: key=[{}:{}], value=[{}]", cache.getName(), key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("[cache evict] redis error: key=[{}:{}]", cache.getName(), key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("[cache clear] redis error: cache=[{}]", cache.getName(), e);
            }
        };
    }

    /**
     * redisTemplate{@code <String, Object>}
     *
     * @author chengzhy
     * @param connectionFactory RedisConnectionFactory
     * @date 2021/8/4 11:10
     * @return redisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域、field、get和set，以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

    /**
     * cacheManager(redis)
     *
     * @author chengzhy
     * @param redisTemplate redisTemplate
     * @date 2021/8/25 14:35
     * @return cacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory()))
                .cacheDefaults(getRedisCacheConfigurationWithTtl(redisTemplate, 3600))
                /**
                 * 自定义缓存配置
                 * .withCacheConfiguration("userInfo", getRedisCacheConfigurationWithTtl(redisTemplate, 60))
                 * or
                 * .withInitialCacheConfigurations()
                 */
                // 配置同步修改或删除 put/evict
                .transactionAware()
                .build();
    }

    /**
     * 自定义redis cache配置
     *
     * @author chengzhy
     * @param redisTemplate redisTemplate
     * @param seconds 过期时间，单位:秒
     * @date 2021/8/26 9:30
     * @return RedisCacheConfiguration
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(RedisTemplate<String, Object> redisTemplate, long seconds) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(seconds))
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> appName.concat(":").concat(cacheName).concat(":"))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(redisTemplate.getStringSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(redisTemplate.getValueSerializer()));
    }

}
