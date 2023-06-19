package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusPushBind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Ovo用户推送表Repository接口
 * @author 方某方
 */
public interface BusPushBindRepository extends JpaRepository<BusPushBind,Long> {

    /**
     * 根据用户id查询推送
     * @param userId 用户id
     * @return 推送列表
     */
    List<BusPushBind> findByUserId(Long userId);

    /**
     * 根据cid查询推送
     * @param cid cid
     * @return 推送
     */
    BusPushBind findByCid(String cid);
}
