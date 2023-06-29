package com.kantboot.business.ovo.service.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.module.ApiPush;
import com.kantboot.api.module.ApiPushPayload;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.api.service.ITencentApiLocationService;
import com.kantboot.api.util.location.LocationEntity;
import com.kantboot.api.util.location.LocationUtil;
import com.kantboot.business.ovo.module.dto.BusOvoUserBindDTO;
import com.kantboot.business.ovo.module.entity.*;
import com.kantboot.business.ovo.module.vo.BusOvoUserBindVO;
import com.kantboot.business.ovo.service.repository.*;
import com.kantboot.business.ovo.service.service.IBusOvoUserService;
import com.kantboot.business.ovo.service.service.IBusPushBindService;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 绑定的用户的服务实现类
 *
 * @author 方某方
 */
@Service
@Slf4j
public class BusOvoUserServiceImpl implements IBusOvoUserService {
    private final BusOvoUserMutualRepository busOvoUserMutualRepository;

    public BusOvoUserServiceImpl(BusOvoUserMutualRepository busOvoUserMutualRepository) {
        this.busOvoUserMutualRepository = busOvoUserMutualRepository;
    }

    @Resource
    private BusOvoUserRepository repository;


    @Resource
    private ISysUserService sysUserService;


    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private ITencentApiLocationService tencentApiLocationService;

    @Resource
    private BusOvoUserBindLocationRepository busOvoUserBindLocationRepository;

    @Resource
    private RelBusOvoUserBindAndBusOvoEmotionalOrientationRepository relBusOvoUserBindAndBusOvoEmotionalOrientationRepository;

    @Resource
    private BusOvoUserFollowRepository busOvoUserFollowRepository;

    @Resource
    private BusOvoPostRepository busOvoPostRepository;

    @Resource
    private IBusPushBindService pushService;

    @Resource
    private IApiPushService apiPushService;

    @Resource
    RedisUtil redisUtil;

    @Resource
    private ISysExceptionService sysExceptionService;



    @Override
    public BusOvoUser getByUserId(Long userId) {
        BusOvoUser busOvoUserBind = repository.findById(userId).orElse(null);
        busOvoUserBind.setUser(sysUserService.getById(userId));
        return busOvoUserBind;
    }

    @Override
    public BusOvoUser bind(BusOvoUserBindDTO dto) {

        // 获取当前用户
        SysUser sysUser = sysUserService.getWithoutHideSensitiveInfo();

        // 设置用户信息
        sysUser.setFileIdOfAvatar(dto.getFileIdOfAvatar())
                .setNickname(dto.getNickname())
                .setGender(dto.getGender())
                .setBirthday(dto.getBirthday());

        SysUser save = sysUserService.save(sysUser);


        // 获取当前用户的id
        Long userId = sysUser.getId();
        // 根据用户id查询用户绑定信息
        BusOvoUser busOvoUserBind = repository.findByUserId(userId);
        // 如果用户绑定信息为空
        if (busOvoUserBind == null) {
            // 创建用户绑定信息
            busOvoUserBind = new BusOvoUser();
            // 设置用户id
            busOvoUserBind.setUserId(userId);
            busOvoUserBind = repository.save(busOvoUserBind);
        }
        busOvoUserBind
                .setIntroduction(dto.getIntroduction())
                .setSexualOrientationCode(dto.getSexualOrientationCode())
                .setSadomasochismAttrCode(dto.getSadomasochismAttrCode());

        List<String> emotionalOrientationCodeList = dto.getEmotionalOrientationCodeList();

        relBusOvoUserBindAndBusOvoEmotionalOrientationRepository.saveAll(emotionalOrientationCodeList.stream().map(code -> {
            return new RelBusOvoUserAndBusOvoEmotionalOrientation()
                    .setUserId(userId)
                    .setEmotionalOrientationCode(code);
        }).collect(Collectors.toList()));

        BusOvoUser result = repository.save(busOvoUserBind);
        result.setUser(save);


        return result;
    }


