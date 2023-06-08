package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.service.IApiLocationService;
import com.kantboot.business.ovo.module.dto.BusOvoUserBindDTO;
import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.module.entity.BusOvoUserBindLocation;
import com.kantboot.business.ovo.service.repository.BusOvoEmotionalOrientationRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserBindLocationRepository;
import com.kantboot.business.ovo.service.repository.BusOvoUserBindRepository;
import com.kantboot.business.ovo.service.service.IBusOvoUserBindService;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 绑定的用户的服务实现类
 * @author 方某方
 */
@Service
public class BusOvoUserBindServiceImpl implements IBusOvoUserBindService {

    @Resource
    private BusOvoUserBindRepository repository;
    @Resource
    private BusOvoEmotionalOrientationRepository emotionalOrientationRepository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private IApiLocationService apiLocationService;

    @Resource
    private BusOvoUserBindLocationRepository busOvoUserBindLocationRepository;

    @Override
    public BusOvoUserBind bind(BusOvoUserBindDTO dto) {
        // 获取当前用户
        SysUser sysUser = sysUserService.getWithoutHideSensitiveInfo();
        sysUser.setFileIdOfAvatar(dto.getFileIdOfAvatar())
                .setNickname(dto.getNickname())
                .setGender(dto.getGender())
                .setBirthday(dto.getBirthday());
        sysUserRepository.save(sysUser);



        // 获取当前用户的id
        Long userId = sysUser.getId();
        // 根据用户id查询用户绑定信息
        BusOvoUserBind busOvoUserBind = repository.findByUserId(userId);
        // 如果用户绑定信息为空
        if (busOvoUserBind == null) {
            // 创建用户绑定信息
            busOvoUserBind = new BusOvoUserBind();
            // 设置用户id
            busOvoUserBind.setUserId(userId);
            busOvoUserBind=repository.save(busOvoUserBind);
        }
        busOvoUserBind
                .setIntroduction(dto.getIntroduction())
                .setSexualOrientationCode(dto.getSexualOrientationCode())
                .setSadomasochismAttrCode(dto.getSadomasochismAttrCode());

        List<BusOvoEmotionalOrientation> busOvoEmotionalOrientationList = new ArrayList<>();
        List<String> emotionalOrientationCodeList = dto.getEmotionalOrientationCodeList();
        for (String emotionalOrientationCode : emotionalOrientationCodeList) {
            busOvoEmotionalOrientationList.add(emotionalOrientationRepository.findByCode(emotionalOrientationCode));
        }

        busOvoUserBind.setEmotionalOrientationList(busOvoEmotionalOrientationList);
        BusOvoUserBind result = repository.save(busOvoUserBind);


        return result;
    }


