package com.kantboot.business.ovo.service.service;

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
    void chatPrivate(Long otherUserId,String typeCode, String content);

}
