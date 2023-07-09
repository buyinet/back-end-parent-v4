package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoUserCharmDetailService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/ovo/userCharmDetail")
public class BusOvoUserCharmDetailController {

    @Resource
    private IBusOvoUserCharmDetailService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取魅力值明细
     * @param userId 用户id
     * @param pageNumber 页码
     * @return 魅力值明细
     */
    @RequestMapping("/getList")
    public RestResult getList(Long userId, Integer pageNumber){
        return stateSuccessService.success(
                service.getList(userId,pageNumber),
                "getSuccess"
        );
    }

    /**
     * 获取自己的魅力值明细
     * @param pageNumber 页码
     * @return 魅力值明细
     */
    @RequestMapping("/getListOfSelf")
    public RestResult getListOfSelf(Integer pageNumber){
        return stateSuccessService.success(
                service.getListOfSelf(pageNumber),
                "getSuccess"
        );
    }

    /**
     * 获取贡献值明细
     * @param userId 用户id
     * @param pageNumber 页码
     * @return 贡献值明细
     */
    @RequestMapping("/getContributionList")
    public RestResult getContributionList(Long userId, Integer pageNumber){
        return stateSuccessService.success(
                service.getContributionList(userId,pageNumber),
                "getSuccess"
        );
    }

    /**
     * 获取自己的贡献值明细
     * @param pageNumber 页码
     * @return 贡献值明细
     */
    @RequestMapping("/getContributionListOfSelf")
    public RestResult getContributionListOfSelf(Integer pageNumber){
        return stateSuccessService.success(
                service.getContributionListOfSelf(pageNumber),
                "getSuccess"
        );
    }

}
