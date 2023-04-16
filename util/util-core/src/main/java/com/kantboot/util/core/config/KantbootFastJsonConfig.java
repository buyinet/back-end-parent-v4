package com.kantboot.util.core.config;


import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 配置fastjson
 * @author 方某方
 */
@Component
@Configuration
public class KantbootFastJsonConfig implements WebMvcConfigurer {

    /**
     * 设置成GMT标准时间
     */
    @PostConstruct
    public void setTimeZone(){
        // 采用了GMT标准时间
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        TimeZone.setDefault(timeZone);
    }


    @Bean
    public FastJsonConfig fastJsonConfig() {
        //1.自定义配置...
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat("millis");

        //2.1配置序列化的行为
        //JSONWriter.Feature.PrettyFormat:格式化输出
        config.setWriterFeatures(
                JSONWriter.Feature.PrettyFormat
        );
        //2.2配置反序列化的行为
        config.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        return config;
    }
    /**
     *  使用 FastJsonHttpMessageConverter 来替换 Spring MVC 默认的 HttpMessageConverter
     *  以提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化和反序列化速度。
     * */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1.转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setFastJsonConfig(fastJsonConfig());
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(0, converter);
    }
}