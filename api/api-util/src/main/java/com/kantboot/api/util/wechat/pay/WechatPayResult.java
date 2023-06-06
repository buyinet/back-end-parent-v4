package com.kantboot.api.util.wechat.pay;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 微信支付的结果
 * @author 方某方
 */
@Data
public class WechatPayResult {

    /**
     * 时间戳
     */
    @JSONField(name = "timeStamp")
    private String timeStamp;

    /**
     * 随机字符串
     */
    @JSONField(name = "nonceStr")
    private String nonceStr;

    /**
     * 预支付交易会话标识
     */
    @JSONField(name = "package")
    private String packageStr;

    /**
     * 签名方式
     */
    @JSONField(name = "signType")
    private String signType;

    /**
     * 签名
     */
    @JSONField(name = "paySign")
    private String paySign;

}
