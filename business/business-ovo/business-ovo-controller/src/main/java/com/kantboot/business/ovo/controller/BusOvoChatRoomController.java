package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo用户聊天室表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/chatRoom")
public class BusOvoChatRoomController {

    @Resource
    private IBusOvoChatRoomService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 创建私聊聊天室
     * @param pageNumber 页码
     * @return 聊天室
     */
    @RequestMapping("/getSelfRoom")
    public RestResult getSelfRoom(Integer pageNumber){
        return stateSuccessService.success(service.getSelfRoom(pageNumber),"getSuccess");
    }

    /**
     * 根据id查询
     * @param id id
     * @return 聊天室
     */
    @RequestMapping("/getById")
    public RestResult getById(Long id){
        return stateSuccessService.success(service.getById(id),"getSuccess");
    }

    /**
     * 根据对方用户id获取私人聊天室
     * @param otherUserId 对方用户id
     * @return 聊天室
     */
    @RequestMapping("/getPrivateChatRoomByOtherUserId")
    public RestResult getPrivateChatRoomByOtherUserId(Long otherUserId){
        return stateSuccessService.success(service.getPrivateChatRoomByOtherUserId(otherUserId),"getSuccess");
    }

}
