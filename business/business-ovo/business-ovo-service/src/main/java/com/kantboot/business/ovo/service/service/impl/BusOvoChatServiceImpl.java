package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.api.module.ApiPush;
import com.kantboot.api.module.ApiPushPayload;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUser;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.business.ovo.service.service.IBusOvoChatService;
import com.kantboot.business.ovo.service.service.IBusOvoUserService;
import com.kantboot.business.ovo.service.service.IBusPushBindService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Resource
    private IBusPushBindService pushBindService;

    @Resource
    private IBusOvoUserService ovoUserBindService;


    /**
     * 与私人进行聊天
     *
     * @param otherUserId 对方用户id
     * @param typeCode    聊天类型
     * @param content     聊天内容
     * @return 聊天内容
     */
    @Override
    public BusOvoChat chatPrivate(Long otherUserId, String typeCode, String content, Long duration, Long fileIdOfCover) {
        BusOvoUser userOfSelf = ovoUserBindService.getSelf();
        // 根据两个用户id查询聊天室
        BusOvoChatRoom privateChatRoom = chatRoomRepository.findByUserId1AndUserId2(userOfSelf.getUserId(), otherUserId);

        if (privateChatRoom == null) {
            // 如果没有聊天室，则创建一个
            privateChatRoom = chatRoomService.createPrivateChatRoom(otherUserId);
        }
        privateChatRoom.setTypeCode("privateChat");

        // 发送消息
        BusOvoChat save = repository.save(
                new BusOvoChat()
                        .setRoomId(privateChatRoom.getId())
                        .setUserIdOfSend(userOfSelf.getUserId())
                        .setTypeCode(typeCode)
                        .setContent(content)
                        .setDuration(duration)
                        .setFileIdOfCover(fileIdOfCover)
        );

        BusOvoChat busOvoChat = repository.findById(save.getId()).orElseThrow(NoSuchElementException::new);
        ApiPush apiPush = new ApiPush();
        apiPush.setTitle("OVO提示: 您有一条 ["+userOfSelf.getUser().getNickname()+"] 发来的消息");
        String contentValue = "[消息]";
        if (typeCode.equals("text")) {
            contentValue = content.length() > 5 ? content.substring(0, 5) + "..." : content;
        }else if (typeCode.equals("image")){
            contentValue = "[图片]";
        }else if (typeCode.equals("video")){
            contentValue = "[视频]";
        }else if (typeCode.equals("voice")){
            contentValue = "[语音]";
        }

        apiPush.setContent(userOfSelf.getUser().getNickname()+": "+contentValue);
        apiPush.setForceNotification(false);
        ApiPushPayload apiPushPayload = new ApiPushPayload();

        apiPushPayload.setEmit("newChat");
        apiPushPayload.setData(busOvoChat);

        apiPush.setPayload(apiPushPayload);
        pushBindService.pushByUserIdWithEmail(otherUserId, apiPush);

        ApiPush apiPush2 = new ApiPush();
        apiPush2.setTitle("您发送了一条新消息");
        apiPush2.setContent(content);
        apiPush2.setForceNotification(false);
        ApiPushPayload apiPushPayload1 = new ApiPushPayload();

        apiPushPayload1.setEmit("newChat");
        apiPushPayload1.setData(busOvoChat);

        apiPush2.setPayload(apiPushPayload);
        pushBindService.pushByUserId(userOfSelf.getUserId(), apiPush2);

        return save;
    }

    @Override
    public HashMap<String, Object> getByRoomId(Long roomId, Integer pageNumber) {
        Pageable pageable = Pageable.ofSize(10).withPage(pageNumber - 1);
        Page<BusOvoChat> byRoomIdOrderByGmtCreateDesc = repository.findByRoomIdOrderByGmtCreateDesc(pageable, roomId);

        HashMap<String, Object> map = new HashMap<>(10);
        map.put("content", byRoomIdOrderByGmtCreateDesc.getContent());
        map.put("totalPages", byRoomIdOrderByGmtCreateDesc.getTotalPages());
        map.put("number", byRoomIdOrderByGmtCreateDesc.getNumber());
        map.put("totalElements", byRoomIdOrderByGmtCreateDesc.getTotalElements());
        return map;
    }
}
