package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.module.dto.BusOvoUserBindDTO;
import com.kantboot.business.ovo.service.repository.BusOvoUserBindLocationRepository;
import com.kantboot.business.ovo.service.service.IBusOvoUserBindService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo用户绑定表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/userBind")
public class BusOvoUserBindController {

    @Resource
    private IBusOvoUserBindService service;

    @Resource
    private IStateSuccessService stateSuccessService;


    @Resource
    private BusOvoUserBindLocationRepository repository;

    @RequestMapping("/test")
    public RestResult test(){

        return stateSuccessService.success(
                repository.findAllWithDistance(
                        PageRequest.of(0, 15),20.0,100.0,1000.0)
                , "getSuccess");
    }

    /**
     * 读取附近的人
     * @param pageNumber 页码
     * @param range 范围
     * @return 附近的人
     */
    @RequestMapping("/getNear")
    public RestResult getNear(Integer pageNumber, Double range){
        return stateSuccessService.success(service.getNear(pageNumber, range), "getSuccess");
    }

    /**
     * 绑定用户
     * @return 绑定用户
     */
    @RequestMapping("/bind")
    public RestResult bind(@RequestBody BusOvoUserBindDTO dto){
        return stateSuccessService.success(service.bind(dto), "optSuccess");
    }

    /**
     * 获取自己的信息
     * @return 自己的信息
     */
    @RequestMapping("/getSelf")
    public RestResult getSelf(){
        return stateSuccessService.success(service.getSelf(), "getSuccess");
    }

    /**
     * 获取推荐列表
     * @param pageNumber 页码
     * @param sortField 排序的字段
     * @param sortOrderBy 排序的方式
     * @return 推荐列表
     */
    @SneakyThrows
    @RequestMapping("/getRecommendList")
    public RestResult getRecommendList(Integer pageNumber,
                                       @DefaultValue("gmtCreate") String sortField,
                                       @DefaultValue("desc") String sortOrderBy){
        return stateSuccessService.success(service.getRecommendList(pageNumber,sortField,sortOrderBy), "getSuccess");
    }

    /**
     * 修改位置信息
     * @param latitude 纬度
     * @param longitude 经度
     * @return 修改后的信息
     */
    @RequestMapping("/updateLocation")
    public RestResult updateLocation(Double latitude, Double longitude){
        return stateSuccessService.success(service.updateLocation(latitude,longitude), "optSuccess");
    }

    /**
     * 根据范围获取附近位置信息
     * @return 附近位置信息
     */
    @RequestMapping("/getLocationInfoByRangeSelf")
    public RestResult getLocationInfoByRangeSelf(){
        return stateSuccessService.success(service.getLocationInfoByRangeSelf(), "getSuccess");
    }
}
