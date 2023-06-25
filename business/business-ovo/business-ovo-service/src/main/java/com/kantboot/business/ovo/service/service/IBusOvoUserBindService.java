package com.kantboot.business.ovo.service.service;

import com.alibaba.fastjson2.JSONArray;
import com.kantboot.business.ovo.module.dto.BusOvoUserBindDTO;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;

import java.util.HashMap;

/**
 * 绑定的用户
 * @author 方某方
 */
public interface IBusOvoUserBindService {
    /**
     * 根据用户id获取用户绑定信息
     */
    BusOvoUserBind getByUserId(Long userId);


    /**
     * 根据用户id获取用户绑定信息
     * @param dto 用户绑定信息
     * @return 用户绑定信息
     */
    BusOvoUserBind bind(BusOvoUserBindDTO dto);

    /**
     * 获取用户自身的绑定信息
     * @return 用户绑定信息
     */
    BusOvoUserBind getSelf();

    /**
     * 获取推荐列表
     * @param pageNumber 页码
     * @param sortField 排序的字段
     * @param sortOrderBy 排序的方式
     * @return 推荐列表
     */
    HashMap<String,Object> getRecommendList(Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 获取附近的人
     * @param pageNumber 页码
     * @param range 范围
     * @return 附近的人
     */
    HashMap<String,Object> getNear(Integer pageNumber, Double range);

    /**
     * 修改位置信息
     * @param latitude 纬度
     * @param longitude 经度
     * @return 修改后的信息
     */
    BusOvoUserBind updateLocation(Double latitude, Double longitude);

    /**
     * 根据自身的经纬度范围查询
     * https://apis.map.qq.com/ws/place/v1/explore?key=${key}&boundary=nearby(${latitude},${longitude},${range},${expand})
     * @return 位置信息
     */
    JSONArray getLocationInfoByRangeSelf();

    /**
     * 进行关注
     * @param userId 用户id
     */
    void follow(Long userId);


    /**
     * 取消关注
     * @param userId 用户id
     */
    void unFollow(Long userId);

    /**
     * 查看是否关注
     */
    boolean isFollow(Long userId);

}
