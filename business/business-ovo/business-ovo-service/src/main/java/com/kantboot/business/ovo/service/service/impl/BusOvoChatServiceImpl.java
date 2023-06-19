package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.api.module.ApiPush;
import com.kantboot.api.module.ApiPushPayload;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.module.entity.BusPushBind;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.business.ovo.service.service.IBusOvoChatService;
import com.kantboot.business.ovo.service.service.IBusPushBindService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    private IBusPushBindService pushService;

    @Resource
    private IApiPushService apiPushService;

    /**
     * 与私人进行聊天
     *
     * @param otherUserId 对方用户id
     * @param typeCode 聊天类型
     * @param content 聊天内容
     * @return 聊天内容
     */
    @Override
    public void chatPrivate(Long otherUserId,String typeCode, String content) {
        Long userIdOfSelf = sysUserService.getIdOfSelf();
        // 根据两个用户id查询聊天室
        BusOvoChatRoom privateChatRoom = null;
        try{
            privateChatRoom = chatRoomRepository.findOne(
                    Example.of(new BusOvoChatRoom().setOvoUserList(
                                    List.of(
                                            new BusOvoUserBind()
                                                    .setUserId(userIdOfSelf),
                                            new BusOvoUserBind()
                                                    .setUserId(otherUserId)
                                    )
                            )
                    )).get();
        }catch (NoSuchElementException e){
            privateChatRoom = null;
        }

        if (privateChatRoom == null){
            // 如果没有聊天室，则创建一个
            privateChatRoom = chatRoomService.createPrivateChatRoom(otherUserId);
        }

        privateChatRoom.setTypeCode("privateChat");

        // 发送消息
        BusOvoChat save = repository.save(
                new BusOvoChat()
                        .setChatRoomId(privateChatRoom.getId())
                        .setUserIdOfSend(userIdOfSelf)
                        .setTypeCode(typeCode)
                        .setContent(content)
        );


        List<BusPushBind> byUserId = pushService.getByUserId(otherUserId);
        for (BusPushBind busPush : byUserId) {
            ApiPush apiPush = new ApiPush();
            apiPush.setCid(busPush.getCid());
            apiPush.setTitle("您有一条新消息");
            apiPush.setContent(content);
            apiPush.setForceNotification(false);
            ApiPushPayload apiPushPayload = new ApiPushPayload();

            apiPushPayload.setEmit("chat:userIdOfSend:"+userIdOfSelf);
            apiPushPayload.setData(save);

            apiPush.setPayload(apiPushPayload);
            apiPushService.push(apiPush);
        }

        List<BusPushBind> userOfSend = pushService.getByUserId(userIdOfSelf);
        for (BusPushBind busPush : userOfSend) {
            ApiPush apiPush = new ApiPush();
            apiPush.setCid(busPush.getCid());
            apiPush.setTitle("您发送了一条新消息");
            apiPush.setContent(content);
            apiPush.setForceNotification(false);
            ApiPushPayload apiPushPayload = new ApiPushPayload();

            apiPushPayload.setEmit("chat:userIdOfReceive:"+userIdOfSelf);
            apiPushPayload.setData(save);

            apiPush.setPayload(apiPushPayload);
            apiPushService.push(apiPush);
        }


    }

}
