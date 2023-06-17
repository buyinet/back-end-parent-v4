package com.kantboot.api.util.location;

import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 位置api工具类
 * @author 方某方
 */
public class LocationApiUtil {

    @SneakyThrows
    private static String webSent(String urlStr) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = new URL(urlStr).openStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        byte[] keyBytes = outputStream.toByteArray();

        return new String(keyBytes, StandardCharsets.UTF_8);
    }

    /**
     * 腾讯地图api根据经纬度获取位置信息
     * @url https://apis.map.qq.com/ws/geocoder/v1/?location=${latitude},${longitude}&key=${key}&get_poi=${poi}
     * @param latitude 纬度
     * @param longitude 经度
     * @param key 腾讯地图的key
     * @param poi 是否返回周边poi列表：1.返回；0.不返回(默认)
     */
    public static JSONObject getLocationInfo(Double latitude,Double longitude, String key, Integer poi) {
        String url = "https://apis.map.qq.com/ws/geocoder/v1/?location=" + latitude + "," + longitude + "&key=" + key + "&get_poi=" + poi;
        // 向腾讯地图api发送请求
        String jsonStr = webSent(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * 百度地图api根据ip获取位置信息
     * @url https://apis.map.qq.com/ws/location/v1/ip?ip=${ip}&key=${key}
     * @param ip ip地址
     * @param key 腾讯地图的key
     * @return 位置信息
     */
    public static JSONObject getLocationInfoByIp(String ip, String key) {
        String url = "https://apis.map.qq.com/ws/location/v1/ip?ip=" + ip + "&key=" + key;
        // 向腾讯地图api发送请求
        String jsonStr = webSent(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * 根据经纬度范围查询
     * https://apis.map.qq.com/ws/place/v1/explore?key=${key}&boundary=nearby(${latitude},${longitude},${range},${expand})
     * @param latitude 纬度
     * @param longitude 经度
     * @param range 范围 单位：米
     * @param expand 是否扩大范围 0：不扩大；1：扩大
     * @param key 密钥
     * @return 位置信息
     */
    public static JSONObject getLocationInfoByRange(Double latitude, Double longitude, Double range, Integer expand,String key) {
        String url = "https://apis.map.qq.com/ws/place/v1/explore?orderby=_distance&page_size=20&key=" + key + "&boundary=nearby(" + latitude + "," + longitude + "," + range + "," + expand + ")";
        // 向腾讯地图api发送请求
        String jsonStr = webSent(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }

    /**
     * ID查询
     * https://apis.map.qq.com/ws/place/v1/detail
     * @param id 腾讯地图POI（地点）唯一标识，支持多poiid检索，最大支持10个，用英文逗号分隔
     * @param key 密钥
     * @return 位置信息
     */
    public static JSONObject getLocationInfoById(String id,String key) {
        String url = "https://apis.map.qq.com/ws/place/v1/detail?id=" + id + "&key=" + key;
        // 向腾讯地图api发送请求
        String jsonStr = webSent(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject;
    }


}
