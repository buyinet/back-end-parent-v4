package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.module.entity.*;
import com.kantboot.business.ovo.service.mapper.BusOvoPostMapper;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.system.module.entity.SysUserHasHide;
import com.kantboot.system.module.entity.SysUserOnline;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 *
 * @author 方某方
 */
@Service
public class BusOvoPostServiceImpl implements IBusOvoPostService {

    @Resource
    private BusOvoPostRepository repository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private BusOvoPostMapper mapper;


    private Long getUserId() {
        try {
            return sysUserService.getSelf().getId();
        } catch (BaseException e) {
            return null;
        }

    }


    @Override
    public BusOvoPost audit(BusOvoPost busOvoPost) {
        BusOvoPost post = repository.findById(busOvoPost.getId()).get();
        post.setAuditStatusCode(busOvoPost.getAuditStatusCode());
        post.setGmtAudit(new Date());

        if (busOvoPost.getAuditStatusCode().equals("reject")) {
            post.setAuditRejectReason(busOvoPost.getAuditRejectReason());
        }

        BusOvoPost save = repository.save(post);
        return save;
    }

    private List<BusOvoPost> mapListToPostList(List<Map<String,Object>> mapList){
        List<BusOvoPost> busOvoPosts = new ArrayList<>();
        for (Map<String, Object> map : mapList) {

            BusOvoPost busOvoPost = JSON.parseObject(JSON.toJSONString(map), BusOvoPost.class);
            BusOvoUser busOvoUser = JSON.parseObject(JSON.toJSONString(map), BusOvoUser.class);
            SysUserHasHide sysUserHasHide = JSON.parseObject(JSON.toJSONString(map), SysUserHasHide.class);
            SysUserOnline sysUserOnline = JSON.parseObject(JSON.toJSONString(map), SysUserOnline.class);

            Object imageFileIdArrStr = map.get("imageFileIdArrStr");
            if(imageFileIdArrStr==null){
                imageFileIdArrStr="[]";
            }

            List<Long> stringList = JSON.parseArray(imageFileIdArrStr+"", Long.class);
            List<BusOvoPostImage> busOvoPostImages = new ArrayList<>();
            if (stringList == null) {
                stringList = new ArrayList<>();
            }

            for (Long fileId : stringList) {
                BusOvoPostImage busOvoPostImage = new BusOvoPostImage();
                busOvoPostImage.setFileId(fileId);
                busOvoPostImages.add(busOvoPostImage);
            }
            busOvoPost.setImageList(busOvoPostImages);

            // 获取user经度
            Object userLongitude = map.get("userLongitude");
            if (userLongitude != null) {
                busOvoPost.setLongitude(Double.parseDouble(userLongitude.toString()));
            }

            BusOvoUserBindLocation busOvoUserBindLocation = new BusOvoUserBindLocation();

            // 获取user纬度
            Object userLatitude = map.get("userLatitude");
            if (userLatitude != null) {
                busOvoUserBindLocation.setLatitude(Double.parseDouble(userLatitude.toString()));
            }

            // 获取user省份
            Object userProvince = map.get("userProvince");
            if (userProvince != null) {
                busOvoUserBindLocation.setProvince(userProvince.toString());
            }

            // 获取user城市
            Object userCity = map.get("userCity");
            if (userCity != null) {
                busOvoUserBindLocation.setCity(userCity.toString());
            }

            // 获取user区县
            Object userDistrict = map.get("userDistrict");
            if (userDistrict != null) {
                busOvoUserBindLocation.setDistrict(userDistrict.toString());
            }

            Object emotionalOrientationCodeArrStr = map.get("emotionalOrientationCodeArrStr");
            if(emotionalOrientationCodeArrStr==null){
                emotionalOrientationCodeArrStr="[]";
            }


            List<String> emotionalOrientationCodeArr = JSON.parseArray(emotionalOrientationCodeArrStr+"", String.class);

            List<BusOvoEmotionalOrientation> busOvoEmotionalOrientations = new ArrayList<>();
            if (emotionalOrientationCodeArr != null) {
                emotionalOrientationCodeArr.forEach(emotionalOrientationCode -> {
                    BusOvoEmotionalOrientation busOvoEmotionalOrientation = new BusOvoEmotionalOrientation();
                    busOvoEmotionalOrientation.setCode(emotionalOrientationCode);
                    busOvoEmotionalOrientations.add(busOvoEmotionalOrientation);
                });

            }


            busOvoPost
                    .setAuditStatusCode(map.get("audit_status_code")+"")
                    .setOvoUser(
                            busOvoUser.setUser(
                                            sysUserHasHide.setUserOnline(sysUserOnline)
                                    ).setLocation(busOvoUserBindLocation)
                                    .setEmotionalOrientationList(busOvoEmotionalOrientations)
                    );

            busOvoPosts.add(busOvoPost);


        }
        return busOvoPosts;
    }

    @Override
    public Object getDefaultRecommend() {
        List<Map<String, Object>> defaultRecommend = mapper.getDefaultRecommend(sysUserService.getIdOfSelf());
        return mapListToPostList(defaultRecommend);
    }

    @Override
    public Object getGreaterOfRecommend(Long id) {
        return mapListToPostList(mapper.getGreaterOfRecommend(id,sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getLessOfRecommend(Long id) {
        return mapListToPostList(mapper.getLessOfRecommend(id,sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getHot() {
        return mapListToPostList(mapper.getHot(sysUserService.getIdOfSelf()));
    }
}

