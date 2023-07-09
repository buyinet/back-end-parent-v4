package com.kantboot.business.ovo.module.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;
import com.kantboot.business.ovo.module.entity.BusOvoUser;
import com.kantboot.business.ovo.module.entity.BusOvoUserBindLocation;
import com.kantboot.system.module.entity.SysUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户时间戳魅力值排行榜的视图对象
 * 用于返回给前端的用户时间戳魅力值排行榜的数据
 * @author 方某方
 */
@Data
public class BusOvoUserCharmTopVO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户魅力值
     */
    private Long totalCharmValue;

    /**
     * 用户昵称，不返回给前端
     */
    @JsonIgnore
    private String nickname;

    /**
     * 用户性别
     */
    @JsonIgnore
    private Integer gender;

    /**
     * 用户生日
     */
    @JsonIgnore
    private Date birthday;

    /**
     * 用户头像文件id
     */
    @JsonIgnore
    private Long fileIdOfAvatar;

    /**
     * 用户简介
     */
    private String introduction;

    /**
     * 用户SM属性
     */
    private String sadomasochismAttrCode;

    /**
     * 用户性取向
     */
    private String sexualOrientationCode;

    /**
     * 用户省份
     */
    @JsonIgnore
    private String province;

    /**
     * 用户城市
     */
    @JsonIgnore
    private String city;

    /**
     * 用户经度
     */
    @JsonIgnore
    private Double longitude;

    /**
     * 用户纬度
     */
    @JsonIgnore
    private Double latitude;

    /**
     * 用户情感取向
     */
    @JsonIgnore
    private String emotionalOrientationCode;

    /**
     * 用户信息
     */
    public SysUser getUser() {
        SysUser sysUser = new SysUser();
        sysUser.setId(this.userId);
        sysUser.setNickname(this.nickname);
        sysUser.setGender(gender);
        sysUser.setBirthday(birthday);
        sysUser.setFileIdOfAvatar(this.fileIdOfAvatar);
        return sysUser;
    }

    /**
     * 地理位置信息
     */
    public BusOvoUserBindLocation getLocation() {
        BusOvoUserBindLocation busOvoUserBindLocation = new BusOvoUserBindLocation();
        busOvoUserBindLocation.setProvince(this.province);
        busOvoUserBindLocation.setCity(this.city);
        busOvoUserBindLocation.setLongitude(this.longitude);
        busOvoUserBindLocation.setLatitude(this.latitude);
        return busOvoUserBindLocation;
    }

    /**
     * 获取用户的emotionalOrientationList
     */
    public List<BusOvoEmotionalOrientation> getEmotionalOrientationList() {
        BusOvoEmotionalOrientation busOvoEmotionalOrientation = new BusOvoEmotionalOrientation().setCode(this.emotionalOrientationCode);
        if (this.emotionalOrientationCode == null) {
            return new ArrayList<>();
        }
        return List.of(busOvoEmotionalOrientation);
    }





}
