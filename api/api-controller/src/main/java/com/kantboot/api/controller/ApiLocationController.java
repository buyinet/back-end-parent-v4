package com.kantboot.api.controller;

import com.kantboot.api.service.IApiLocationService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地点控制器
 * 用于获取地点信息
 *
 * @author 方某方
 */
@RestController
@RequestMapping("/api/location")
public class ApiLocationController {

    @Resource
    private IApiLocationService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 根据经纬度范围查询
     * https://apis.map.qq.com/ws/place/v1/explore?key=${key}&boundary=nearby(${latitude},${longitude},${range},${expand})
     * @param latitude 纬度
     * @param longitude 经度
     * @param range 范围 单位：米
     * @param expand 是否扩大范围 0：不扩大；1：扩大
     * @return 位置信息
     */
    @RequestMapping("/getLocationInfoByRange")
    public RestResult getLocationInfoByRange(Double latitude, Double longitude, Double range, Integer expand){
        return stateSuccessService.success(service.getLocationInfoByRange(latitude, longitude, range, expand), "getSuccess");
    }

}
