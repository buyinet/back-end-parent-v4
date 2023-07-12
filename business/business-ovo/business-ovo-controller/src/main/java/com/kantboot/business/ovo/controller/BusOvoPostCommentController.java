package com.kantboot.business.ovo.controller;


import com.kantboot.business.ovo.module.entity.BusOvoPostComment;
import com.kantboot.business.ovo.service.service.IBusOvoPostCommentService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子评论的控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/postComment")
public class BusOvoPostCommentController {

    @Resource
    private IBusOvoPostCommentService service;
    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取帖子的评论
     * @param postId 帖子id
     * @param pageNumber 页码
     * @return 帖子的评论
     */
    @RequestMapping("/getByPostId")
    public RestResult getByPostId(Long postId, Integer pageNumber){
        return stateSuccessService.success(
                service.getByPostId(postId, pageNumber),
                "getSuccess"
        );
    }

    @RequestMapping("/like")
    public RestResult like(Long commentId){
        return stateSuccessService.success(
                service.like(commentId),
                "getSuccess"
        );
    }

    /**
     * 发布评论
     * @param comment 评论
     * @return 评论
     */
    @RequestMapping("/publish")
    public RestResult publish(@RequestBody BusOvoPostComment comment){
        return stateSuccessService.success(
                service.publish(comment),
                "success"
        );
    }




}
