package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.module.ApiPush;
import com.kantboot.api.module.ApiPushPayload;
import com.kantboot.api.service.ITencentApiLocationService;
import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.*;
import com.kantboot.business.ovo.service.mapper.BusOvoPostMapper;
import com.kantboot.business.ovo.service.repository.BusOvoPostLikeRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.business.ovo.service.service.IBusOvoUserService;
import com.kantboot.business.ovo.service.service.IBusPushBindService;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.module.entity.SysUserHasHide;
import com.kantboot.system.module.entity.SysUserOnline;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

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


    @Resource
    private IBusOvoUserService userBindService;

    @Resource
    private ITencentApiLocationService locationService;

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private BusOvoPostLikeRepository postLikeRepository;


    private Long getUserId() {
        try {
            return sysUserService.getSelf().getId();
        } catch (BaseException e) {
            return null;
        }

    }

    @Override
    public BusOvoPost publish(BusOvoPostDTO dto) {

        BusOvoPost post = new BusOvoPost();
        // 帖子内容
        post.setContent(dto.getContent());
        // 可见编码
        post.setVisibleCode(dto.getVisibleCode());

        // 审核状态编码,默认为wait
        post.setAuditStatusCode("wait");

        // 帖子图片
        List<BusOvoPostImage> postImageList = new ArrayList<>();
        dto.getFileIdListOfImage().forEach(fileId -> {
            postImageList.add(new BusOvoPostImage().setFileId(fileId));
        });
        post.setImageList(postImageList);
        BusOvoUser self = userBindService.getSelf();


        String detailIdOfLocation = dto.getDetailIdOfLocation();
        if(detailIdOfLocation!=null&&!detailIdOfLocation.equals("")
                &&!detailIdOfLocation.equals("province")
                &&!detailIdOfLocation.equals("city")
                &&!detailIdOfLocation.equals("district")
        ){
            // 使用位置查询id获取位置信息
            JSONObject locationInfoById = null;
            locationInfoById = locationService.getLocationInfoById(dto.getDetailIdOfLocation());

            // 之后再加：异常处理
            // ...

            // 位置标题
            String addressTitle = locationInfoById.getString("title");
            // 位置地址
            String address = locationInfoById.getString("address");
            // 位置经纬度
            JSONObject location = locationInfoById.getJSONObject("location");
            // 位置纬度
            Double latitude = location.getDouble("lat");
            // 位置经度
            Double longitude = location.getDouble("lng");
            // 位置信息
            JSONObject adInfo = locationInfoById.getJSONObject("ad_info");
            // 位置省份
            String province = adInfo.getString("province");
            // 位置城市
            String city = adInfo.getString("city");
            // 位置区县
            String district = adInfo.getString("district");
            // 位置编码
            Integer adCode = adInfo.getInteger("adcode");

            post.setAddressTitleOfSelect(addressTitle);
            post.setAddressOfSelect(address);
            post.setLatitudeOfSelect(latitude);
            post.setLongitudeOfSelect(longitude);
            post.setProvinceOfSelect(province);
            post.setCityOfSelect(city);
            post.setDistrictOfSelect(district);
            post.setAdCodeOfSelect(adCode+"");
            // 位置级别: specific 代表具体位置，province 代表省份，city 代表城市，district 代表区县
            post.setAddressLevelOfSelect("specific");
        }else{
            if(detailIdOfLocation.equals("province")) {
                post.setAddressTitleOfSelect(self.getLocation().getProvince());
                post.setAddressLevelOfSelect("province");
            }else if(detailIdOfLocation.equals("city")){
                post.setAddressTitleOfSelect(self.getLocation().getCity());
                post.setAddressLevelOfSelect("city");
            }else if(detailIdOfLocation.equals("district")){
                post.setAddressTitleOfSelect(self.getLocation().getDistrict());
                post.setAddressLevelOfSelect("district");
            }else if(detailIdOfLocation==null||detailIdOfLocation==""){
                post.setAddressLevelOfSelect("none");
            }
            post.setLatitudeOfSelect(self.getLocation().getLatitude());
            post.setLongitudeOfSelect(self.getLocation().getLongitude());
            post.setProvinceOfSelect(self.getLocation().getProvince());
            post.setCityOfSelect(self.getLocation().getCity());
            post.setDistrictOfSelect(self.getLocation().getDistrict());
            post.setAdCodeOfSelect(self.getLocation().getAdCode());
        }

        post.setUserId(self.getUserId());
        if(self.getLocation()!=null){
            post.setLatitude(self.getLocation().getLatitude());
            post.setLongitude(self.getLocation().getLongitude());
            post.setProvince(self.getLocation().getProvince());
            post.setCity(self.getLocation().getCity());
            post.setDistrict(self.getLocation().getDistrict());
            post.setAdCode(self.getLocation().getAdCode());
        }
        return repository.save(post);
    }


    @Resource
    private IBusPushBindService pushBindService;

    @Override
    public BusOvoPost audit(BusOvoPost busOvoPost) {
        BusOvoPost post = repository.findById(busOvoPost.getId()).get();

        post.setAuditStatusCode(busOvoPost.getAuditStatusCode());
        post.setGmtAudit(new Date());

        if (busOvoPost.getAuditStatusCode().equals("reject")) {
            post.setAuditRejectReason(busOvoPost.getAuditRejectReason());
        }
        BusOvoPost save = repository.save(post);


        ApiPush apiPush = new ApiPush();
        String title = "";
        String content = "";
        if (busOvoPost.getAuditStatusCode().equals("pass")) {
            title = "OVO提示: 帖子审核通过";
            content = "您的帖子已经审核通过";
        } else if (busOvoPost.getAuditStatusCode().equals("reject")) {
            title = "OVO提示: 帖子审核不通过";
            content = "您的帖子审核不通过,原因是: " + busOvoPost.getAuditRejectReason();
        }

        apiPush.setTitle(title);
        apiPush.setContent(content);
        apiPush.setTtl(3600);

        apiPush.setForceNotification(false);
        ApiPushPayload apiPushPayload = new ApiPushPayload();

        apiPushPayload.setEmit("postAudit");
        apiPushPayload.setData(save);

        apiPush.setPayload(apiPushPayload);
        pushBindService.pushByUserIdWithEmail(save.getUserId(), apiPush);

        return save;
    }

    private BusOvoPost mapToPost(Map<String,Object> map){
        BusOvoPost busOvoPost = JSON.parseObject(JSON.toJSONString(map), BusOvoPost.class);
        if(map.get("address_level_of_select")!=null&&!map.get("address_level_of_select").equals("")){
            busOvoPost.setAddressLevelOfSelect(map.get("address_level_of_select")+"");
        }
        if(map.get("address_title_of_select")!=null&&!map.get("address_title_of_select").equals("")) {
            busOvoPost.setAddressTitleOfSelect(map.get("address_title_of_select") + "");
        }
        //audit_reject_reason
        if(map.get("audit_reject_reason")!=null&&!map.get("audit_reject_reason").equals("")) {
            busOvoPost.setAuditRejectReason(map.get("audit_reject_reason") + "");
        }
        if(map.get("visible_code")!=null&&!map.get("visible_code").equals("")) {
            busOvoPost.setVisibleCode(map.get("visible_code")+"");
        }


        BusOvoUser busOvoUser = JSON.parseObject(JSON.toJSONString(map), BusOvoUser.class);
        SysUserHasHide sysUserHasHide = JSON.parseObject(JSON.toJSONString(map), SysUserHasHide.class);
        if (map.get("file_id_of_avatar")!=null&&!map.get("file_id_of_avatar").equals("")) {
            sysUserHasHide.setFileIdOfAvatar(Long.parseLong(map.get("file_id_of_avatar") + ""));
        }

        SysUserOnline sysUserOnline = JSON.parseObject(JSON.toJSONString(map), SysUserOnline.class);

        Object imageFileIdArrStr = null;
        try{
            imageFileIdArrStr = map.get("imageFileIdArrStr");
        }catch (NullPointerException e){
            imageFileIdArrStr=null;
        }

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

        BusOvoUserBindLocation busOvoUserBindLocation = new BusOvoUserBindLocation();
        // 获取user经度
        Object userLongitude = map.get("userLongitude");
        if (userLongitude != null) {
            busOvoUserBindLocation.setLongitude(Double.parseDouble(userLongitude.toString()));
        }


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
                        busOvoUser
                                .setUserId(busOvoPost.getUserId())
                                .setUser(
                                        sysUserHasHide.setUserOnline(sysUserOnline)
                                ).setLocation(busOvoUserBindLocation)
                                .setEmotionalOrientationList(busOvoEmotionalOrientations)
                );
        return busOvoPost;
    }

    private List<BusOvoPost> mapListToPostList(List<Map<String,Object>> mapList){
        List<BusOvoPost> busOvoPosts = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            busOvoPosts.add(mapToPost(map));
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
    public Object getFollowLess(Long id) {
        return mapListToPostList(mapper.getFollowLess(id,sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getFollowGreater(Long id) {
        return mapListToPostList(mapper.getFollowGreater(id,sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getLessOfRecommend(Long id) {
        return mapListToPostList(mapper.getLessOfRecommend(id,sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getFollowDefault() {
        return mapListToPostList(mapper.getFollowDefault(sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getHot() {
        return mapListToPostList(mapper.getHot(sysUserService.getIdOfSelf()));
    }

    @Override
    public Object getSelf(Long id) {
        return mapListToPostList(mapper.getSelf(id,sysUserService.getIdOfSelf()));
    }

    @Override
    public BusOvoPost getById(Long id) {
        Long idOfSelf=null;
        try {
             idOfSelf= sysUserService.getIdOfSelf();
        } catch (BaseException e){

        }
        if(idOfSelf==null){
            idOfSelf=0L;
        }

        BusOvoPost busOvoPost = mapToPost(mapper.getById(id,idOfSelf));
        boolean pass = busOvoPost.getAuditStatusCode().equals("pass");

        if(!pass && !idOfSelf.equals(busOvoPost.getUserId())){
            // 非法访问，驼峰式
            throw exceptionService.getException("illegalAccess");
        }

        return busOvoPost;
    }



    @Override
    public BusOvoPost like(Long id) {
        Long idOfSelf=sysUserService.getIdOfSelf();

        Boolean aBoolean = postLikeRepository.existsByUserIdAndPostId(idOfSelf, id);
        if (aBoolean) {
            return null;
        }
        postLikeRepository.save(new BusOvoPostLike().setPostId(id).setUserId(idOfSelf));

        return null;
    }


    @Override
    public BusOvoPost unLike(Long id) {
        Long idOfSelf=sysUserService.getIdOfSelf();

        Boolean aBoolean = postLikeRepository.existsByUserIdAndPostId(idOfSelf, id);
        if(aBoolean){
            List<BusOvoPostLike> allByUserIdAndPostId = postLikeRepository.findAllByUserIdAndPostId(idOfSelf, id);
            postLikeRepository.deleteAll(allByUserIdAndPostId);
        }

        return null;
    }

    @Override
    public Object getNear(Long pageNumber, Double range) {
        BusOvoUser self = userBindService.getSelf();
        // 获取纬度
        Double latitude = self.getLocation().getLatitude();
        // 获取经度
        Double longitude = self.getLocation().getLongitude();
        // 经纬度保留五位小数
        latitude = Double.valueOf(String.format("%.5f", latitude));
        longitude = Double.valueOf(String.format("%.5f", longitude));

        List<Map<String, Object>> all =
                mapper.findAllWithDistance(pageNumber - 1, latitude, longitude, range,self.getUserId());

        return mapListToPostList(all);
    }
}

