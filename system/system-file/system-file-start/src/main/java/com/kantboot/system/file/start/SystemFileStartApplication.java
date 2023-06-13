package com.kantboot.system.file.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@EnableJpaRepositories(value = {"com.kantboot"})
@EntityScan(basePackages = "com.kantboot")
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.kantboot"})
@ComponentScan(basePackages = {"com.kantboot"})
@ServletComponentScan(basePackages = {"com.kantboot"})
public class SystemFileStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemFileStartApplication.class, args);
    }

}
