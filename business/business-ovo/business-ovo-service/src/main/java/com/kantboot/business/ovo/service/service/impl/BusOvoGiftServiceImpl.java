package com.kantboot.business.ovo.service.service.impl;

import cn.hutool.core.lang.Assert;
import com.kantboot.business.ovo.module.dto.BusOvoOMoneyReduceDTO;
import com.kantboot.business.ovo.module.dto.GiveGiftDto;
import com.kantboot.business.ovo.module.entity.BusOvoGift;
import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoUserCharm;
import com.kantboot.business.ovo.module.entity.BusOvoUserGiftDetail;
import com.kantboot.business.ovo.service.repository.BusOvoGiftRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserCharmRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserGiftDetailRepository;
import com.kantboot.business.ovo.service.service.IBusOvoGiftService;
import com.kantboot.business.ovo.service.service.IBusOvoOMoneyService;
import com.kantboot.system.service.ISysBalanceService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 礼物表的业务实现类
 * @author 方某方
 */
@Log4j2
@Service
public class BusOvoGiftServiceImpl implements IBusOvoGiftService {

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private BusOvoGiftRepository busOvoGiftRepository;

    @Resource
    private BusOvoPostRepository busOvoPostRepository;

    @Resource
    private BusOvoUserGiftDetailRepository busOvoUserGiftDetailRepository;

    @Resource
    private BusOvoUserCharmRepository busOvoUserCharmRepository;

    @Resource
    private IBusOvoOMoneyService busOvoOMoneyService;

    @Resource
    private IBusOvoGiftService busOvoGiftService;

    @Resource
    private ISysUserService userService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ISysBalanceService sysBalanceService;


    @Override
    public BusOvoGift getByCode(String code) {
        BusOvoGift byCode = busOvoGiftRepository.findByCode(code);
        if (byCode == null) {
            // 如果礼物不存在，抛出异常
            throw exceptionService.getException("giftNotExist");
        }
        return byCode;
    }

    @Override
    public List<BusOvoGift> getAll() {
        return busOvoGiftRepository.findAllByOrderByPriorityDesc();
    }

    @Override
    public void give(GiveGiftDto giveGiftDto) {

        Long idOfSelf = userService.getIdOfSelf();
        Boolean giveGift = redisUtil.lock("giveGift:fromUserId:"+idOfSelf, 100, TimeUnit.MILLISECONDS);

        if (giveGift) {
            log.info("操作过于频繁:{}", giveGiftDto);
            // 操作太频繁，抛出异常
            throw exceptionService.getException("requestTooMuch");
        }

        if (giveGiftDto.getGiftCode() == null) {
            // 如果礼物编码为空，抛出异常
            throw exceptionService.getException("giftCodeIsNull");
        }
        if (giveGiftDto.getNumber() == null) {
            // 如果礼物数量为空，抛出异常
            throw exceptionService.getException("giftNumberIsNull");
        }
        BusOvoGift byCode = getByCode(giveGiftDto.getGiftCode());

        if (byCode == null) {
            // 如果礼物不可用，抛出异常
            throw exceptionService.getException("giftIsNotEnable");
        }

        // 礼物赠送给谁
        Long toUserId = giveGiftDto.getToUserId();

        // 计算需要的O币数量
        Long oMoneyNum = byCode.getCostOfOMoney() * giveGiftDto.getNumber();


        // start:礼物明细
        BusOvoUserGiftDetail busOvoUserGiftDetail = new BusOvoUserGiftDetail();
        busOvoUserGiftDetail.setGiftCode(giveGiftDto.getGiftCode());
        busOvoUserGiftDetail.setFromUserId(idOfSelf);
        busOvoUserGiftDetail.setGiftNum(giveGiftDto.getNumber());


        if(giveGiftDto.getToUserId()!=null){
            // 如果赠送给用户，设置赠送给用户的id
            busOvoUserGiftDetail.setToUserId(giveGiftDto.getToUserId());
            busOvoUserGiftDetail.setTypeCode("toUser");

            toUserId=giveGiftDto.getToUserId();

        }

        if(giveGiftDto.getToPostId()!=null){
            // 如果赠送给帖子，设置赠送给帖子的id
            busOvoUserGiftDetail.setToPostId(giveGiftDto.getToPostId());

            BusOvoPost busOvoPost =
            busOvoPostRepository.findById(giveGiftDto.getToPostId())
                    .orElseThrow(()->exceptionService.getException("postNotExist"));

            busOvoUserGiftDetail.setToUserId(busOvoPost.getUserId());
            busOvoUserGiftDetail.setTypeCode("toPost");

            toUserId=busOvoPost.getUserId();
        }

        // 扣除对应的O币
        BusOvoOMoneyReduceDTO busOvoOMoneyReduceDTO = new BusOvoOMoneyReduceDTO();
        busOvoOMoneyReduceDTO.setUserId(idOfSelf);
        busOvoOMoneyReduceDTO.setOMoneyNum(oMoneyNum);
        busOvoOMoneyReduceDTO.setTypeCode("giveGift");
        busOvoOMoneyService.reduce(busOvoOMoneyReduceDTO);


        // 添加到礼物明细表
        busOvoUserGiftDetailRepository.save(busOvoUserGiftDetail);
        // end:礼物明细

        // 获取可以增加的魅力值
        Long charmValue = byCode.getCharmValue() * giveGiftDto.getNumber();

        // 增加魅力值
        sysBalanceService.addBalance("charmValue",charmValue+0.0,toUserId);

        sysBalanceService.addBalance("charmPoints",(byCode.getCharmValue()+0.00) * giveGiftDto.getNumber(),toUserId);


    }
}









