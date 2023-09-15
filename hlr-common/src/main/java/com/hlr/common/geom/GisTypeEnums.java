package com.hlr.common.geom;

public enum GisTypeEnums {
    WGS84(0,"大地坐标体系"),GCJ02(1,"火星坐标体系"),BD09(2,"百度坐标体系");

    int value;
    String data;

    GisTypeEnums(int value,String data){
        this.value=value;
        this.data=data;
    }

    public static GisTypeEnums getData(int value){
        for (GisTypeEnums scenesEnum : GisTypeEnums.values()) {
            if(scenesEnum.getValue() == value){
                return scenesEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getData() {
        return data;
    }
}
