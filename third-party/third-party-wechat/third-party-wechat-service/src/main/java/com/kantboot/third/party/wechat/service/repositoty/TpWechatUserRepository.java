package com.kantboot.third.party.wechat.service.repositoty;

import com.kantboot.third.party.module.entity.TpWechatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 第三方微信用户仓库
 * @author 方某方
 */
public interface TpWechatUserRepository extends JpaRepository<TpWechatUser, Long>, Repository<TpWechatUser, Long> {

    /**
     * 根据openId查询
     * @param unionid 微信用户唯一标识
     * @return 微信用户
     */
    TpWechatUser findByUnionid(String unionid);

}
