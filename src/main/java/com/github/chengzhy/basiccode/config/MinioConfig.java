package com.github.chengzhy.basiccode.config;

import io.minio.MinioClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置
 * @author chengzhy
 * @date 2021/8/6 15:30
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Getter
@Setter
public class MinioConfig {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    /**
     * MinioClient Bean
     *
     * @author chengzhy
     * @date 2021/8/6 15:30
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
