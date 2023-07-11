package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子的controller
 * 用于处理帖子的请求
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/post")
public class BusOvoPostController {

    @Resource
    private IBusOvoPostService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * getDefaultRecommend
     * @return RestResult
     */
    @RequestMapping("/getDefaultRecommend")
    public RestResult getDefaultRecommend(){
        return stateSuccessService.success(service.getDefaultRecommend(),"getSuccess");
    }

}
