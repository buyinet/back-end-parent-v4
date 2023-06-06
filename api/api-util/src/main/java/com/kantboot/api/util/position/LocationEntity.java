package com.kantboot.api.util.position;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 位置工具类
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class LocationEntity {

    public LocationEntity(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;



}
