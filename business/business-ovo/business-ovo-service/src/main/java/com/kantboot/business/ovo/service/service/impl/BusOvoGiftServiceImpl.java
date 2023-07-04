package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.dto.GiveGiftDto;
import com.kantboot.business.ovo.module.entity.BusOvoGift;
import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoUserGiftDetail;
import com.kantboot.business.ovo.service.repository.BusOvoGiftRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserCharmRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserGiftDetailRepository;
import com.kantboot.business.ovo.service.service.IBusOvoGiftService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 礼物表的业务实现类
 * @author 方某方
 */
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
    private ISysUserService userService;


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

        Long idOfSelf = userService.getIdOfSelf();

        // 礼物赠送给谁
        Long toUserId = null;



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

        // 添加到礼物明细表
        busOvoUserGiftDetailRepository.save(busOvoUserGiftDetail);
        // end:礼物明细

        // 未完成，魅力值用定时器计算，从魅力值明细中取上一个小时的魅力值




    }
}









