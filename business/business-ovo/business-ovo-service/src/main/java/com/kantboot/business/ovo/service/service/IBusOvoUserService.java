package com.kantboot.business.ovo.service.service;

import com.alibaba.fastjson2.JSONArray;
import com.kantboot.business.ovo.module.dto.BusOvoUserBindDTO;
import com.kantboot.business.ovo.module.entity.BusOvoUser;
import com.kantboot.business.ovo.module.vo.BusOvoUserBindVO;
import com.kantboot.util.common.result.PageResult;

import java.util.HashMap;

/**
 * 绑定的用户
 * @author 方某方
 */
public interface IBusOvoUserService {
    /**
     * 根据用户id获取用户绑定信息
     */
    BusOvoUser getByUserId(Long userId);


    /**
     * 根据用户id获取用户绑定信息
     * @param dto 用户绑定信息
     * @return 用户绑定信息
     */
    BusOvoUser bind(BusOvoUserBindDTO dto);

    /**
     * 获取用户自身的绑定信息
     * @return 用户绑定信息
     */
    BusOvoUserBindVO getSelf();

    /**
     * 获取推荐列表
     * @param pageNumber 页码
     * @param sortField 排序的字段
     * @param sortOrderBy 排序的方式
     * @return 推荐列表
     */
    PageResult getRecommendList(Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * findByIdGreaterThanOrderByIdAsc
     * @param id id
     * @return 用户信息
     */
    PageResult findGreater(Long id);


    /**
     * 获取附近的人
     * @param pageNumber 页码
     * @param range 范围
     * @return 附近的人
     */
    PageResult getNear(Integer pageNumber, Double range);

    /**
     * 修改位置信息
     * @param latitude 纬度
     * @param longitude 经度
     * @return 修改后的信息
     */
    BusOvoUser updateLocation(Double latitude, Double longitude);

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
    BusOvoUserBindVO follow(Long userId);


    /**
     * 取消关注
     * @param userId 用户id
     */
    BusOvoUserBindVO unFollow(Long userId);

    /**
     * 查看是否关注
     * @param userId 用户id
     * @return 是否关注
     */
    boolean isFollow(Long userId);

    /**
     * 查看是否为互相关注
     * @param userId 用户id
     * @return 是否为互相关注
     */
    boolean isMutual(Long userId);

    /**
     * 根据用户id查看关注ta的人
     * @param userId 用户id
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 关注ta的人
     */
    HashMap<String,Object> getFollowerListByUserId(Long userId, Integer pageNumber, String sortField, String sortOrderBy);


    /**
     * 获取所有关注我的人
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 所有关注我的人
     */
    HashMap<String,Object> getFollowerListSelf(Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 获取所有ta关注的人
     * @param userId 用户id
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 所有ta关注的人
     */
    HashMap<String,Object> getFollowingListByUserId(Long userId, Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 获取所有我关注的人
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 所有我关注的人
     */
    HashMap<String,Object> getFollowingListSelf(Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 根据用户id获取关注和粉丝数量
     * @param userId 用户id
     * @return 关注和粉丝数量
     */
    HashMap<String,Object> getFollowerAndFollowingCountByUserId(Long userId);

    /**
     * 根据两个用户id查询出互相关注
     * @param userId 用户id
     * @return 互相关注
     */
    HashMap<String,Object> getMutual(Long userId,Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 根据用户id查询出自己的互相关注
     * @return 自己的互相关注
     */
    HashMap<String,Object> getMutualSelf(Integer pageNumber, String sortField, String sortOrderBy);
}
