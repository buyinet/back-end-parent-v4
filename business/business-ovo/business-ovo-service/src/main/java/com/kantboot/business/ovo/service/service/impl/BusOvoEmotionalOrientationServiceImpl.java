package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;
import com.kantboot.business.ovo.module.entity.BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation;
import com.kantboot.business.ovo.service.repository.BusOvoEmotionalOrientationRepository;
import com.kantboot.business.ovo.service.repository.BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientationRepository;
import com.kantboot.business.ovo.service.service.IBusOvoEmotionalOrientationService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 情感取向的服务
 * @author 方某方
 */
@Service
public class BusOvoEmotionalOrientationServiceImpl implements IBusOvoEmotionalOrientationService {

    @Resource
    private BusOvoEmotionalOrientationRepository repository;

    @Resource
    private BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientationRepository sexualOrientationLimitRepository;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<BusOvoEmotionalOrientation> getByGenderAndSexualOrientationCode(Integer gender, String sexualOrientationCode) {

        String redisKey="getByGenderAndSexualOrientationCode:"+gender+":"+sexualOrientationCode;

        String json = redisUtil.get(redisKey);
        if(json!=null){
            List<BusOvoEmotionalOrientation> busOvoEmotionalOrientations
                    = JSON.parseArray(json, BusOvoEmotionalOrientation.class);
            return busOvoEmotionalOrientations;
        }


        List<BusOvoEmotionalOrientation> result=new ArrayList<>();

        // 查询出没有性别限制和性取向限制的情感取向
        List<BusOvoEmotionalOrientation> byGenderLimitAndSexualOrientationLimit
                = repository.findByGenderLimitAndSexualOrientationLimit(-1, false);
        result.addAll(byGenderLimitAndSexualOrientationLimit);
        List<BusOvoEmotionalOrientation> byGenderLimitAndSexualOrientationLimit2
                = repository.findByGenderLimitAndSexualOrientationLimit(gender, false);
        result.addAll(byGenderLimitAndSexualOrientationLimit2);

        // 查询出没有性别限制但有性取向限制的情感取向
        List<BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation> bySexualOrientationCode
                = sexualOrientationLimitRepository.findBySexualOrientationCode(sexualOrientationCode);
        List<BusOvoEmotionalOrientation> nList=new ArrayList<>();
        for (BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation item : bySexualOrientationCode) {
            nList.add(repository.findByCode(item.getEmotionalOrientationCode()));
        }

        List<BusOvoEmotionalOrientation> nList2=new ArrayList<>();
        for (BusOvoEmotionalOrientation busOvoEmotionalOrientation : nList) {
            if(busOvoEmotionalOrientation.getGenderLimit()==-1){
                nList2.add(busOvoEmotionalOrientation);
            }else if(busOvoEmotionalOrientation.getGenderLimit()==-gender){
                nList2.add(busOvoEmotionalOrientation);
            }
        }
        result.addAll(nList2);

        redisUtil.set(redisKey,JSON.toJSONString(result));
        return result;
    }

    @Override
    public Map<String, String> getMap() {
        return repository.findAll().stream().collect(
                java.util.stream.Collectors.toMap(BusOvoEmotionalOrientation::getCode, BusOvoEmotionalOrientation::getName));
    }
}
