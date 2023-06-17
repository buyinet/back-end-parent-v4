package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Ovo用户聊天室表Repository接口
 * @author 方某方
 */
public interface BusOvoChatRoomRepository
        extends JpaRepository<BusOvoChatRoom, Long> {

}
