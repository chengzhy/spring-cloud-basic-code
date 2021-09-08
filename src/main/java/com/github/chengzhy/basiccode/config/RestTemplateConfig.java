package com.github.chengzhy.basiccode.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置
 * @author chengzhy
 * @date 2021/8/4 8:49
 **/
@Configuration
public class RestTemplateConfig {

    /**
     * restTemplate(通过ip+port请求)
     *
     * @author chengzhy
     * @date 2021/8/4 8:49
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 负载均衡restTemplate(通过服务名请求)
     *
     * @author chengzhy
     * @date 2021/8/4 8:51
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate loadBalanced() {
        return new RestTemplate();
    }

}
