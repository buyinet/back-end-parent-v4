package com.kantboot.util.common.http;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * http请求头工具类
 * @author 方某方
 */
@Component
@Data
public class HttpRequestHeaderUtil {

    @Resource
    private HttpServletRequest request;

    /**
     * token字段
     */
    public final static String TOKEN_FIELD = "token";

    /**
     * 语言编码字段
     * 从请求头中获取语言编码字段的值，如果没有语言编码字段则返回null，如果有语言编码字段则返回语言编码字段的值
     */
    public final static String LANGUAGE_CODE_FIELD = "languageCode";

    /**
     * 场景编码字段
     * 从请求头中获取场景编码字段的值，如果没有场景编码字段则返回null，如果有场景编码字段则返回场景编码字段的值
     */
    public final static String SCENE_CODE_FIELD = "sceneCode";

    /**
     * 用于设备型号获取的map
     */
    private final Map<String,String> deviceMap;

    public HttpRequestHeaderUtil() {
        // 初始化设备型号map
        deviceMap = new HashMap<>();
        deviceMap.put("Android","Build");
        deviceMap.put("iPhone","CPU");
        deviceMap.put("iPad","CPU");
        deviceMap.put("iPod","CPU");
        deviceMap.put("Windows Phone","ARM");
        deviceMap.put("MQQBrowser","ARM");
        deviceMap.put("Windows CE","ARM");
        deviceMap.put("Windows NT","Windows NT");
        deviceMap.put("Mac OS X","Mac OS X");
        deviceMap.put("Linux","Linux");
        deviceMap.put("X11","X11");
        deviceMap.put("Chrome OS","CrOS");
    }



    /**
     * 获取token
     * 原理：从请求头中获取token字段的值，如果没有token字段则返回null，如果有token字段则返回token字段的值
     * @return token
     */
    public String getToken() {
        return request.getHeader(TOKEN_FIELD);
    }


    /**
     * 获取ip地址
     * 原理：从请求头中获取ip地址，如果没有ip地址则返回null
     * @return ip地址
     */
    public String getIp() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip==null){
            // 如果ip为空，则从请求头中的“Proxy-Client-IP”字段中获取ip
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip==null){
            // 如果ip为空，则从请求头中的“WL-Proxy-Client-IP”字段中获取ip
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip==null){
            // 如果ip为空，则从请求头中的“HTTP_CLIENT_IP”字段中获取ip
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip==null){
            // 如果ip为空，则从请求头中的“HTTP_X_FORWARDED_FOR”字段中获取ip
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip==null){
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 从请求头中获取User-Agent
     */
    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    /**
     * 从请求头中的ua中获取设备
     * 原理：从请求头中获取ua，如果没有ua则返回null，如果有ua则返回设备
     * @return 设备
     */
    public String getDevice() {
        String ua = getUserAgent();
        if (ua == null) {
            return null;
        }

        // 遍历设备型号map
        for (Map.Entry<String, String> entry : deviceMap.entrySet()) {
            if (ua.contains(entry.getKey())) {
                String[] split = ua.split(";");
                for (String s : split) {
                    if (s.contains(entry.getValue())) {
                        return s;
                    }
                }
            }
        }


        return null;
    }

    /**
     * 获取请求头中的语言编码
     */
    public String getLanguageCode() {
        return request.getHeader(LANGUAGE_CODE_FIELD);
    }

    /**
     * 获取请求头中的场景编码
     */
    public String getSceneCode() {
        return request.getHeader(SCENE_CODE_FIELD);
    }

}

