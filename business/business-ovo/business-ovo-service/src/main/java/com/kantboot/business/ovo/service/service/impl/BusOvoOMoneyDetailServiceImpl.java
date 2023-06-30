package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoOMoneyDetail;
import com.kantboot.business.ovo.service.repository.BusOvoOMoneyDetailRepository;
import com.kantboot.business.ovo.service.service.IBusOvoOMoneyDetailService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.result.PageResult;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * O币明细的服务类实现类
 * @author 方某方
 */
@Service
public class BusOvoOMoneyDetailServiceImpl implements IBusOvoOMoneyDetailService {

    @Resource
    private BusOvoOMoneyDetailRepository busOvoOMoneyDetailRepository;

    @Resource
    private ISysUserService sysUserService;

    @Override
    public PageResult get(Long userId, Integer pageNumber) {
        Page<BusOvoOMoneyDetail> byUserIdOrderByCreateTimeDesc = busOvoOMoneyDetailRepository.findByUserIdOrderByGmtCreateDesc(userId, PageRequest.of(pageNumber-1, 30));
        return PageResult.of(byUserIdOrderByCreateTimeDesc);
    }

    @Override
    public PageResult getSelf(Integer pageNumber) {
        return get(sysUserService.getIdOfSelf(), pageNumber);
    }
}
