package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.bind.DefaultValue;
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

    /**
     * getGreaterOfRecommend
     * @return RestResult
     */
    @RequestMapping("/getGreaterOfRecommend")
    public RestResult getGreaterOfRecommend(Long id){
        return stateSuccessService.success(service.getGreaterOfRecommend(id),"getSuccess");
    }

    /**
     * getLessOfRecommend
     * @return RestResult
     */
    @RequestMapping("/getLessOfRecommend")
    public RestResult getLessOfRecommend(Long id){
        return stateSuccessService.success(service.getLessOfRecommend(id),"getSuccess");
    }

    /**
     * getHost
     * @return RestResult
     */
    @RequestMapping("/getHot")
    public RestResult getHot(){
        return stateSuccessService.success(service.getHot(),"getSuccess");
    }

    @RequestMapping("/publish")
    public RestResult publish(@RequestBody BusOvoPostDTO dto){
        return stateSuccessService.success(service.publish(dto),"getSuccess");
    }

    @RequestMapping("/getSelf")
    public RestResult getSelf(
            @DefaultValue("9999999999999999") Long id){
        return stateSuccessService.success(service.getSelf(id),"getSuccess");
    }

    @RequestMapping("/getById")
    public RestResult getById(Long id){
        return stateSuccessService.success(service.getById(id),"getSuccess");
    }

    @RequestMapping("/like")
    public RestResult like(Long id){
        return stateSuccessService.success(service.like(id),"getSuccess");
    }


    @RequestMapping("/unLike")
    public RestResult unLike(Long id){
        return stateSuccessService.success(service.unLike(id),"getSuccess");
    }

    @RequestMapping("/getNear")
    public RestResult getNear(Long pageNumber,Double range){
        return stateSuccessService.success(service.getNear(pageNumber,range),"getSuccess");
    }

    /**
     * getAllByFollowDefault
     * @return RestResult
     */
    @RequestMapping("/getFollowDefault")
    public RestResult getFollowDefault(){
        return stateSuccessService.success(service.getFollowDefault(),"getSuccess");
    }


    /**
     * getFollowLess
     * @param id id
     * @return RestResult
     */
    @RequestMapping("/getFollowLess")
    public RestResult getFollowLess(Long id){
        return stateSuccessService.success(service.getFollowLess(id),"getSuccess");
    }

    /**
     * getFollowGreater
     * @param id id
     * @return RestResult
     */
    @RequestMapping("/getFollowGreater")
    public RestResult getFollowGreater(Long id){
        return stateSuccessService.success(service.getFollowGreater(id),"getSuccess");
    }
}
