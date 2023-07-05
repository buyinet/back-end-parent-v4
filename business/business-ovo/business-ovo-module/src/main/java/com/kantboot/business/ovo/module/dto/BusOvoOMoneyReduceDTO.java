package com.kantboot.business.ovo.module.dto;

import lombok.Data;

/**
 * 减少O币的DTO
 * @author 方某方
 */
@Data
public class BusOvoOMoneyReduceDTO {

    private String typeCode;

    private Long oMoneyNum;

    private Long userId;

    /**
     * 礼物明细id
     */
    private Long giftDetailId;

}