    @Override
    public BusOvoUserBindVO getSelf() {
        SysUser sysUser = sysUserService.getWithoutHideSensitiveInfo();
        Long userId = sysUser.getId();
        BusOvoUser busOvoUserBind = repository.findByUserId(userId);
        if (busOvoUserBind == null) {
            busOvoUserBind=repository.save(new BusOvoUser().setUserId(userId));
        }

        BusOvoUserBindVO busOvoUserBindVO = new BusOvoUserBindVO();
        try {
            if (busOvoUserBind.getLocation() == null) {
                BusOvoUserBindLocation busOvoUserBindLocation = new BusOvoUserBindLocation();
                JSONObject locationInfoByIp = tencentApiLocationService.getLocationInfoByIp(httpRequestHeaderUtil.getIp());
                log.info("locationInfoByIp:{}", locationInfoByIp);
                JSONObject location = locationInfoByIp.getJSONObject("location");
                if (location != null) {
                    busOvoUserBindLocation.setLongitude(location.getDouble("lng"));
                    busOvoUserBindLocation.setLatitude(location.getDouble("lat"));
                }

                JSONObject adInfo = locationInfoByIp.getJSONObject("ad_info");
                String country = adInfo.getString("nation");
                if (country != null) {
                    busOvoUserBindLocation.setCountry(country);
                }
                String province = adInfo.getString("province");
                if (province != null) {
                    busOvoUserBindLocation.setProvince(province);
                }
                String city = adInfo.getString("city");
                if (city != null) {
                    busOvoUserBindLocation.setCity(city);
                }
                String district = adInfo.getString("district");
                if (district != null) {
                    busOvoUserBindLocation.setDistrict(district);
                }
                String areaCode = adInfo.getString("adcode");
                if (areaCode != null) {
                    busOvoUserBindLocation.setAdCode(areaCode);
                }
                String countryCode = adInfo.getString("nation_code");
                if (countryCode != null) {
                    busOvoUserBindLocation.setCountryCode(countryCode);
                }

                busOvoUserBindLocation.setUserId(userId);
                busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(busOvoUserBind, busOvoUserBindVO);
        // 获取用户的粉丝数
        Long fansCount = busOvoUserFollowRepository.countByFollowUserId(userId);
        // 获取用户的关注数
        Long followCount = busOvoUserFollowRepository.countByUserId(userId);
        busOvoUserBindVO.setFollowersCount(fansCount);
        busOvoUserBindVO.setFollowingCount(followCount);

        // 获取帖子数
        Long postCount = busOvoPostRepository.countByUserIdAndAuditStatusCode(userId, "pass");
        busOvoUserBindVO.setPostCount(postCount);
        busOvoUserBindVO.setUser(sysUser);

        return busOvoUserBindVO;
    }

    @Override
    public HashMap<String, Object> getRecommendList(Integer pageNumber, String sortField, String sortOrderBy) {
        Sort sort = Sort.by(sortField);
        Sort sort1 =
                sortOrderBy.toUpperCase().equals("ASC") ?
                        sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 15, sort1);

        Page<BusOvoUser> all = repository.findAllExcludeUserId(pageable, sysUserService.getIdOfSelf());
        for (BusOvoUser busOvoUserBind : all.getContent()) {
            SysUser user = sysUserService.getById(busOvoUserBind.getUserId());
            busOvoUserBind.setUser(user);
        }

        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", all.getContent());
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }

    @Override
    public HashMap<String, Object> getNear(Integer pageNumber, Double range) {
        BusOvoUser self = getSelf();
        // 获取纬度
        Double latitude = self.getLocation().getLatitude();
        // 获取经度
        Double longitude = self.getLocation().getLongitude();
        // 经纬度保留五位小数
        latitude = Double.valueOf(String.format("%.5f", latitude));
        longitude = Double.valueOf(String.format("%.5f", longitude));

        // 获取附近的人的经纬度地点信息
        Page<BusOvoUserBindLocation> all = busOvoUserBindLocationRepository
                .findAllWithDistance(PageRequest.of(pageNumber - 1, 15), latitude, longitude, range);
        List<BusOvoUserBindLocation> content = all.getContent();

        List<BusOvoUser> busOvoUserBindList = new ArrayList<>();
        for (BusOvoUserBindLocation busOvoUserBindLocation : content) {
            if (!busOvoUserBindLocation.getUserId().equals(self.getUserId())) {
                busOvoUserBindList.add(repository.findByUserId(busOvoUserBindLocation.getUserId()).setUser(sysUserService.getById(busOvoUserBindLocation.getUserId())));
            }
        }

        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", busOvoUserBindList);
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }


    /**
     * 设置位置的具体信息
     */
    private Boolean useMahuatengAddressApi(Double latitude, Double longitude, BusOvoUserBindLocation busOvoUserBindLocation) {

        JSONObject locationInfo = tencentApiLocationService.getLocationInfo(latitude, longitude, 0);

        if (locationInfo == null) {
            // 以后完善这部分代码
            // 如果为空，则说明费用不足或者其他原因，便不可再用腾讯地图api
            log.info("调用第一个腾讯地图api失败，原因可能是费用不足或者其他原因");
            return false;
        }

        // 获取国家
        String country = locationInfo.getJSONObject("ad_info").getString("nation");
        if (country != null) {
            busOvoUserBindLocation.setCountry(country);
        }
        // 获取省份
        String province = locationInfo.getJSONObject("ad_info").getString("province");
        if (province != null) {
            busOvoUserBindLocation.setProvince(province);
        }
        // 获取城市
        String city = locationInfo.getJSONObject("ad_info").getString("city");
        if (city != null) {
            busOvoUserBindLocation.setCity(city);
        }
        // 获取区县
        String district = locationInfo.getJSONObject("ad_info").getString("district");
        if (district != null) {
            busOvoUserBindLocation.setDistrict(district);
        }
        // 获取街道
        String street = locationInfo.getJSONObject("ad_info").getString("street");
        if (street != null) {
            busOvoUserBindLocation.setAddressTitle(street);
        }
        // 获取街道号
        String streetNumber = locationInfo.getJSONObject("ad_info").getString("street_number");
        if (streetNumber != null) {
            busOvoUserBindLocation.setAddress(streetNumber);
        }
        // 获取国家代码
        String countryCode = locationInfo.getJSONObject("ad_info").getString("nation_code");
        if (countryCode != null) {
            busOvoUserBindLocation.setCountryCode(countryCode);
        }
        // 获取城市代码
        String cityCode = locationInfo.getJSONObject("ad_info").getString("adcode");
        if (cityCode != null) {
            busOvoUserBindLocation.setAdCode(cityCode);
        }

        // 设置调用api的时间
        busOvoUserBindLocation.setGmtTencentMapApiTime(new Date());

        return true;
    }


    @Override
    public BusOvoUser updateLocation(Double latitude, Double longitude) {


        Long userId = sysUserService.getWithoutHideSensitiveInfo().getId();
        BusOvoUserBindLocation busOvoUserBindLocation = busOvoUserBindLocationRepository.findByUserId(userId);

        Boolean isNull = busOvoUserBindLocation == null;
        if (isNull) {
            busOvoUserBindLocation = new BusOvoUserBindLocation();
            busOvoUserBindLocation.setUserId(userId);
        }

        // 是否使用腾讯地图api获取位置信息成功
        Boolean useMahuatengAddressApiBool = false;

        // 如果数据库中没有位置信息，就通过api获取
        if (isNull || busOvoUserBindLocation.getGmtTencentMapApiTime() == null) {
            busOvoUserBindLocation.setLatitude(latitude);
            busOvoUserBindLocation.setLongitude(longitude);
            useMahuatengAddressApiBool = useMahuatengAddressApi(latitude, longitude, busOvoUserBindLocation);
            // 使用腾讯地图api获取位置信息，如果调用成功，就保存到数据库，如果调用失败，就进行下一步
            if (useMahuatengAddressApiBool) {
                busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
                return getSelf();
            }
        }

        if (useMahuatengAddressApiBool) {
            // 如果附近10公里内有用户，如果有，就直接使用其省市区
            Page<BusOvoUserBindLocation> all = busOvoUserBindLocationRepository
                    .findAllWithDistance(PageRequest.of(0, 1), latitude, longitude, 10000.0);
            if (all.getContent().size() > 0) {
                BusOvoUserBindLocation busOvoUserBindLocation1 = all.getContent().get(0);
                busOvoUserBindLocation.setProvince(busOvoUserBindLocation1.getProvince());
                busOvoUserBindLocation.setCity(busOvoUserBindLocation1.getCity());
                busOvoUserBindLocation.setDistrict(busOvoUserBindLocation1.getDistrict());
                busOvoUserBindLocation.setLatitude(latitude);
                busOvoUserBindLocation.setLongitude(longitude);
                busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
                return getSelf();
            }
        }


        // 如果小于三个小时，就保存经纬度，不再做其它操作
        if (busOvoUserBindLocation.getGmtTencentMapApiTime() != null && busOvoUserBindLocation.getGmtTencentMapApiTime().getTime() + 3 * 60 * 60 * 1000 > System.currentTimeMillis()) {
            busOvoUserBindLocation.setLatitude(latitude);
            busOvoUserBindLocation.setLongitude(longitude);
            busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
            return getSelf();
        }

        // 如果经纬度的距离小于100公里与调用api的时间小于24小时，就保存经纬度，不再做其它操作
        LocationEntity locationEntity = new LocationEntity(latitude, longitude);
        LocationEntity locationEntity1 = new LocationEntity(busOvoUserBindLocation.getLatitude(), busOvoUserBindLocation.getLongitude());
        Double distance = LocationUtil.getDistance(locationEntity, locationEntity1);
        if (busOvoUserBindLocation.getGmtTencentMapApiTime() != null && busOvoUserBindLocation.getGmtTencentMapApiTime() != null && distance < 100000 && busOvoUserBindLocation.getGmtTencentMapApiTime().getTime() + 24 * 60 * 60 * 1000 > System.currentTimeMillis()) {
            busOvoUserBindLocation.setLatitude(latitude);
            busOvoUserBindLocation.setLongitude(longitude);
            busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
            return getSelf();
        }

        // 如果附近10公里内有用户，如果有，就直接使用其省市区
        Page<BusOvoUserBindLocation> all = busOvoUserBindLocationRepository
                .findAllWithDistance(PageRequest.of(0, 1), latitude, longitude, 10000.0);
        if (all.getContent().size() > 0) {
            BusOvoUserBindLocation busOvoUserBindLocation1 = all.getContent().get(0);
            busOvoUserBindLocation.setProvince(busOvoUserBindLocation1.getProvince());
            busOvoUserBindLocation.setCity(busOvoUserBindLocation1.getCity());
            busOvoUserBindLocation.setDistrict(busOvoUserBindLocation1.getDistrict());
            busOvoUserBindLocation.setLatitude(latitude);
            busOvoUserBindLocation.setLongitude(longitude);
            busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
            return getSelf();
        }

        // 如果与上一次位置的距离小于100公里，就保存经纬度，不再做其它操作
        if (distance < 100000) {
            busOvoUserBindLocation.setLatitude(latitude);
            busOvoUserBindLocation.setLongitude(longitude);
            busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
            return getSelf();
        }


        busOvoUserBindLocation.setLatitude(latitude);
        busOvoUserBindLocation.setLongitude(longitude);
        busOvoUserBindLocation.setGmtTencentMapApiTime(new Date());
        useMahuatengAddressApi(latitude, longitude, busOvoUserBindLocation);
        busOvoUserBindLocationRepository.save(busOvoUserBindLocation);
        return getSelf();
    }


    /**
     * 通过经纬度获取位置信息
     *
     * @return 位置信息
     */
    @Override
    public JSONArray getLocationInfoByRangeSelf() {
        if(redisUtil.lock("getLocationInfoByRangeSelf:token:"+httpRequestHeaderUtil.getToken(),
                "1", 100, TimeUnit.MILLISECONDS)){
            // 告诉用户请求太频繁，虽然一般不会出现这种情况
            throw sysExceptionService.getException("requestTooMuch");
        }
        Long idOfSelf = sysUserService.getIdOfSelf();

        // 从redis中获取
        String redisKey = "locationInfoByRange:userId:"+idOfSelf;
        String locationInfoByRangeJsonStr = redisUtil.get(redisKey);
        BusOvoUserBindLocation busOvoUserBindLocation = null;
        if (locationInfoByRangeJsonStr!=null){
            // 如果redis中有，就直接返回
            return JSON.parseArray(locationInfoByRangeJsonStr);
        }

        // 获取用户的经纬度
        busOvoUserBindLocation = busOvoUserBindLocationRepository.findByUserId(idOfSelf);
        if (busOvoUserBindLocation == null) {
            // 如果用户没有绑定经纬度，就返回空
            return new JSONArray();
        }
        JSONArray locationInfoByRange = tencentApiLocationService.getLocationInfoByRange(busOvoUserBindLocation.getLatitude(), busOvoUserBindLocation.getLongitude(), 500.0, 0);
        // 保存到redis中，5分钟过期
        redisUtil.setEx(redisKey, JSON.toJSONString(locationInfoByRange), 5, TimeUnit.MINUTES);
        return locationInfoByRange;
    }

    @Override
    public BusOvoUserBindVO follow(Long userId) {
        BusOvoUserBindVO self = getSelf();
        Long idOfSelf = self.getUserId();
        boolean b = busOvoUserFollowRepository.existsByUserIdAndFollowUserId(idOfSelf, userId);
        if (!b) {
            BusOvoUserFollow busOvoUserFollow = new BusOvoUserFollow();
            busOvoUserFollow.setUserId(idOfSelf);
            busOvoUserFollow.setFollowUserId(userId);
            busOvoUserFollowRepository.save(busOvoUserFollow);
        }
        // 查询是否也被对方关注
        boolean b1 = busOvoUserFollowRepository.existsByUserIdAndFollowUserId(userId, idOfSelf);
        if (b1) {
            if(
                    !busOvoUserMutualRepository.existsByUser1IdAndUser2Id(idOfSelf, userId)&&
                            !busOvoUserMutualRepository.existsByUser1IdAndUser2Id(userId, idOfSelf)
            ){
                // 如果也被对方关注，就互相关注
                busOvoUserMutualRepository.save(new BusOvoUserMutual().setUser1Id(idOfSelf).setUser2Id(userId));
            }
        }

        return getSelf();
    }

    @Override
    public BusOvoUserBindVO unFollow(Long userId) {
        BusOvoUserBindVO self = getSelf();
        Long idOfSelf = self.getUserId();

        List<BusOvoUserFollow> byUserIdAndFollowUserId = busOvoUserFollowRepository.findByUserIdAndFollowUserId(idOfSelf, userId);
        busOvoUserFollowRepository.deleteAll(byUserIdAndFollowUserId);
        // 查询是否也被对方关注
        boolean b1 = busOvoUserFollowRepository.existsByUserIdAndFollowUserId(userId, idOfSelf);
        if (b1) {
            // 如果也被对方关注，就删除互相关注
            List<BusOvoUserMutual> byUser1IdAndUser2Id = busOvoUserMutualRepository.findByUser1IdAndUser2Id(idOfSelf, userId);
            busOvoUserMutualRepository.deleteAll(byUser1IdAndUser2Id);
            List<BusOvoUserMutual> byUser1IdAndUser2Id1 = busOvoUserMutualRepository.findByUser1IdAndUser2Id(userId, idOfSelf);
            busOvoUserMutualRepository.deleteAll(byUser1IdAndUser2Id1);
        }
        return getSelf();
    }

    @Override
    public boolean isFollow(Long userId) {
        return busOvoUserFollowRepository.existsByUserIdAndFollowUserId(sysUserService.getIdOfSelf(), userId);
    }

    @Override
    public boolean isMutual(Long userId) {
        return busOvoUserMutualRepository.existsByUser1IdAndUser2Id(sysUserService.getIdOfSelf(), userId)||busOvoUserMutualRepository.existsByUser1IdAndUser2Id(userId, sysUserService.getIdOfSelf());
    }

    @Override
    public HashMap<String, Object> getFollowerListByUserId(Long userId, Integer pageNumber, String sortField, String sortOrderBy) {
        Sort sort = Sort.by(sortField);
        Sort sort1 =
                sortOrderBy.toUpperCase().equals("ASC") ?
                        sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 30, sort1);

        Page<BusOvoUserFollow> byFollowUserId = busOvoUserFollowRepository.findByFollowUserId(userId, pageable);
        for (BusOvoUserFollow busOvoUserFollow : byFollowUserId) {
            busOvoUserFollow.setOvoUser(getByUserId(busOvoUserFollow.getUserId()));
            busOvoUserFollow.setFollowOvoUser(getByUserId(busOvoUserFollow.getFollowUserId()));
        }

        HashMap<String, Object> map = new HashMap<>(10);

        map.put("pageSize", 30);
        map.put("totalElements", byFollowUserId.getTotalElements());
        map.put("totalPages", byFollowUserId.getTotalPages());
        map.put("content", byFollowUserId.getContent());
        return map;
    }

