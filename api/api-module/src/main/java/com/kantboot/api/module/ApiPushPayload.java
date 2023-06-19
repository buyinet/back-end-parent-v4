package com.kantboot.api.module;

import lombok.Data;

/**
 * 推送模块
 * @author 方某方
 */
@Data
public class ApiPushPayload {

    private String emit;

    private Object data;

}
