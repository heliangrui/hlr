package com.hlr.common.entity;

import com.sun.istack.internal.NotNull;

/**
 * @author hlr
 */
public class Point {

    private Double longitude;

    private Double latitude;
    //类型 0天地图 1高德/腾讯 2百度坐标 
    private Integer type;

    public Point(){

    }
    public Point(Double longitude,Double latitude,Integer type){
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
