package com.kantboot.api.module;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 推送模块
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class ApiPush {

    /**
     * 客户端id
     */
    private String cid;

    /**
     * 是否通知
     */
    private Boolean forceNotification;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 附加数据
     */
    private ApiPushPayload payload;


}
