package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.service.IApiLocationService;
import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoPostImage;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.business.ovo.service.service.IBusOvoUserBindService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 * @author 方某方
 */
@Service
public class BusOvoPostServiceImpl implements IBusOvoPostService {

    @Resource
    private BusOvoPostRepository repository;

    @Resource
    private IBusOvoUserBindService userBindService;

    @Resource
    private IApiLocationService locationService;

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
        BusOvoUserBind self = userBindService.getSelf();


        String detailIdOfLocation = dto.getDetailIdOfLocation();
        if(detailIdOfLocation==null&&detailIdOfLocation==""&&
                detailIdOfLocation.equals("province")
        &&detailIdOfLocation.equals("city")
        &&detailIdOfLocation.equals("district")
        ){

            // 使用位置查询id获取位置信息
            JSONObject locationInfoById = locationService.getLocationInfoById(dto.getDetailIdOfLocation());

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
}

