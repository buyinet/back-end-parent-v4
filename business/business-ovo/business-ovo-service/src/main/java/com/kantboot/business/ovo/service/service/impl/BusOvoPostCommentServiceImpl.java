package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.module.entity.BusOvoPostComment;
import com.kantboot.business.ovo.module.entity.BusOvoPostCommentLike;
import com.kantboot.business.ovo.module.vo.BusOvoPostCommentVO;
import com.kantboot.business.ovo.service.repository.BusOvoPostCommentLikeRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostCommentRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostCommentService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 帖子评论的service实现类
 * @author 方某方
 */
@Service
public class BusOvoPostCommentServiceImpl implements IBusOvoPostCommentService {

    @Resource
    private BusOvoPostCommentRepository repository;

    @Resource
    private BusOvoPostCommentLikeRepository likeRepository;

    @Resource
    private ISysUserService userService;


    @Override
    public HashMap<String, Object> getByPostId(Long postId, Integer pageNumber) {
        Sort sort = Sort.by("gmtCreate").descending();
        PageRequest pageable = PageRequest.of(pageNumber-1, 15, sort);
        Page<BusOvoPostComment> all = repository.findByPostId(postId, pageable);
        List<BusOvoPostCommentVO> content = new ArrayList<>();
        Long idOfSelf = userService.getIdOfSelf();
        for (BusOvoPostComment comment : all) {
            Boolean aBoolean = likeRepository.existsBusOvoPostCommentLikeByUserIdAndCommentId(idOfSelf, comment.getId());
            BusOvoPostCommentVO vo = new BusOvoPostCommentVO();
            BeanUtils.copyProperties(comment, vo);
            vo.setLike(aBoolean);
            vo.setLikeCount(likeRepository.countByCommentId(comment.getId()));
            content.add(vo);
        }

        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", content);
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }

    @Override
    public BusOvoPostCommentVO publish(BusOvoPostComment comment) {
        BusOvoPostComment res=new BusOvoPostComment();
        res.setPostId(comment.getPostId());
        res.setContent(comment.getContent());
        res.setDelete(false);
        res.setAtCommentId(comment.getAtCommentId());
        Long idOfSelf = userService.getIdOfSelf();
        res.setUserId(idOfSelf);
        BusOvoPostComment save = repository.save(res);
        System.out.println(JSON.toJSONString(save));
        Boolean aBoolean = likeRepository.existsBusOvoPostCommentLikeByUserIdAndCommentId(idOfSelf, comment.getId());
        BusOvoPostCommentVO vo = new BusOvoPostCommentVO();
        BeanUtils.copyProperties(save, vo);
        vo.setLike(aBoolean);
        vo.setLikeCount(likeRepository.countByCommentId(comment.getId()));
        if(comment.getAtCommentId()!=null){
            Optional<BusOvoPostComment> byId = repository.findById(comment.getAtCommentId());
            vo.setAtComment(byId.orElse(null));
        }
        return vo;
    }

    @Override
    public BusOvoPostCommentVO like(Long commentId) {
        Long idOfSelf = userService.getIdOfSelf();
        Boolean aBoolean = likeRepository.existsBusOvoPostCommentLikeByUserIdAndCommentId(idOfSelf, commentId);
        BusOvoPostCommentVO vo = new BusOvoPostCommentVO();
        if (aBoolean) {
            likeRepository.deleteAll(likeRepository.findByUserIdAndCommentId(idOfSelf, commentId));
        } else {
            likeRepository.save(new BusOvoPostCommentLike().setCommentId(commentId).setUserId(idOfSelf));
        }
        BusOvoPostComment comment = repository.findById(commentId).orElse(null);
        BeanUtils.copyProperties(comment, vo);
        vo.setLike(!aBoolean);
        vo.setLikeCount(likeRepository.countByCommentId(comment.getId()));

        return vo;
    }
}
