package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoChat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ovo用户聊天室表Repository接口
 * @author 方某方
 */
public interface BusOvoChatRepository
extends JpaRepository<BusOvoChat, Long>
{
}
