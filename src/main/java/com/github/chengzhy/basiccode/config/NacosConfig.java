package com.github.chengzhy.basiccode.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Nacos相关配置
 * @author chengzhy
 * @date 2021/8/4 8:53
 **/
@Configuration
public class NacosConfig {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String configServerAddr;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String discoveryServerAddr;

    /**
     * ConfigService Bean
     *
     * @author chengzhy
     * @date 2021/8/4 14:31
     * @return ConfigService
     * @throws NacosException
     */
    @Bean
    public ConfigService configService() throws NacosException {
        return NacosFactory.createConfigService(configServerAddr);
    }

    /**
     * NamingService Bean
     *
     * @author chengzhy
     * @date 2021/8/4 14:31
     * @return NamingService
     * @throws NacosException
     */
    @Bean
    public NamingService namingService() throws NacosException {
        return NacosFactory.createNamingService(discoveryServerAddr);
    }

}
