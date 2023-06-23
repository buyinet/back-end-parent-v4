package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ovo用户聊天室表Repository接口
 * @author 方某方
 */
public interface BusOvoChatRepository
extends JpaRepository<BusOvoChat, Long>
{
    /**
     * 根据房间号查询最后一条消息
     * @param roomId 房间号
     * @return 最后一条消息
     */
    BusOvoChat findFirstByRoomIdOrderByGmtCreateDesc(Long roomId);

    /**
     * 根据房间号获取消息，分页
     * @param pageable 分页
     * @param roomId 房间号
     * @return 消息
     */
    Page<BusOvoChat> findByRoomIdOrderByGmtCreateDesc(Pageable pageable,Long roomId);

    /**
     * 根据房间号获取消息数量
     * @param roomId 房间号
     * @return 消息数量
     */
    Long countByRoomId(Long roomId);

    /**
     * 获取最后一页的页码
     * @param roomId 房间号
     * @param pageSize 页大小
     * @return 最后一页
     */
    default Long getLastPage(Long roomId,Integer pageSize) {
        Long count = countByRoomId(roomId);
        return count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
    }

}
