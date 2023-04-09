package com.kantboot.system.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 为方便后期扩展成微服务，将系统启动类放在单独的模块中
 * @author 方某方
 */
@EnableJpaRepositories(value = {"com.kantboot"})
@EntityScan(basePackages = "com.kantboot")
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.kantboot"})
@ComponentScan(basePackages = {"com.kantboot"})
public class SystemStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemStartApplication.class, args);
    }

}
