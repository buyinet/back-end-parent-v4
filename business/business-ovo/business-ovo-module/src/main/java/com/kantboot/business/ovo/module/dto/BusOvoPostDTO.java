package com.kantboot.business.ovo.module.dto;

import lombok.Data;

import java.util.List;

/**
 * 发布帖子的参数
 * 用于接收发布帖子的参数
 * @author 方某方
 */
@Data
public class BusOvoPostDTO {

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子图片文件id列表
     */
    private List<Long> fileIdListOfImage;

    /**
     * 位置查询id
     * none 代表不显示位置，"province" 代表显示省份，"city" 代表显示城市，"district" 代表显示区县，
     * 其它得到的地址ID
     */
    private String detailIdOfLocation;

    /**
     * 可见编码
     */
    private String visibleCode;



}
