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

    /**
     * 根据两个用户id查询房间，另外一个表是rel_bus_ovo_chat_room_and_bus_ovo_user_bind，里面有user_id和room_id
     * @param userId1 用户id1
     * @param userId2 用户id2
     * @return 房间
     */
    @Query(value = "select * from bus_ovo_chat_room where id in (select room_id from rel_bus_ovo_chat_room_and_bus_ovo_user_bind where user_id = ?1 and room_id in (select room_id from rel_bus_ovo_chat_room_and_bus_ovo_user_bind where user_id = ?2))",nativeQuery = true)
    BusOvoChatRoom findByUserId1AndUserId2(Long userId1, Long userId2);


    /**
     * 根据两个用户id查询房间，并且是私聊，
     * 另外一个表是rel_bus_ovo_chat_room_and_bus_ovo_user_bind，里面有user_id和room_id
     * @param userId1 用户id1
     * @param userId2 用户id2
     * @return 房间
     */
    @Query(value = "select * from bus_ovo_chat_room where id in (select room_id from rel_bus_ovo_chat_room_and_bus_ovo_user_bind where user_id = ?1 and room_id in (select room_id from rel_bus_ovo_chat_room_and_bus_ovo_user_bind where user_id = ?2)) and type_code = 'privateChat'",nativeQuery = true)
    BusOvoChatRoom findPrivateByUserId1AndUserId2(Long userId1, Long userId2);
}
