package com.kantboot.business.ovo.module.dto;

import lombok.Data;

/**
 * 赠送礼物的数据传输对象
 * 用于接收前端传来的赠送礼物的数据
 * @author 方某方
 */
@Data
public class GiveGiftDto {

    /**
     * 礼物编码
     */
    private String giftCode;

    /**
     * 赠送给谁
     */
    private Long toUserId;

    /**
     * 赠送给哪个帖子
     */
    private Long toPostId;

    /**
     * 赠送数量
     */
    private Long number;

}
