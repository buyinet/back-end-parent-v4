package com.kantboot.business.ovo.controller;


import com.kantboot.business.ovo.service.service.IBusOvoSadomasochismAttrService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo性虐属性表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/sadomasochismAttr")
public class BusOvoSadomasochismAttrController {

    @Resource
    private IBusOvoSadomasochismAttrService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取列表
     * @return 性虐属性列表
     */
    @RequestMapping("/getList")
    public RestResult getList(){
        return stateSuccessService.success(service.getList(), "getSuccess");
    }

    /**
     * 获取字典
     * @return 性虐属性字典
     */
    @RequestMapping("/getMap")
    public RestResult getMap(){
        return stateSuccessService.success(service.getMap(), "getSuccess");
    }
}
