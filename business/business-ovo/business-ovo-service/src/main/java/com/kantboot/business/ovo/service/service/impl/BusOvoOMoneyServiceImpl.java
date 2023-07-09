package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.api.module.ApiPush;
import com.kantboot.api.module.ApiPushPayload;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.business.ovo.module.dto.BusOvoOMoneyReduceDTO;
import com.kantboot.business.ovo.module.entity.BusOvoOMoney;
import com.kantboot.business.ovo.module.entity.BusOvoOMoneyDetail;
import com.kantboot.business.ovo.module.entity.BusOvoOMoneyOrder;
import com.kantboot.business.ovo.service.repository.BusOvoOMoneyDetailRepository;
import com.kantboot.business.ovo.service.repository.BusOvoOMoneyOrderRepository;
import com.kantboot.business.ovo.service.repository.BusOvoOMoneyRepository;
import com.kantboot.business.ovo.service.service.IBusOvoOMoneyService;
import com.kantboot.business.ovo.service.service.IBusPushBindService;
import com.kantboot.pay.module.entity.PayOrder;
import com.kantboot.pay.service.service.IPayService;
import com.kantboot.system.service.ISysBalanceService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.result.PageResult;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * O币的服务类实现类
 * @author 方某方
 */
@Service
@Slf4j
public class BusOvoOMoneyServiceImpl implements IBusOvoOMoneyService {

    @Resource
    private BusOvoOMoneyRepository repository;

    @Resource
    private BusOvoOMoneyOrderRepository busOvoOMoneyOrderRepository;

    @Resource
    private BusOvoOMoneyDetailRepository busOvoOMoneyDetailRepository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IPayService payService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ISysExceptionService sysExceptionService;

    @Resource
    private ISysBalanceService sysBalanceService;

    @Resource
    private IBusPushBindService busPushBindService;

    @Resource
    private IApiPushService apiPushService;


    @Override
    public List<BusOvoOMoney> getOvoOMoneyList() {
        return repository.findByStatusCodeOrderByPriorityDesc("use");
    }

    @Override
    public Long buy(Long id) {
        Long idOfSelf = sysUserService.getIdOfSelf();
        if(redisUtil.lock("ovoOMoneyBuy:userId" + idOfSelf, 1, TimeUnit.SECONDS)){
            // 100毫秒内只能请求一次，防止重复请求，如果请求过于频繁，就会报错
            throw sysExceptionService.getException("requestTooMuch");
        }

        BusOvoOMoney busOvoOMoney = repository.findById(id).get();
        PayOrder oMoneyRecharge = payService.generatePayOrder("oMoneyRecharge", busOvoOMoney.getAmount(),
                "OVO官方-O币充值(" + busOvoOMoney.getNum() + ")","CNY");

        BusOvoOMoneyOrder busOvoOMoneyOrder = new BusOvoOMoneyOrder();
        busOvoOMoneyOrder.setStatusCode("unpaid");
        busOvoOMoneyOrder.setOrderId(oMoneyRecharge.getId());
        busOvoOMoneyOrder.setOMoneyId(id);
        busOvoOMoneyOrder.setUserId(idOfSelf);
        busOvoOMoneyOrderRepository.save(busOvoOMoneyOrder);

        return oMoneyRecharge.getId();
    }

    @Override
    public void rechargeCallback(String orderId) {
        Long aLong = Long.parseLong(orderId);
        log.info("aLong = " + aLong);
        BusOvoOMoneyOrder busOvoOMoneyOrder = busOvoOMoneyOrderRepository.findByOrderId(aLong);
        if(busOvoOMoneyOrder == null){
            throw sysExceptionService.getException("payOrderNotExist");
        }
        if("paid".equals(busOvoOMoneyOrder.getStatusCode())){
            // 代表已支付就不在进行处理
            throw sysExceptionService.getException("payOrderStatusExist");
        }

        // 查询明细中是否存在
        BusOvoOMoneyDetail byOrderId = busOvoOMoneyDetailRepository.findByOrderId(aLong);
        if(byOrderId != null){
            // 代表已支付就不在进行处理
            throw sysExceptionService.getException("payOrderStatusExist");
        }

        PayOrder byId = payService.getById(aLong);
        if(!"paid".equals(byId.getStatusCode())){
            throw sysExceptionService.getException("payOrderStatusError");
        }
        busOvoOMoneyOrder.setStatusCode("paid");
        busOvoOMoneyOrder.setGmtModified(new Date());
        busOvoOMoneyOrderRepository.save(busOvoOMoneyOrder);
        Long oMoneyId = busOvoOMoneyOrder.getOMoneyId();
        BusOvoOMoney busOvoOMoney = repository.findById(oMoneyId).get();
        sysBalanceService.addBalance("oMoney",
                busOvoOMoney.getNum()+0.00,
                busOvoOMoneyOrder.getUserId());

        BusOvoOMoneyDetail busOvoOMoneyDetail = new BusOvoOMoneyDetail();
        busOvoOMoneyDetail.setOrderId(aLong);
        busOvoOMoneyDetail.setUserId(busOvoOMoneyOrder.getUserId());
        busOvoOMoneyDetail.setOMoneyId(oMoneyId);
        // 设置类型为充值
        busOvoOMoneyDetail.setTypeCode("recharge");
        busOvoOMoneyDetail.setNum(busOvoOMoney.getNum());
        busOvoOMoneyDetail.setAmount(busOvoOMoney.getAmount());
        busOvoOMoneyDetailRepository.save(busOvoOMoneyDetail);

        ApiPush apiPush = new ApiPush();
        apiPush.setContent("已到账" + busOvoOMoney.getNum() + "个O币");
        apiPush.setTtl(-1);
        apiPush.setPayload(new ApiPushPayload().setData(sysBalanceService.getByUserId(busOvoOMoneyOrder.getUserId())).setEmit("balanceChange"));
        busPushBindService.pushByUserId(busOvoOMoneyOrder.getUserId(), apiPush);
    }

    @Override
    public void reduce(BusOvoOMoneyReduceDTO dto) {

        // 查看是否有足够的O币
        Map<String, Double> byUserId = sysBalanceService.getByUserId(dto.getUserId());
        Double oMoney = byUserId.get("oMoney");
        if(oMoney < dto.getOMoneyNum()){
            // 代表O币不足
            throw sysExceptionService.getException("oMoneyNotEnough");
        }
        sysBalanceService.addBalance("oMoney", -(dto.getOMoneyNum()+0.0), dto.getUserId());

        BusOvoOMoneyDetail busOvoOMoneyDetail = new BusOvoOMoneyDetail();
        busOvoOMoneyDetail.setTypeCode(dto.getTypeCode());
        busOvoOMoneyDetail.setNum(-dto.getOMoneyNum());
        busOvoOMoneyDetail.setUserId(dto.getUserId());
        busOvoOMoneyDetailRepository.save(busOvoOMoneyDetail);
    }
}















