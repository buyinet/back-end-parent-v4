package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.vo.BusOvoUserCharmTopVO;
import com.kantboot.business.ovo.service.mapper.BusOvoUserCharmMapper;
import com.kantboot.business.ovo.service.service.IBusOvoUserCharmService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusOvoUserCharmServiceImpl
implements IBusOvoUserCharmService
{

    @Resource
    private BusOvoUserCharmMapper busOvoUserCharmMapper;

    @Resource
    private ISysUserService sysUserService;

    @Override
    public List<BusOvoUserCharmTopVO> getTopByTimestamp(Long startTimestamp, Long endTimestamp) {
        return busOvoUserCharmMapper.getTopByTimestamp(startTimestamp, endTimestamp);
    }

    @Override
    public BusOvoUserCharmTopVO getTopByTimestampAndUserId(Long startTimestamp, Long endTimestamp, Long userId) {
        return busOvoUserCharmMapper.getTopByTimestampAndUserId(startTimestamp, endTimestamp, userId);
    }

    @Override
    public BusOvoUserCharmTopVO getCharmSelf(Long startTimestamp, Long endTimestamp) {
        return getTopByTimestampAndUserId(startTimestamp, endTimestamp, sysUserService.getIdOfSelf());
    }

    @Override
    public List<BusOvoUserCharmTopVO> getGuardiansByUserId(Long userId) {
        return busOvoUserCharmMapper.getGuardiansByUserId(userId);
    }

    @Override
    public List<BusOvoUserCharmTopVO> getGuardiansOfSelf() {
        return getGuardiansByUserId(sysUserService.getIdOfSelf());
    }
}
