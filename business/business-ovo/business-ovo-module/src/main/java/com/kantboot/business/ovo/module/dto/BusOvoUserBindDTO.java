package com.kantboot.business.ovo.module.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 绑定的用户
 * 用于接收绑定用户的参数
 * @author 方某方
 */
@Data
public class BusOvoUserBindDTO {
    /**
     * 性取向编码
     */
    private String sexualOrientationCode;

    /**
     * 性虐属性编码
     */
    private String sadomasochismAttrCode;

    /**
     * 情感取向编码列表
     */
    private List<String> emotionalOrientationCodeList;

    /**
     * 自我介绍
     */
    private String introduction;

    /**
     * 头像文件id
     */
    private Long fileIdOfAvatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 生日
     */
    private Date birthday;
}
