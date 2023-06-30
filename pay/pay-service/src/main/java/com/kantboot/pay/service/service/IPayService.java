package com.kantboot.pay.service.service;

import com.kantboot.api.util.wechat.pay.PayNotifyParam;
import com.kantboot.api.util.wechat.pay.WechatPayResult;
import com.kantboot.pay.module.entity.PayOrder;

/**
 * 关于支付的服务
 * @author 方某方
 */
public interface IPayService {

    /**
     * 生成支付订单
     * @param productCode 商品编码
     * @param amount 金额
     * @param description 描述
     * @param currency 币种
     * @return 支付订单
     */
    PayOrder generatePayOrder(String productCode,Double amount,String description,String currency);

    /**
     * 根据id查询支付订单
     * @param id id
     * @return 支付订单
     */
    PayOrder getById(Long id);

    /**
     * 生成微信支付的参数
     * @param orderId 订单号
     * @param sceneCode 场景码
     * @param code 微信小程序的code
     * @return 微信支付的参数
     */
    WechatPayResult wechatPay(String orderId, String sceneCode,String code);

    /**
     * 微信支付回调
     *
     */
    void wechatPayCallback(PayNotifyParam payNotify,String orderId);

}
