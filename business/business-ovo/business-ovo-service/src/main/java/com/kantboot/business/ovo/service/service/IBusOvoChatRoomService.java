package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.vo.BusOvoChatRoomVO;

/**
 * Ovo用户聊天室表Service接口
 * @author 方某方
 */
public interface IBusOvoChatRoomService {

    /**
     * 创建私聊聊天室
     * @param otherUserId 对方用户id
     * @return 聊天室
     */
    BusOvoChatRoom createPrivateChatRoom(Long otherUserId);


}
