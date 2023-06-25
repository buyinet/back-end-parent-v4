package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.module.entity.RelBusOvoChatRoomAndBusOvoUserBind;
import com.kantboot.business.ovo.module.vo.BusOvoChatRoomVO;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserBindRepository;
import com.kantboot.business.ovo.service.repository.RelBusOvoChatRoomAndBusOvoUserBindRepository;
import com.kantboot.business.ovo.service.service.IBusOvoChatRoomService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    private BusOvoChatRoomRepository repository;

    @Resource
    private BusOvoChatRepository chatRepository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private BusOvoUserBindRepository userBindRepository;

    @Resource
    private RelBusOvoChatRoomAndBusOvoUserBindRepository relBusOvoChatRoomAndBusOvoUserBindRepository;

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

        return repository.save(chatRoom);
    }

    @Override
    public HashMap<String,Object> getSelfRoom(Integer pageNumber) {
        Pageable pageable = Pageable.ofSize(50).withPage(pageNumber-1);
        Page<RelBusOvoChatRoomAndBusOvoUserBind> byUserId
                = relBusOvoChatRoomAndBusOvoUserBindRepository.findByUserId(pageable,
                sysUserService.getIdOfSelf());

        List<RelBusOvoChatRoomAndBusOvoUserBind> byUserIdContent = byUserId.getContent();
        List<BusOvoChatRoomVO> content = new ArrayList<>();
        for (RelBusOvoChatRoomAndBusOvoUserBind relBusOvoChatRoomAndBusOvoUserBind : byUserIdContent) {
            BusOvoChatRoomVO busOvoChatRoomVO = new BusOvoChatRoomVO();
            // 传入房间id
            busOvoChatRoomVO.setRoomId(relBusOvoChatRoomAndBusOvoUserBind.getRoomId());
            BusOvoChatRoom busOvoChatRoom = repository.findById(relBusOvoChatRoomAndBusOvoUserBind.getRoomId()).get();
            busOvoChatRoomVO.setTypeCode(busOvoChatRoom.getTypeCode());
            busOvoChatRoomVO.setId(busOvoChatRoom.getId());
            busOvoChatRoom.setName(busOvoChatRoom.getName());
            boolean privateChat = busOvoChatRoom.getTypeCode().equals("privateChat");
            if(privateChat){
                BusOvoChat firstByRoomIdOrderByCreateTimeDesc = chatRepository.findFirstByRoomIdOrderByGmtCreateDesc(busOvoChatRoom.getId());
                busOvoChatRoomVO.setLastChat(firstByRoomIdOrderByCreateTimeDesc);
                // 如果是私聊，那么就要获取对方的信息
                List<BusOvoUserBind> ovoUserList = busOvoChatRoom.getOvoUserList();
                for (BusOvoUserBind busOvoUserBind : ovoUserList) {
                    if (!busOvoUserBind.getUserId().equals(sysUserService.getIdOfSelf())){
                        busOvoChatRoomVO.setOtherUser(busOvoUserBind);
                        busOvoChatRoomVO.setName(busOvoUserBind.getUser().getNickname());
                        busOvoChatRoomVO.setFileIdOfAvatar(busOvoUserBind.getUser().getFileIdOfAvatar());
                    }
                }
            }
            content.add(busOvoChatRoomVO);
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("content",content);
        map.put("totalPage", byUserId.getTotalPages());
        map.put("totalElements",byUserId.getTotalElements());
        map.put("number",byUserId.getNumber()+1);
        map.put("size", byUserId.getSize());
        return map;
    }

    @Override
    public BusOvoChatRoomVO getById(Long id) {
        BusOvoChatRoom busOvoChatRoom = repository.findById(id).get();
        BusOvoChatRoomVO busOvoChatRoomVO = new BusOvoChatRoomVO();
        // 传入房间id
        busOvoChatRoomVO.setTypeCode(busOvoChatRoom.getTypeCode());
        busOvoChatRoomVO.setId(busOvoChatRoom.getId());
        busOvoChatRoom.setName(busOvoChatRoom.getName());
        boolean privateChat = busOvoChatRoom.getTypeCode().equals("privateChat");
        if(privateChat){
            BusOvoChat firstByRoomIdOrderByCreateTimeDesc = chatRepository.findFirstByRoomIdOrderByGmtCreateDesc(busOvoChatRoom.getId());
            busOvoChatRoomVO.setLastChat(firstByRoomIdOrderByCreateTimeDesc);
            // 如果是私聊，那么就要获取对方的信息
            List<BusOvoUserBind> ovoUserList = busOvoChatRoom.getOvoUserList();
            for (BusOvoUserBind busOvoUserBind : ovoUserList) {
                if (!busOvoUserBind.getUserId().equals(sysUserService.getIdOfSelf())){
                    busOvoChatRoomVO.setOtherUser(busOvoUserBind);
                    busOvoChatRoomVO.setName(busOvoUserBind.getUser().getNickname());
                    busOvoChatRoomVO.setFileIdOfAvatar(busOvoUserBind.getUser().getFileIdOfAvatar());
                }
            }
        }
        return busOvoChatRoomVO;
    }
}










