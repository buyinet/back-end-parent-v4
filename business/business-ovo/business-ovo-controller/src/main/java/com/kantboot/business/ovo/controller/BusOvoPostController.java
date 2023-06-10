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
     * 发布帖子
     * @param dto 发布帖子的参数
     * @return 发布的帖子
     */
    @RequestMapping("/publish")
    public RestResult publish(
            @RequestBody BusOvoPostDTO dto){
        return stateSuccessService.success(
                        service.publish(dto),
                        "success"
                );
    }

    /**
     * 获取自己的帖子
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 自己的帖子
     */
    @RequestMapping("/getSelf")
    public RestResult getSelf(Integer pageNumber, String sortField, String sortOrderBy){
        return stateSuccessService.success(
                        service.getSelf(pageNumber, sortField, sortOrderBy),
                        "getSuccess"
                );
    }

    /**
     * 获取推荐的帖子
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 推荐的帖子
     */
    @RequestMapping("/getRecommend")
    public RestResult getRecommend(Integer pageNumber, String sortField, String sortOrderBy){
        return stateSuccessService.success(
                        service.getRecommend(pageNumber, sortField, sortOrderBy),
                        "getSuccess"
                );
    }

    /**
     * 获取附近的帖子
     * @param pageNumber 页码
     * @param range 范围
     * @return 附近的帖子
     */
    @RequestMapping("/getNear")
    public RestResult getNear(Integer pageNumber,Double range){
        return stateSuccessService.success(
                        service.getNear(pageNumber, range),
                        "getSuccess"
                );
    }



}