    @Override
    public HashMap<String, Object> getFollowerListSelf(Integer pageNumber, String sortField, String sortOrderBy) {
        return getFollowerListByUserId(sysUserService.getIdOfSelf(), pageNumber, sortField, sortOrderBy);
    }

    @Override
    public HashMap<String, Object> getFollowingListByUserId(Long userId, Integer pageNumber, String sortField, String sortOrderBy) {
        Sort sort = Sort.by(sortField);
        Sort sort1 =
                sortOrderBy.toUpperCase().equals("ASC") ?
                        sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 30, sort1);

        Page<BusOvoUserFollow> byUserId = busOvoUserFollowRepository.findByUserId(userId, pageable);
        for (BusOvoUserFollow busOvoUserFollow : byUserId) {
            busOvoUserFollow.setOvoUser(getByUserId(busOvoUserFollow.getUserId()));
            busOvoUserFollow.setFollowOvoUser(getByUserId(busOvoUserFollow.getFollowUserId()));
        }

        HashMap<String, Object> map = new HashMap<>(10);
        map.put("pageSize", 30);
        map.put("totalElements", byUserId.getTotalElements());
        map.put("totalPages", byUserId.getTotalPages());
        map.put("content", byUserId.getContent());
        return map;
    }

    @Override
    public HashMap<String, Object> getFollowingListSelf(Integer pageNumber, String sortField, String sortOrderBy) {
        return getFollowingListByUserId(sysUserService.getIdOfSelf(), pageNumber, sortField, sortOrderBy);
    }

    @Override
    public HashMap<String, Object> getFollowerAndFollowingCountByUserId(Long userId) {
        // 获取用户的粉丝数
        Long followersCount = busOvoUserFollowRepository.countByFollowUserId(userId);
        // 获取用户的关注数
        Long followCount = busOvoUserFollowRepository.countByUserId(userId);
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("followersCount", followersCount);
        map.put("followCount", followCount);
        return map;
    }

    @Override
    public HashMap<String, Object> getMutual(Long userId,Integer pageNumber, String sortField, String sortOrderBy) {
        Sort sort = Sort.by(sortField);
        Sort sort1 =
                sortOrderBy.toUpperCase().equals("ASC") ?
                        sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 30, sort1);

        Page<BusOvoUserMutual> byUserId = busOvoUserMutualRepository.findOtherUserIdByUserId(userId, pageable);
        List<BusOvoUserMutual> content = byUserId.getContent();
        content.forEach(busOvoUserMutual -> {
            busOvoUserMutual.setOvoUser(getByUserId(busOvoUserMutual.getUser2Id()));
        });

        HashMap<String, Object> map = new HashMap<>(10);
        map.put("pageSize", 30);
        map.put("totalElements", byUserId.getTotalElements());
        map.put("totalPages", byUserId.getTotalPages());
        map.put("content", content);
        return map;
    }

    @Override
    public HashMap<String, Object> getMutualSelf(Integer pageNumber, String sortField, String sortOrderBy) {
        return getMutual(sysUserService.getIdOfSelf(), pageNumber, sortField, sortOrderBy);
    }
}



