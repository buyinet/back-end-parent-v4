package com.kantboot.pay.service.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.util.wechat.pay.PayNotifyParam;
import com.kantboot.api.util.wechat.pay.PemUtil;
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
import com.kantboot.util.common.result.RestResult;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Map;

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
    public PayOrder getById(Long id) {
        return payRepository.findById(id).get();
    }

    @Override
    public WechatPayResult wechatPay(String orderId, String sceneCode, String code) {

        Map<String, String> wechat = sysSettingService.getMap("wechat");


        PayOrder payOrder = payRepository.findById(Long.valueOf(orderId)).get();
        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setMchid(wechat.get("mchid"));
        wechatPayParam.setAppId(wechat.get("mpAppid"));

        wechatPayParam.setOutTradeNo(payOrder.getId().toString());

        String payNotifyUrl = wechat.get("payNotifyUrl");
        String s = "/";
        // 如果结尾是/，则加上订单号
        if (payNotifyUrl.endsWith(s)) {
            payNotifyUrl = payNotifyUrl + payOrder.getId();
        }else{
            payNotifyUrl = payNotifyUrl + s + payOrder.getId();
        }
        wechatPayParam.setNotifyUrl(payNotifyUrl);
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

        // 参数
        log.info("wechatPayParam:{}", JSON.toJSONString(wechatPayParam));


        return wechatPayParam.createResult(wechat.get("payCertPath"));
    }

    @SneakyThrows
    @Override
    public void wechatPayCallback(PayNotifyParam payNotify,String orderId) {
        String mchid = sysSettingService.getValue("wechat", "mchid");
        String payCertPath = sysSettingService.getValue("wechat", "payCertPath");
        String payCertSerialNo=sysSettingService.getValue("wechat", "payCertSerialNo");

        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        String nonceStr = IdUtil.simpleUUID().toUpperCase();
        String outTradeNo = orderId;

        // 生成签名
        String data="GET\n" +
                "/v3/pay/transactions/out-trade-no/"+outTradeNo+"?mchid="+mchid+"\n" +
                timeStamp+"\n" +
                nonceStr+"\n" +
                "\n";
        String base64Signature = PemUtil.getBase64Signature(data, payCertPath);

        String authorization="WECHATPAY2-SHA256-RSA2048 "
                +"mchid=\""+mchid+"\","
                +"nonce_str=\""+nonceStr+"\","
                +"signature=\""+base64Signature+"\","
                +"timestamp=\""+timeStamp+"\","
                +"serial_no=\""+payCertSerialNo+"\"";
        // 订单号查询地址
        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + outTradeNo+"?mchid="+mchid;
        // 发送请求,okhttp
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "SERVER")
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", authorization)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // start:解析返回结果
        // 金额
        String currency = JSON.parseObject(responseBody).getJSONObject("amount").getString("currency");
        // 支付金额
        Integer total = JSON.parseObject(responseBody).getJSONObject("amount").getInteger("total");
        // end:解析返回结果

        // 根据订单号查询订单
        PayOrder payOrder = payRepository.findById(Long.valueOf(orderId)).get();

        if(!payOrder.getCurrency().equals(currency) && payOrder.getAmount().equals(total/100.0)) {
            // 支付金额不相等，支付失败，暂时不做处理，以后再修改
            return;
        }

        String productCode = payOrder.getProductCode();

        // 再次查询订单，判断是否支付成功
        String payNotifyIn=sysSettingService.getValue("payNotify", productCode);

        payOrder.setStatusCode("paid");
        payRepository.save(payOrder);

        if(payNotifyIn==null){
            return;
        }

        // 回调
        String payNotifyUrl = payNotifyIn + "?orderId=" + orderId;
        log.info("payNotifyUrl:{}", payNotifyUrl);
        Request request1 = new Request.Builder()
                .url(payNotifyUrl)
                .addHeader("User-Agent", "SERVER")
                .addHeader("Accept", "*/*")
                .get()
                .build();
        Response response1 = client.newCall(request1).execute();
        // 判断是否接收到回调
        if(response1.code()!=200){
            // 回调异常
            log.info("回调异常:{}",response1.code());
            return;
        }


        String json = response1.body().string();
        RestResult restResult = JSON.parseObject(json, RestResult.class);
        if(restResult.isSuccess()){
            // 回调成功
            log.info("回调成功:{}",json);
        }else{
            log.info("订单异常:{}",json);
        }


        log.info("有微信支付回调:{},{}", orderId, payNotify);
        log.info("responseBody:{}", responseBody);
        // 根据订单号查询订单

    }

}














