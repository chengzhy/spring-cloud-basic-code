package com.github.chengzhy.basiccode.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * redisson配置
 * @author chengzhy
 * @date 2021/8/4 14:10
 */
@Configuration
@Slf4j
public class RedissonConfig {

    @Value("${redisson.yaml-path:redisson.yml}")
    private String yamlPath;

    /**
     * RedissonClient Bean
     *
     * <p>根据配置文件{@code redisson.yml}来初始化RedissonClient，
     * redisson.yml包含redis单节点模式和集群模式，请根据实际情况选择或添加对应的模式并修改配置参数
     * 其它模式配置请参考<a href="https://github.com/redisson/redisson/wiki/2.-Configuration">github.com/redisson</a>
     *
     * @author chengzhy
     * @date 2021/8/4 14:10
     * @return RedissonClient
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        RedissonClient redissonClient;
        File file = new File(yamlPath);
        if (file.exists()) {
            log.info("===== 加载" + yamlPath + "配置文件 =====");
            redissonClient = Redisson.create(Config.fromYAML(file));
        } else {
            log.info("===== 加载内置默认redisson.yml配置文件 =====");
            redissonClient = Redisson.create(
                    Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
        }
        return redissonClient;
    }

}
