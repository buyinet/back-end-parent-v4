package com.kantboot.business.ovo.service.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis的mapper扫描配置类
 * @author 方某方
 */
@Configuration
@MapperScan("com.kantboot.business.ovo.service.mapper")
public class MapperConfig {
}
