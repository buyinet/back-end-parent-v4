package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.module.entity.BusOvoChat;
import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import com.kantboot.business.ovo.module.entity.BusOvoUser;
import com.kantboot.business.ovo.module.entity.RelBusOvoChatRoomAndBusOvoUser;
import com.kantboot.business.ovo.module.vo.BusOvoChatRoomVO;
import com.kantboot.business.ovo.service.repository.BusOvoChatRepository;
import com.kantboot.business.ovo.service.repository.BusOvoChatRoomRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserRepository;
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
    private BusOvoUserRepository userBindRepository;

    @Resource
    private RelBusOvoChatRoomAndBusOvoUserBindRepository relBusOvoChatRoomAndBusOvoUserBindRepository;

    /**
     * 建立私人聊天室
     * @param otherUserId 对方用户id
     * @return 聊天室
     */
    @Override
    public BusOvoChatRoom createPrivateChatRoom(Long otherUserId) {

        List<BusOvoUser> userBindList = new ArrayList<>();
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
    public BusOvoChatRoomVO getPrivateChatRoomByOtherUserId(Long otherUserId) {
//        根据对方用户id和自身用户id查询
        BusOvoChatRoom busOvoChatRoom = repository.findPrivateByUserId1AndUserId2(
                sysUserService.getIdOfSelf(), otherUserId
        );
        System.err.println(JSON.toJSONString(busOvoChatRoom));
        if (busOvoChatRoom==null) {
            return null;
        }
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
            List<BusOvoUser> ovoUserList = busOvoChatRoom.getOvoUserList();
            for (BusOvoUser busOvoUserBind : ovoUserList) {
                busOvoUserBind.setUser(sysUserService.getById(busOvoUserBind.getUserId()));
                if (!busOvoUserBind.getUserId().equals(sysUserService.getIdOfSelf())){
                    busOvoChatRoomVO.setOtherUser(busOvoUserBind);
                    busOvoChatRoomVO.setName(busOvoUserBind.getUser().getNickname());
                    busOvoChatRoomVO.setFileIdOfAvatar(busOvoUserBind.getUser().getFileIdOfAvatar());
                }
            }
        }
        return busOvoChatRoomVO;
    }

    @Override
    public HashMap<String,Object> getSelfRoom(Integer pageNumber) {
        Pageable pageable = Pageable.ofSize(50).withPage(pageNumber-1);
        Page<RelBusOvoChatRoomAndBusOvoUser> byUserId
                = relBusOvoChatRoomAndBusOvoUserBindRepository.findByUserId(pageable,
                sysUserService.getIdOfSelf());

        List<RelBusOvoChatRoomAndBusOvoUser> byUserIdContent = byUserId.getContent();
        List<BusOvoChatRoomVO> content = new ArrayList<>();
        for (RelBusOvoChatRoomAndBusOvoUser relBusOvoChatRoomAndBusOvoUserBind : byUserIdContent) {
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
                List<BusOvoUser> ovoUserList = busOvoChatRoom.getOvoUserList();
                for (BusOvoUser busOvoUserBind : ovoUserList) {
                    busOvoUserBind.setUser(sysUserService.getById(busOvoUserBind.getUserId()));
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
            List<BusOvoUser> ovoUserList = busOvoChatRoom.getOvoUserList();
            for (BusOvoUser busOvoUserBind : ovoUserList) {
                busOvoUserBind.setUser(sysUserService.getById(busOvoUserBind.getUserId()));
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











