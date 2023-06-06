package com.kantboot.api.util.position;

/**
 * 位置工具类
 * 1. 计算两个经纬度之间的距离
 * 2. 计算两个经纬度之间的方位角
 * 3. 根据经纬度和距离计算出最大最小经纬度
 */
public class LocationUtil {

    /**
     * 计算两个经纬度之间的距离
     *
     * @param locationEntity1 经纬度1
     * @param locationEntity2 经纬度2
     * @return 两个经纬度之间的距离，单位为m
     */
    public static double getDistance(LocationEntity locationEntity1, LocationEntity locationEntity2) {
        double lat1 = locationEntity1.getLatitude();
        double lng1 = locationEntity1.getLongitude();
        double lat2 = locationEntity2.getLatitude();
        double lng2 = locationEntity2.getLongitude();
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 计算两个经纬度之间的方位角
     *
     * @param locationEntity1 经纬度1
     * @param locationEntity2 经纬度2
     * @return 两个经纬度之间的方位角，单位为°
     */
    public static double getAngle(LocationEntity locationEntity1, LocationEntity locationEntity2) {
        double lat1 = locationEntity1.getLatitude();
        double lng1 = locationEntity1.getLongitude();
        double lat2 = locationEntity2.getLatitude();
        double lng2 = locationEntity2.getLongitude();
        double y = Math.sin(Math.toRadians(lng2 - lng1)) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
                Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.cos(Math.toRadians(lng2 - lng1));
        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        return brng;
    }

    /**
     * 根据经纬度和距离计算出最大最小经纬度
     *
     * @param locationEntity 经纬度
     * @param distance       距离
     * @return 最大最小经纬度
     */
    public static LocationEntity[] getMinMax(LocationEntity locationEntity, double distance) {
        double lat = locationEntity.getLatitude();
        double lng = locationEntity.getLongitude();
        double dlng = 2 * Math.asin(Math.sin(distance / (2 * 6378137)) / Math.cos(Math.toRadians(lat)));
        dlng = Math.toDegrees(dlng);
        double dlat = distance / 6378137;
        dlat = Math.toDegrees(dlat);
        double minlat = lat - dlat;
        double maxlat = lat + dlat;
        double minlng = lng - dlng;
        double maxlng = lng + dlng;
        return new LocationEntity[]{
                new LocationEntity(minlat,minlng),
                new LocationEntity(maxlat,maxlng)
        };
    }



}
