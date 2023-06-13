package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoPostComment;
import com.kantboot.business.ovo.service.repository.BusOvoPostCommentRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostCommentService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 帖子评论的service实现类
 * @author 方某方
 */
@Service
public class BusOvoPostCommentServiceImpl implements IBusOvoPostCommentService {

    @Resource
    private BusOvoPostCommentRepository repository;


    @Override
    public HashMap<String, Object> getByPostId(Long postId, Integer pageNumber) {
        Sort sort = Sort.by("gmtCreate").descending();
        PageRequest pageable = PageRequest.of(pageNumber-1, 15, sort);
        Page<BusOvoPostComment> all = repository.findByPostId(postId, pageable);
        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", all.getContent());
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }
}
