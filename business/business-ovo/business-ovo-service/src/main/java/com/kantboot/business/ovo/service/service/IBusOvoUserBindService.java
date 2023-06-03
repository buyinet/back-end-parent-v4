package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.dto.BusOvoUserBindDto;
import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;

import java.util.Date;
import java.util.List;

/**
 * 绑定的用户
 * @author 方某方
 */
public interface IBusOvoUserBindService {


    /**
     * 根据用户id获取用户绑定信息
     * @param dto 用户绑定信息
     * @return 用户绑定信息
     */
    BusOvoUserBind bind(BusOvoUserBindDto dto);

    /**
     * 获取用户自身的绑定信息
     * @return 用户绑定信息
     */
    BusOvoUserBind getSelf();
}
