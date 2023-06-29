package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoOMoney;
import com.kantboot.business.ovo.module.entity.BusOvoOMoneyOrder;
import com.kantboot.business.ovo.service.repository.BusOvoOMoneyOrderRepository;
import com.kantboot.business.ovo.service.repository.BusOvoOMoneyRepository;
import com.kantboot.business.ovo.service.service.IBusOvoOMoneyService;
import com.kantboot.pay.module.entity.PayOrder;
import com.kantboot.pay.service.service.IPayService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * O币的服务类实现类
 * @author 方某方
 */
@Service
public class BusOvoOMoneyServiceImpl implements IBusOvoOMoneyService {

    @Resource
    private BusOvoOMoneyRepository repository;

    @Resource
    private BusOvoOMoneyOrderRepository busOvoOMoneyOrderRepository;

    @Resource
    private IPayService payService;

    @Override
    public List<BusOvoOMoney> getOvoOMoneyList() {
        return repository.findByStatusCode("use");
    }

    @Override
    public Long buy(Long id) {
        BusOvoOMoney busOvoOMoney = repository.findById(id).get();
        PayOrder oMoneyRecharge = payService.generatePayOrder("oMoneyRecharge", busOvoOMoney.getAmount(),
                "OVO官方-O币充值(" + busOvoOMoney.getNum() + ")","CNY");

        BusOvoOMoneyOrder busOvoOMoneyOrder = new BusOvoOMoneyOrder();
        busOvoOMoneyOrder.setStatusCode("unpaid");
        busOvoOMoneyOrder.setOrderId(oMoneyRecharge.getId());
        busOvoOMoneyOrder.setOMoneyId(id);
        busOvoOMoneyOrderRepository.save(busOvoOMoneyOrder);

        return oMoneyRecharge.getId();
    }

}















