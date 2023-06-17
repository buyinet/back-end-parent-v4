package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserBindRepository;
import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Ovo用户聊天室表Service接口实现类
 * @author 方某方
 */
@Service
public class BusOvoChatRoomServiceImpl
        implements IBusOvoChatRoomService
{

    @Resource
    private BusOvoChatRoomRepository ovoChatRoomRepository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private BusOvoUserBindRepository userBindRepository;

    /**
     * 建立私人聊天室
     * @param otherUserId 对方用户id
     * @return 聊天室
     */
    @Override
    public BusOvoChatRoom createPrivateChatRoom(Long otherUserId) {

        List<BusOvoUserBind> userBindList = new ArrayList<>();
        userBindList.add(
                userBindRepository.findByUserId(sysUserService.getIdOfSelf())
        );
        userBindList.add(
                userBindRepository.findByUserId(otherUserId)
        );

        BusOvoChatRoom chatRoom = new BusOvoChatRoom()
                .setOvoUserList(userBindList);

        return ovoChatRoomRepository.save(chatRoom);
    }

}
