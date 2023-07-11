package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoChatService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 与私人进行聊天
     * @param otherUserId 对方用户id
     * @param content 聊天内容
     * @param typeCode 聊天类型
     * @return 聊天内容
     */
    @RequestMapping("/chatPrivate")
    public RestResult chatPrivate(
            @RequestParam("otherUserId")
            Long otherUserId,
            @RequestParam("typeCode")
            String typeCode,
            @RequestParam("content")
            String content,
            @RequestParam("duration")
            Long duration,
            @RequestParam("fileIdOfCover")
            Long fileIdOfCover){
        return stateSuccessService.success(service.chatPrivate(otherUserId, typeCode,content,duration,fileIdOfCover),"sendSuccess");
    }


    @RequestMapping("/getByRoomId")
    public RestResult getByRoomId(Long roomId, Integer pageNumber){
        return stateSuccessService.success(service.getByRoomId(roomId,pageNumber),"getSuccess");
    }

    @RequestMapping("/findLess")
    public RestResult findLess(Long roomId, Long id){
        return stateSuccessService.success(service.findLess(roomId,id),"getSuccess");
    }


}
