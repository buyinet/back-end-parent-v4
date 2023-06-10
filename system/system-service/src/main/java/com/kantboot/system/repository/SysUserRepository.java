package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * 用户仓库
 * @author 方某方
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long>, Repository<SysUser, Long> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 根据用户名查询用户数量
     * @param username 用户名
     * @return 用户数量
     */
    Long countByUsername(String username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     *              会忽略大小写
     * @return 用户
     */
    SysUser findByEmailIgnoreCase(String email);

    /**
     * 根据邮箱查询用户数量
     * @param email 邮箱
     *              会忽略大小写
     * @return 用户数量
     */
    Long countByEmailIgnoreCase(String email);

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户
     */
    SysUser findByPhone(String phone);

    /**
     * 根据手机号查询用户数量
     * @param phone 手机号
     * @return 用户数量
     */
    Long countByPhone(String phone);

    /**
     * 修改用户头像id
     * @param id 用户id
     * @param avatarId 头像id
     * @return 修改后的用户
     */
    @Query("update SysUser u set u.fileIdOfAvatar = ?2 where u.id = ?1")
    SysUser updateAvatarIdById(Long id, Long avatarId);

}
