package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.util.common.result.PageResult;

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
     * @param duration 语音时长
     * @param fileIdOfCover 封面的文件id
     * @return 聊天内容
     */
    BusOvoChat chatPrivate(Long otherUserId, String typeCode, String content,Long duration,Long fileIdOfCover);

    /**
     * 查询聊天室的聊天记录
     * @param roomId 聊天室id
     * @param pageNumber 页码
     * @return 聊天记录
     */
    HashMap<String,Object> getByRoomId(Long roomId, Integer pageNumber);

    /**
     * findByRoomIdAndIdLessThanOrderByGmtCreateDesc
     * @param roomId 聊天室id
     * @param id 聊天id
     * @return 聊天记录
     */
    PageResult findLess(Long roomId, Long id);

}
