package com.github.chengzhy.basiccode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * SpringCloudBasicCodeApplication启动类
 * @author chengzhy
 * @date 2021/8/3 15:45
 */
@SpringBootApplication(scanBasePackages = {"com.github.chengzhy.basiccode.**"})
@MapperScan({"com.github.chengzhy.basiccode.**.mapper.**"})
@EnableAsync
@EnableCaching
@EnableFeignClients
@EnableScheduling
public class SpringCloudBasicCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudBasicCodeApplication.class, args);
    }

}
