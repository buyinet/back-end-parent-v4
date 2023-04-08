package com.kantboot.util.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置
 * @author 方某方
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeProperties {

    /**
     * 工作节点ID
     */
    private Long workerId;

    /**
     * 数据中心ID
     */
    private Long datacenterId;
}