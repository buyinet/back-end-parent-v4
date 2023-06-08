package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 帖子的repository
 * @author 方某方
 */
public interface BusOvoPostRepository extends JpaRepository<BusOvoPost,Long> {

}
