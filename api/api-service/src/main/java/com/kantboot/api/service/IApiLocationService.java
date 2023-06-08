package com.kantboot.api.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * 位置服务
 * @author 方某方
 */
public interface IApiLocationService {


    /**
     * 腾讯地图api
     * @url https://apis.map.qq.com/ws/geocoder/v1/?location=${latitude},${longitude}&key=${key}&get_poi=${poi}
     * @param latitude 纬度
     * @param longitude 经度
     * @param poi 是否返回周边poi列表：1.返回；0.不返回(默认)
     * @return 位置信息
     */
    JSONObject getLocationInfo(Double latitude, Double longitude, Integer poi);

    /**
     * 根据ip获取位置信息
     * @param ip ip地址
     * @return 位置信息
     */
    JSONObject getLocationInfoByIp(String ip);

    /**
     * 根据经纬度范围查询
     * https://apis.map.qq.com/ws/place/v1/explore?key=${key}&boundary=nearby(${latitude},${longitude},${range},${expand})
     * @param latitude 纬度
     * @param longitude 经度
     * @param range 范围 单位：米
     * @param expand 是否扩大范围 0：不扩大；1：扩大
     * @return 位置信息
     */
    JSONArray getLocationInfoByRange(Double latitude, Double longitude, Double range, Integer expand);

    /**
     * ID查询
     * https://apis.map.qq.com/ws/place/v1/detail
     * @param id 腾讯地图POI（地点）唯一标识，支持多poiid检索，最大支持10个，用英文逗号分隔
     * @return 位置信息
     */
    JSONObject getLocationInfoById(String id);

}