    @Override
    public BusOvoUserBind getSelf() {
        SysUser sysUser = sysUserService.getWithoutHideSensitiveInfo();
        Long userId = sysUser.getId();
        BusOvoUserBind busOvoUserBind = repository.findByUserId(userId);
        try{
            if (busOvoUserBind.getLocation()==null) {
                BusOvoUserBindLocation busOvoUserBindLocation = new BusOvoUserBindLocation();
                JSONObject locationInfoByIp = apiLocationService.getLocationInfoByIp(httpRequestHeaderUtil.getIp());
//            {"ip":"115.193.38.142","location":{"lat":30.18534,"lng":120.26457},"ad_info":{"nation":"中国","province":"浙江省","city":"杭州市","district":"萧山区","adcode":330109,"nation_code":156}}
                JSONObject adInfo = locationInfoByIp.getJSONObject("ad_info");
                String country = adInfo.getString("nation");
                if(country!=null){
                    busOvoUserBindLocation.setCountry(country);
                }
                String province = adInfo.getString("province");
                if(province!=null){
                    busOvoUserBindLocation.setProvince(province);
                }
                String city = adInfo.getString("city");
                if(city!=null){
                    busOvoUserBindLocation.setCity(city);
                }
                String district = adInfo.getString("district");
                if(district!=null){
                    busOvoUserBindLocation.setDistrict(district);
                }
                String areaCode = adInfo.getString("adcode");
                if(areaCode!=null){
                    busOvoUserBindLocation.setAdCode(areaCode);
                }
                String countryCode = adInfo.getString("nation_code");
                if(countryCode!=null){
                    busOvoUserBindLocation.setCountryCode(countryCode);
                }

                busOvoUserBind = repository.save(busOvoUserBind.setLocation(busOvoUserBindLocation));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return busOvoUserBind;
    }

    @Override
    public HashMap<String, Object>  getRecommendList(Integer pageNumber, String sortField, String sortOrderBy) {

        Sort sort = Sort.by(sortField);
        Sort sort1=
                sortOrderBy.toUpperCase().equals("ASC")?
                        sort.ascending():sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber-1, 15, sort1);

        Page<BusOvoUserBind> all = repository.findAll(pageable);
        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", all.getContent());
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }

    @Override
    public HashMap<String, Object> getNear(Integer pageNumber,Double range) {
        BusOvoUserBind self = getSelf();
        // 获取纬度
        Double latitude = self.getLocation().getLatitude();
        // 获取经度
        Double longitude = self.getLocation().getLongitude();

        Page<BusOvoUserBindLocation> all = busOvoUserBindLocationRepository
                .findAllWithDistance(PageRequest.of(pageNumber-1, 15),latitude,longitude,range);

        List<BusOvoUserBindLocation> content = all.getContent();
        System.out.println(JSON.toJSONString(content));
        System.out.println(content.size());
        List<BusOvoUserBind> busOvoUserBindList = new ArrayList<>();
        for (BusOvoUserBindLocation busOvoUserBindLocation : content) {
            if(!busOvoUserBindLocation.getUserId().equals(self.getUserId())){
                busOvoUserBindList.add(repository.findByUserId(busOvoUserBindLocation.getUserId()));
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


    @Override
    public BusOvoUserBind updateLocation(Double latitude, Double longitude) {
        JSONObject locationInfo = apiLocationService.getLocationInfo(latitude, longitude,0);
        Long userId = sysUserService.getWithoutHideSensitiveInfo().getId();
        BusOvoUserBindLocation busOvoUserBindLocation = busOvoUserBindLocationRepository.findByUserId(userId);
        if (busOvoUserBindLocation==null){
            busOvoUserBindLocation = new BusOvoUserBindLocation();
            busOvoUserBindLocation.setUserId(userId);
        }

        // 获取国家
        String country = locationInfo.getJSONObject("ad_info").getString("nation");
        if (country!=null){
            busOvoUserBindLocation.setCountry(country);
        }
        // 获取省份
        String province = locationInfo.getJSONObject("ad_info").getString("province");
        if (province!=null){
            busOvoUserBindLocation.setProvince(province);
        }
        // 获取城市
        String city = locationInfo.getJSONObject("ad_info").getString("city");
        if (city!=null){
            busOvoUserBindLocation.setCity(city);
        }
        // 获取区县
        String district = locationInfo.getJSONObject("ad_info").getString("district");
        if (district!=null){
            busOvoUserBindLocation.setDistrict(district);
        }
        // 获取街道
        String street = locationInfo.getJSONObject("ad_info").getString("street");
        if (street!=null){
            busOvoUserBindLocation.setAddressTitle(street);
        }
        // 获取街道号
        String streetNumber = locationInfo.getJSONObject("ad_info").getString("street_number");
        if (streetNumber!=null){
            busOvoUserBindLocation.setAddress(streetNumber);
        }
        // 获取国家代码
        String countryCode = locationInfo.getJSONObject("ad_info").getString("nation_code");
        if (countryCode!=null){
            busOvoUserBindLocation.setCountryCode(countryCode);
        }
        // 获取城市代码
        String cityCode = locationInfo.getJSONObject("ad_info").getString("adcode");
        if (cityCode!=null){
            busOvoUserBindLocation.setAdCode(cityCode);
        }
        busOvoUserBindLocation.setLatitude(latitude);
        busOvoUserBindLocation.setLongitude(longitude);

        busOvoUserBindLocationRepository.save(busOvoUserBindLocation);

        return getSelf();
    }


    /**
     * 通过经纬度获取位置信息
     * @return 位置信息
     */
    @Override
    public JSONArray getLocationInfoByRangeSelf() {
        Long idOfSelf = sysUserService.getIdOfSelf();
        BusOvoUserBindLocation busOvoUserBindLocation = busOvoUserBindLocationRepository.findByUserId(idOfSelf);
        if (busOvoUserBindLocation==null){
            return new JSONArray();
        }
        return apiLocationService.getLocationInfoByRange(busOvoUserBindLocation.getLatitude(), busOvoUserBindLocation.getLongitude(),500.0,0);
    }
}



