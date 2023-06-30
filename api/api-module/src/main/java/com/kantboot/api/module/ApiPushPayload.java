package com.kantboot.api.module;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 推送模块
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class ApiPushPayload {

    private String emit;

    private Object data;

}
