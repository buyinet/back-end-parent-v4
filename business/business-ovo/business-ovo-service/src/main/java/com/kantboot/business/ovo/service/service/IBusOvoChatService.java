package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoChat;

import java.util.HashMap;

/**
 * Ovo用户聊天室表Service接口
 * @author 方某方
 */
public interface IBusOvoChatService {

    /**
     * 与私人进行聊天
     * @param otherUserId 对方用户id
     * @param typeCode 聊天类型
     * @param content 聊天内容
     * @return 聊天内容
     */
    BusOvoChat chatPrivate(Long otherUserId, String typeCode, String content);

    /**
     * 查询聊天室的聊天记录
     * @param roomId 聊天室id
     * @param pageNumber 页码
     * @return 聊天记录
     */
    HashMap<String,Object> getByRoomId(Long roomId, Integer pageNumber);

}
