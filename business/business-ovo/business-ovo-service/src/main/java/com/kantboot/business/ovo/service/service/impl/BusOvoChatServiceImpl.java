package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.api.util.uni.push.PushUtil;
import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.module.entity.BusPush;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.business.ovo.service.service.IBusOvoChatService;
import com.kantboot.business.ovo.service.service.IBusPushService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Resource
    private IBusPushService pushService;

    /**
     * 与私人进行聊天
     *
     * @param otherUserId 对方用户id
     * @return 聊天内容
     */
    @Override
    public void chatPrivate(Long otherUserId, String content) {
        Long userIdOfSelf = sysUserService.getIdOfSelf();
        // 根据两个用户id查询聊天室
        BusOvoChatRoom privateChatRoom = chatRoomRepository.findOne(
                Example.of(new BusOvoChatRoom().setOvoUserList(
                                        List.of(
                                                new BusOvoUserBind()
                                                        .setUserId(userIdOfSelf),
                                                new BusOvoUserBind()
                                                        .setUserId(otherUserId)
                                        )
                                )
                )).get();

        if (privateChatRoom == null){
            // 如果没有聊天室，则创建一个
            privateChatRoom = chatRoomService.createPrivateChatRoom(otherUserId);
        }

        privateChatRoom.setTypeCode("privateChat");
        // 发送消息
        repository.save(
                new BusOvoChat()
                        .setChatRoomId(privateChatRoom.getId())
                        .setUserId(otherUserId)
        );

        // TODO: 发送消息，先记下，明天再改
        List<BusPush> byUserId = pushService.getByUserId(otherUserId);
        for (BusPush busPush : byUserId) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("cid",busPush.getCid());
            map.put("title","您有一条新消息");
            map.put("content",content);
            map.put("forceNotification",false);
            HashMap<String,Object> payload = new HashMap<>();
            payload.put("type","text");
            payload.put("chatRoomId",privateChatRoom.getId());
            payload.put("content",content);
            map.put("payload",payload);
            PushUtil.sendPush(map);
        }

        List<BusPush> byUserId2 = pushService.getByUserId(userIdOfSelf);
        for (BusPush busPush : byUserId2) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("cid",busPush.getCid());
            map.put("title","您发送成功了消息");
            map.put("content",content);
            map.put("forceNotification",false);
            HashMap<String,Object> payload = new HashMap<>();
            payload.put("type","text");
            payload.put("chatRoomId",privateChatRoom.getId());
            payload.put("content",content);
            map.put("payload",payload);
            PushUtil.sendPush(map);
        }


    }

}
