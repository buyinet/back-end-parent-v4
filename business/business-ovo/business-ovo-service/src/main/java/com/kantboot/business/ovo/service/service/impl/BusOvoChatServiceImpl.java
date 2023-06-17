package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.business.ovo.service.service.IBusOvoChatService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ovo用户聊天室表Service接口实现类
 *
 * @author 方某方
 */
@Service
public class BusOvoChatServiceImpl
        implements IBusOvoChatService {

    @Resource
    private BusOvoChatRoomRepository chatRoomRepository;

    @Resource
    private IBusOvoChatRoomService chatRoomService;

    @Resource
    private BusOvoChatRepository repository;

    @Resource
    private ISysUserService sysUserService;

    /**
     * 与私人进行聊天
     *
     * @param otherUserId 对方用户id
     * @return 聊天内容
     */
    @Override
    public void chatPrivate(Long otherUserId, String content) {
        // 根据两个用户id查询聊天室
        BusOvoChatRoom privateChatRoom = chatRoomRepository.findOne(
                Example.of(new BusOvoChatRoom().setOvoUserList(
                                        List.of(
                                                new BusOvoUserBind()
                                                        .setUserId(sysUserService.getIdOfSelf()),
                                                new BusOvoUserBind()
                                                        .setUserId(otherUserId)
                                        )
                                )
                )).get();

        if (privateChatRoom == null){
            privateChatRoom = chatRoomService.createPrivateChatRoom(otherUserId);
        }

        privateChatRoom.setTypeCode("privateChat");
        repository.save(
                new BusOvoChat()
                        .setChatRoomId(privateChatRoom.getId())
                        .setUserId(otherUserId)
        );

        // TODO: 发送消息

    }

}
