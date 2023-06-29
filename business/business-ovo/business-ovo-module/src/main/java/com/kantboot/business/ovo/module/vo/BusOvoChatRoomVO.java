package com.kantboot.business.ovo.module.vo;

import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoUser;
import lombok.Data;

import java.util.Date;

/**
 * Ovo用户聊天室表VO
 * @author 方某方
 */
@Data
public class BusOvoChatRoomVO {

    /**
     * 聊天室id
     */
    private Long id;


    /**
     * 聊天室名称
     */
    private String name;

    /**
     * 群头像id
     */
    private Long fileIdOfAvatar;

    /**
     * 聊天室类型编码
     * privateChat 私聊
     * groupChat 群聊
     */
    private String typeCode;


    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 最后一条消息
     */
    private BusOvoChat lastChat;

    /**
     * 聊天室对方
     * 只有私聊才有
     */
    private BusOvoUser otherUser;

    /**
     * 创建时间
     */
    private Date gmtCreate;


}
