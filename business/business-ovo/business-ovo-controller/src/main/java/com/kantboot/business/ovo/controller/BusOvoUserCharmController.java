package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoUserCharmService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 魅力值排行榜的controller
 * 用于处理魅力值排行榜的请求
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/userCharm")
public class BusOvoUserCharmController {

    @Resource
    private IStateSuccessService stateSuccessService;

    @Resource
    private IBusOvoUserCharmService service;


    /**
     * 获取魅力值排行榜
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 魅力值排行榜
     */
    @RequestMapping("/getTopByTimestamp")
    public RestResult getTopByTimestamp(Long startTimestamp, Long endTimestamp){
        return stateSuccessService.success(
                service.getTopByTimestamp(startTimestamp,endTimestamp),
                "getSuccess"
        );
    }

    /**
     * getCharmSelf
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 魅力值
     */
    @RequestMapping("/getCharmSelf")
    public RestResult getCharmSelf(Long startTimestamp, Long endTimestamp){
        return stateSuccessService.success(
                service.getCharmSelf(startTimestamp,endTimestamp),
                "getSuccess"
        );
    }

    /**
     * 获取自己的守护者
     * @return 自己的守护者
     */
    @RequestMapping("/getGuardiansOfSelf")
    public RestResult getGuardiansOfSelf(){
        return stateSuccessService.success(
                service.getGuardiansOfSelf(),
                "getSuccess"
        );
    }



}
