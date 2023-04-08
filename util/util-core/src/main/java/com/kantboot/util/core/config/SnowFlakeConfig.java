package com.kantboot.util.core.config;

import cn.hutool.core.lang.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置
 * @author 方某方
 */
@Configuration
@RequiredArgsConstructor
public class SnowFlakeConfig {
    private final SnowflakeProperties snowflakeProperties;

    /**
     * 初始化SnowflakeIdWorker Bean
     *
     * @return SnowflakeIdWorker
     */
    @Bean
    public Snowflake snowflake() {
        return new Snowflake(snowflakeProperties.getWorkerId(), snowflakeProperties.getDatacenterId());
    }
}
