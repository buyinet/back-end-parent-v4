package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.dto.GiveGiftDto;
import com.kantboot.business.ovo.module.entity.BusOvoGift;

import java.util.List;
import java.util.Map;

/**
 * 礼物表的业务接口类
 * @author 方某方
 */
public interface IBusOvoGiftService {

    /**
     * 获取礼物字典
     * @return 礼物字典
     */
    Map<String,Object> getMap();

    /**
     * 根据礼物编码查询礼物
     * @param code 礼物编码
     * @return 礼物
     */
    BusOvoGift getByCode(String code);

    /**
     * 获取所有礼物
     * @return 所有礼物
     */
    List<BusOvoGift> getAll();

    /**
     * 赠送礼物
     * @param giveGiftDto 赠送礼物的数据传输对象
     */
    void give(GiveGiftDto giveGiftDto);

}
