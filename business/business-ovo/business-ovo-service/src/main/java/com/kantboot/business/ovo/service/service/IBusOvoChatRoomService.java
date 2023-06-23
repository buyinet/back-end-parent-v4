package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.vo.BusOvoChatRoomVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Objects;

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

    /**
     * 查询用户自身的房间
     * @param pageNumber 页码
     * @return 房间
     */
    HashMap<String, Object> getSelfRoom(Integer pageNumber);

    /**
     * 根据id查询
     * @param id id
     * @return 聊天室
     */
    BusOvoChatRoomVO getById(Long id);



}
