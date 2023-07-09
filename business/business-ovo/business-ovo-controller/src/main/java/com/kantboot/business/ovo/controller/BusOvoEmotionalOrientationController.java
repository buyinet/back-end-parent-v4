package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoEmotionalOrientationService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 情感取向表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/emotionalOrientation")
public class BusOvoEmotionalOrientationController {

    @Resource
    IBusOvoEmotionalOrientationService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 根据性别和性取向获取
     * @param gender 性别
     * @param sexualOrientationCode 性取向编码
     * @return 情感取向
     */
    @RequestMapping("/getByGenderAndSexualOrientationCode")
    public RestResult getByGenderAndSexualOrientationCode(Integer gender,String sexualOrientationCode){
        return stateSuccessService.success(
                        service.getByGenderAndSexualOrientationCode(gender, sexualOrientationCode),
                        "getSuccess"
                );
    }

    /**
     * 获取情感取向字典
     * @return 情感取向字典
     */
    @RequestMapping("/getMap")
    public RestResult getMap(){
        return stateSuccessService.success(
                service.getMap(),
                "getSuccess"
        );
    }

}
