package com.kantboot.business.ovo.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.api.module.ApiPush;
import com.kantboot.api.module.ApiPushPayload;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.module.entity.BusPushBind;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.service.*;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
    private IBusOvoUserBindService ovoUserBindService;


    /**
     * 与私人进行聊天
     *
     * @param otherUserId 对方用户id
     * @param typeCode 聊天类型
     * @param content 聊天内容
     * @return 聊天内容
     */
    @Override
    public BusOvoChat chatPrivate(Long otherUserId,String typeCode, String content,Long duration,Long fileIdOfCover) {
        BusOvoUserBind userOfSelf = ovoUserBindService.getSelf();
        // 根据两个用户id查询聊天室
        BusOvoChatRoom privateChatRoom = chatRoomRepository.findByUserId1AndUserId2(userOfSelf.getUserId(), otherUserId);

        if (privateChatRoom == null){
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

        // 多线程推送消息
        ThreadUtil.execute(() -> {
            System.out.println("发送了消息");
            List<BusPushBind> byUserId = pushService.getByUserId(otherUserId);
            for (BusPushBind busPush : byUserId) {
                ApiPush apiPush = new ApiPush();
                apiPush.setCid(busPush.getCid());
                apiPush.setTitle("您有一条新消息");
                apiPush.setContent(content);
                apiPush.setForceNotification(false);
                ApiPushPayload apiPushPayload = new ApiPushPayload();

                apiPushPayload.setEmit("newChat");
                apiPushPayload.setData(busOvoChat);

                apiPush.setPayload(apiPushPayload);
                apiPushService.push(apiPush);
            }

            List<BusPushBind> userOfSend = pushService.getByUserId(userOfSelf.getUserId());
            for (BusPushBind busPush : userOfSend) {
                ApiPush apiPush = new ApiPush();
                apiPush.setCid(busPush.getCid());
                apiPush.setTitle("您发送了一条新消息");
                apiPush.setContent(content);
                apiPush.setForceNotification(false);
                ApiPushPayload apiPushPayload = new ApiPushPayload();

                apiPushPayload.setEmit("newChat");
                apiPushPayload.setData(busOvoChat);

                apiPush.setPayload(apiPushPayload);
                apiPushService.push(apiPush);
            }
        });

        return save;
    }

    @Override
    public HashMap<String, Object> getByRoomId(Long roomId, Integer pageNumber) {
        Pageable pageable = Pageable.ofSize(10).withPage(pageNumber-1);
        Page<BusOvoChat> byRoomIdOrderByGmtCreateDesc = repository.findByRoomIdOrderByGmtCreateDesc(pageable, roomId);
        HashMap<String, Object> map = new HashMap<>(10);
        map.put("content",byRoomIdOrderByGmtCreateDesc.getContent());
        map.put("totalPages",byRoomIdOrderByGmtCreateDesc.getTotalPages());
        map.put("number",byRoomIdOrderByGmtCreateDesc.getNumber());
        map.put("totalElements",byRoomIdOrderByGmtCreateDesc.getTotalElements());
        return map;
    }
}
