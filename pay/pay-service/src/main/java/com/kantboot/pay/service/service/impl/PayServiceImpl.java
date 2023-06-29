package com.kantboot.pay.service.service.impl;

import com.kantboot.api.util.wechat.pay.WechatPayParam;
import com.kantboot.api.util.wechat.pay.WechatPayResult;
import com.kantboot.pay.module.entity.PayOrder;
import com.kantboot.pay.service.repository.PayRepository;
import com.kantboot.pay.service.service.IPayService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysSettingService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.third.party.wechat.service.service.ITpWechatUserService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 支付服务实现类
 *
 * @author 方某方
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {

    @Resource
    private PayRepository payRepository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysSettingService sysSettingService;

    @Resource
    private ITpWechatUserService tpWechatUserService;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private ISysExceptionService sysExceptionService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public PayOrder generatePayOrder(String productCode, Double amount, String description, String currency) {
        PayOrder payOrder = new PayOrder().setAmount(amount).setProductCode(productCode).setDescription(description);
        // 设置成未支付状态
        payOrder.setStatusCode("unpaid");
        payOrder.setCurrency(currency);
        // 设置发起人
        payOrder.setUserId(sysUserService.getIdOfSelf());
        PayOrder save = payRepository.save(payOrder);
        return save;
    }

    @Override
    public WechatPayResult wechatPay(String orderId, String sceneCode, String code) {

        Map<String, String> wechat = sysSettingService.getMap("wechat");

        PayOrder payOrder = payRepository.findById(Long.valueOf(orderId)).get();
        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setMchid(wechat.get("mchid"));
        wechatPayParam.setAppId(wechat.get("mpAppid"));
        wechatPayParam.setNotifyUrl(wechat.get("payNotifyUrl"));
        wechatPayParam.setOutTradeNo(payOrder.getId().toString());

        WechatPayParam.Amount amount = new WechatPayParam.Amount();
        Double amount1 = payOrder.getAmount();
        Integer total = (int) (amount1 * 100);
        log.info("total:{}", total);
        // 金额单位是分,amount1是元
        amount.setTotal((int) (amount1 * 100));
        amount.setCurrency(payOrder.getCurrency());
        wechatPayParam.setAmount(amount);
        // 设置支付人
        WechatPayParam.Payer payer = new WechatPayParam.Payer();
        payer.setOpenid(tpWechatUserService.getOpenIdByCode(code));
        wechatPayParam.setPayer(payer);
        // 设置商品描述
        wechatPayParam.setDescription(payOrder.getDescription());

        return wechatPayParam.createResult(wechat.get("payCertPath"));
    }
}














