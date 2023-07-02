package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.kantboot.api.service.ITencentApiLocationService;
import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.*;
import com.kantboot.business.ovo.module.vo.BusOvoPostVO;
import com.kantboot.business.ovo.service.repository.BusOvoPostCommentRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostLikeRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.business.ovo.service.service.IBusOvoUserService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private IBusOvoUserService userBindService;

    @Resource
    private ITencentApiLocationService locationService;

    @Resource
    private BusOvoPostLikeRepository postLikeRepository;

    @Resource
    private BusOvoPostCommentRepository postCommentRepository;

    @Resource
    private RedisUtil redisUtil;

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

    @Override
    public HashMap<String, Object> getSelf(Integer pageNumber, String sortField, String sortOrderBy) {
        BusOvoUser self = userBindService.getSelf();
        Sort sort = Sort.by(sortField);
        Sort sort1=
                sortOrderBy.toUpperCase().equals("ASC")?
                        sort.ascending():sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber-1, 15, sort1);
        Page<BusOvoPost> all = repository.findAllByUserId(self.getUserId(), pageable);
        List<BusOvoPostVO> content = new ArrayList<>();
        for (BusOvoPost post : all.getContent()) {
            BusOvoPostVO vo = new BusOvoPostVO();
            BeanUtils.copyProperties(post, vo);
            vo.setLike(postLikeRepository.existsBusOvoPostLikeByUserIdAndPostId(self.getUserId(), post.getId()));
            vo.setLikeCount(postLikeRepository.countByPostId(post.getId()));
            vo.setCommentCount(postCommentRepository.countByPostId(post.getId()));
            content.add(vo);
        }

        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", content);
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }

    @Override
    public HashMap<String, Object> getRecommend(Integer pageNumber, String sortField, String sortOrderBy,Long time) {
        BusOvoUser self = userBindService.getSelf();
        Sort sort = Sort.by(sortField);
        Sort sort1=
                sortOrderBy.toUpperCase().equals("ASC")?
                        sort.ascending():sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber-1, 15, sort1);
//        Page<BusOvoPost> all = repository.findAllByAuditStatusCode("pass", pageable);
//        findAllByAuditStatusCodeAndGmtCreateGreaterThan
        Page<BusOvoPost> all = repository.findAllByAuditStatusCodeAndGmtCreateGreaterThan("pass",
                new Date(time)
                , pageable);


        List<BusOvoPostVO> content = new ArrayList<>();
        for (BusOvoPost post : all.getContent()) {
            BusOvoPostVO vo = new BusOvoPostVO();
            BeanUtils.copyProperties(post, vo);
            vo.setLikeCount(postLikeRepository.countByPostId(post.getId()));
            vo.setCommentCount(postCommentRepository.countByPostId(post.getId()));
            vo.setLike(postLikeRepository.existsBusOvoPostLikeByUserIdAndPostId(self.getUserId(),post.getId()));
            content.add(vo);
        }

        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", content);
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }



    @Override
    public HashMap<String, Object> getNear(Integer pageNumber,Double range) {
        BusOvoUser self = userBindService.getSelf();
        // 获取纬度
        Double latitude = self.getLocation().getLatitude();
        // 获取经度
        Double longitude = self.getLocation().getLongitude();
        // 经纬度保留五位小数
        latitude = Double.valueOf(String.format("%.5f", latitude));
        longitude = Double.valueOf(String.format("%.5f", longitude));

        Page<BusOvoPost> all =
                repository.findAllWithDistance(PageRequest.of(pageNumber - 1, 15),
                        latitude, longitude, range);
        List<BusOvoPostVO> content = new ArrayList<>();
        for (BusOvoPost post : all.getContent()) {
            BusOvoPostVO vo = new BusOvoPostVO();
            BeanUtils.copyProperties(post, vo);
            vo.setLikeCount(postLikeRepository.countByPostId(post.getId()));
            vo.setCommentCount(postCommentRepository.countByPostId(post.getId()));
            vo.setLike(postLikeRepository.existsBusOvoPostLikeByUserIdAndPostId(self.getUserId(),post.getId()));
            content.add(vo);
        }
        HashMap<String, Object> result = new HashMap<>(5);
        result.put("totalElements", all.getTotalElements());
        result.put("totalPage", all.getTotalPages());
        result.put("content", content);
        result.put("number", all.getNumber() + 1);
        result.put("size", all.getSize());
        return result;
    }

    // guava的锁
    private static final Interner<String> pool = Interners.newWeakInterner();
    @Override
    public BusOvoPostVO like(Long postId) {
        long l = System.currentTimeMillis();
        BusOvoUser self = userBindService.getSelf();
        synchronized (pool.intern(postId.toString())){
            long l1 = System.currentTimeMillis();
            int minTime = 1000;
            // 只有锁在1秒内才处理，如果超过1秒，就不处理
            if(l1-l<minTime){

                Boolean aBoolean = postLikeRepository.existsBusOvoPostLikeByUserIdAndPostId(self.getUserId(), postId);
                if(aBoolean){
                    List<BusOvoPostLike> allByUserIdAndPostId = postLikeRepository.findAllByUserIdAndPostId(self.getUserId(), postId);
                    postLikeRepository.deleteAll(allByUserIdAndPostId);
                }else{
                    BusOvoPostLike like = new BusOvoPostLike();
                    like.setUserId(self.getUserId());
                    like.setPostId(postId);
                    postLikeRepository.save(like);
                }
            }
            BusOvoPost post = repository.findById(postId).get();
            BusOvoPostVO vo = new BusOvoPostVO();
            BeanUtils.copyProperties(post, vo);
            vo.setLikeCount(postLikeRepository.countByPostId(post.getId()));
            vo.setCommentCount(postCommentRepository.countByPostId(post.getId()));
            vo.setLike(postLikeRepository.existsBusOvoPostLikeByUserIdAndPostId(self.getUserId(),post.getId()));
            return vo;
        }

    }


    @Override
    public BusOvoPostVO getById(Long id) {


        BusOvoPost post = repository.findById(id).get();
        BusOvoPostVO vo = new BusOvoPostVO();
        BeanUtils.copyProperties(post, vo);

        try{
            BusOvoUser self = userBindService.getSelf();
            vo.setLike(postLikeRepository.existsBusOvoPostLikeByUserIdAndPostId(self.getUserId(),post.getId()));
        }catch (BaseException e){
            if (e.getStateCode().equals("notLogin")) {
                // 如果没有登录，就将like设置为false
                vo.setLike(false);
            }
        }

        vo.setLikeCount(postLikeRepository.countByPostId(post.getId()));
        vo.setCommentCount(postCommentRepository.countByPostId(post.getId()));
        return vo;
    }

    @Override
    public BusOvoPostVO audit(BusOvoPost busOvoPost) {
        BusOvoPost post = repository.findById(busOvoPost.getId()).get();
        post.setAuditStatusCode(busOvoPost.getAuditStatusCode());
        post.setGmtAudit(new Date());

        if(busOvoPost.getAuditStatusCode().equals("reject")){
            post.setAuditRejectReason(busOvoPost.getAuditRejectReason());
        }

        BusOvoPost save = repository.save(post);
        BusOvoPostVO vo = new BusOvoPostVO();
        BeanUtils.copyProperties(save, vo);
        vo.setLikeCount(postLikeRepository.countByPostId(post.getId()));
        vo.setCommentCount(postCommentRepository.countByPostId(post.getId()));
        return vo;
    }
}

