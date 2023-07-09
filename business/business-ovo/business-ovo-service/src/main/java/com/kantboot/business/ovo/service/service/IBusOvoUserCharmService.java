package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.vo.BusOvoUserCharmTopVO;
import com.kantboot.business.ovo.service.mapper.BusOvoUserCharmMapper;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * 用户魅力值的service接口
 * 用于处理用户魅力值的业务逻辑
 * @author 方某方
 */
public interface IBusOvoUserCharmService {

    /**
     * 获取魅力值排行榜
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 魅力值排行榜
     */
    List<BusOvoUserCharmTopVO> getTopByTimestamp(Long startTimestamp, Long endTimestamp);

    /**
     * getTopByTimestampAndUserId
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @param userId 用户id
     * @return 魅力值排行榜
     */
    BusOvoUserCharmTopVO getTopByTimestampAndUserId(Long startTimestamp, Long endTimestamp, Long userId);

    /**
     * 获取自己的魅力值
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 魅力值
     */
    BusOvoUserCharmTopVO getCharmSelf(Long startTimestamp, Long endTimestamp);

    /**
     * 获取守护者列表
     * @param userId 用户id
     * @return 守护者列表
     */
    List<BusOvoUserCharmTopVO> getGuardiansByUserId(Long userId);

    /**
     * 获取自己的守护者
     * @return 自己的守护者
     */
    List<BusOvoUserCharmTopVO> getGuardiansOfSelf();
}
