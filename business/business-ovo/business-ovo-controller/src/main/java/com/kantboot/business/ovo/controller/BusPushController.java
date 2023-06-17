package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusPushService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo用户推送表Controller类
 * @author 方某方
 */
@RestController
@RequestMapping("/business/push/push")
public class BusPushController {

    @Resource
    private IBusPushService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * cid绑定用户
     * @param cid cid
     */
    @RequestMapping("/bind")
    public RestResult bind(String cid){
        service.bind(cid);
        return stateSuccessService.success(null,"bindSuccess");
    }

}
