package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.RelBusOvoChatRoomAndBusOvoUserBind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ovo用户聊天室表和Ovo用户绑定表关联表Repository接口
 * @author 方某方
 */
public interface RelBusOvoChatRoomAndBusOvoUserBindRepository
extends JpaRepository<RelBusOvoChatRoomAndBusOvoUserBind, Long>
{

    /**
     * 根据用户id查询
     * @param pageable 分页
     * @param userId 用户id
     * @return
     */
    Page<RelBusOvoChatRoomAndBusOvoUserBind> findByUserId(Pageable pageable, Long userId);


}
