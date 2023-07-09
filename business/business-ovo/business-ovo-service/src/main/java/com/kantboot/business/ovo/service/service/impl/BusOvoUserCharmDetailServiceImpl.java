package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoUserCharmDetail;
import com.kantboot.business.ovo.service.repository.BusOvoUserCharmDetailRepository;
import com.kantboot.business.ovo.service.service.IBusOvoUserCharmDetailService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.result.PageResult;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 用户魅力值明细表的service实现类
 * 用于处理用户魅力值明细表的业务逻辑
 * @author 方某方
 */
@Service
public class BusOvoUserCharmDetailServiceImpl
implements IBusOvoUserCharmDetailService
{

    @Resource
    private BusOvoUserCharmDetailRepository repository;

    @Resource
    private ISysUserService sysUserService;

    @Override
    public PageResult getList(Long userId, Integer pageNumber) {
        return
                PageResult.of(
                        repository.findByToUserIdOrderByGmtCreateDesc(userId, Pageable.ofSize(30).withPage(pageNumber-1))
                );
    }

    @Override
    public PageResult getListOfSelf(Integer pageNumber) {
        return getList(sysUserService.getIdOfSelf(), pageNumber);
    }

    @Override
    public PageResult getContributionList(Long userId, Integer pageNumber) {
        return PageResult.of(
                repository.findByFromUserIdOrderByGmtCreateDesc(userId, Pageable.ofSize(30).withPage(pageNumber-1))
        );
    }

    @Override
    public PageResult getContributionListOfSelf(Integer pageNumber) {
        return getContributionList(sysUserService.getIdOfSelf(), pageNumber);
    }
}
