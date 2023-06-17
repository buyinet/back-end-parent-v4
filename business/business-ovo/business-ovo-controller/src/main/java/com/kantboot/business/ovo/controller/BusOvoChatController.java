package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoChatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo用户聊天表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/chat")
public class BusOvoChatController {

    @Resource
    private IBusOvoChatService service;

    /**
     * 与私人进行聊天
     * @param otherUserId 对方用户id
     * @return 聊天内容
     */
    @RequestMapping("/chatPrivate")
    public void chatPrivate(Long otherUserId,String content){
        service.chatPrivate(otherUserId, content);
    }



}
