package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoUserCharmDetail;
import com.kantboot.util.common.result.PageResult;
import org.springframework.data.domain.Page;

/**
 * 魅力值明细表
 * @author 方某方
 */
public interface IBusOvoUserCharmDetailService {

    /**
     * 查看魅力值明细
     * @param userId 用户id
     * @param pageNumber 页码
     * @return 魅力值明细
     */
    PageResult getList(Long userId, Integer pageNumber);

    /**
     * 查看自己的魅力值明细
     * @param pageNumber 页码
     * @return 魅力值明细
     */
    PageResult getListOfSelf(Integer pageNumber);

    /**
     * 查看贡献值明细
     * @param userId 用户id
     * @param pageNumber 页码
     * @return 贡献值明细
     */
    PageResult getContributionList(Long userId, Integer pageNumber);

    /**
     * 查看自己的贡献值明细
     * @param pageNumber 页码
     * @return 贡献值明细
     */
    PageResult getContributionListOfSelf(Integer pageNumber);


}
