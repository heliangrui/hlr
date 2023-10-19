package com.hlr.common.geom;

import com.hlr.common.entity.Point;

/**
 * GisConvert
 * Description:
 * date: 2023/9/15 17:38
 *
 * @author hlr
 */
public class GisConvert {

    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    // π
    private static double pi = 3.1415926535897932384626;
    // 长半轴
    private static double a = 6378245.0;
    // 扁率
    private static double ee = 0.00669342162296594323;

    public static boolean out_of_china(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        } else if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    // point 坐标转换

    /**
     * @param type 转换类型  0天地图 1高德/腾讯 2百度坐标
     * @Point: 原始点
     **/
    public static Point autoExchange(Point point, Integer type) {
        GisTypeEnums tag = GisTypeEnums.getData(type);
        Point result = null;
        if (point.getLongitude() == null || point.getLatitude() == null) {
            return point;
        }

        switch (GisTypeEnums.getData(point.getType())) {
            case BD09:
                switch (tag) {
                    case WGS84:
                        result = bd09towgs84(point.getLongitude(), point.getLatitude());
                        break;
                    case GCJ02:
                        result = bd09togcj02(point.getLongitude(), point.getLatitude());
                        break;
                    case BD09:
                        result = point;
                        break;
                }
                break;
            case GCJ02:
                switch (tag) {
                    case WGS84:
                        result = gcj02towgs84(point.getLongitude(), point.getLatitude());
                        break;
                    case GCJ02:
                        result = point;
                        break;
                    case BD09:
                        result = gcj02tobd09(point.getLongitude(), point.getLatitude());
                        break;
                }
                break;
            case WGS84:
                switch (tag) {
                    case WGS84:
                        result = point;
                        break;
                    case GCJ02:
                        result = wgs84togcj02(point.getLongitude(), point.getLatitude());
                        ;
                        break;
                    case BD09:
                        result = wgs84tobd09(point.getLongitude(), point.getLatitude());
                        break;
                }
                break;
        }
        return result;
    }

    public static double transformlat(double lon, double lat) {
        double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat + 0.2 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * pi) + 20.0 * Math.sin(2.0 * lon * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 * Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformlng(double lon, double lat) {
        double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
        ret += (20.0 * Math.sin(6.0 * lon * pi) + 20.0 * Math.sin(2.0 * lon * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lon / 12.0 * pi) + 300.0 * Math.sin(lon / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * WGS84转GCJ02(火星坐标系)
     *
     * @param wgs_lon WGS84坐标系的经度
     * @param wgs_lat WGS84坐标系的纬度
     * @return 火星坐标数组
     */
    public static Point wgs84togcj02(double wgs_lon, double wgs_lat) {
        Point point = new Point();
        point.setType(GisTypeEnums.GCJ02.value);
        if (out_of_china(wgs_lon, wgs_lat)) {
            point.setLatitude(wgs_lat);
            point.setLongitude(wgs_lon);
            return point;
        }
        double dlat = transformlat(wgs_lon - 105.0, wgs_lat - 35.0);
        double dlng = transformlng(wgs_lon - 105.0, wgs_lat - 35.0);
        double radlat = wgs_lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = wgs_lat + dlat;
        double mglng = wgs_lon + dlng;
        point.setLatitude(mglat);
        point.setLongitude(mglng);
        return point;
    }

    /**
     * GCJ02(火星坐标系)转GPS84
     *
     * @param gcj_lon 火星坐标系的经度
     * @param gcj_lat 火星坐标系纬度
     * @return WGS84坐标数组
     */
    public static Point gcj02towgs84(double gcj_lon, double gcj_lat) {
        Point point = new Point();
        point.setType(GisTypeEnums.WGS84.value);
        if (out_of_china(gcj_lon, gcj_lat)) {
            point.setLatitude(gcj_lat);
            point.setLongitude(gcj_lon);
            return point;
        }
        double dlat = transformlat(gcj_lon - 105.0, gcj_lat - 35.0);
        double dlng = transformlng(gcj_lon - 105.0, gcj_lat - 35.0);
        double radlat = gcj_lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = gcj_lat + dlat;
        double mglng = gcj_lon + dlng;

        point.setLatitude(gcj_lat * 2 - mglat);
        point.setLongitude(gcj_lon * 2 - mglng);
        return point;
    }


    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     * <p>
     * 谷歌、高德——>百度
     *
     * @param gcj_lon 火星坐标经度
     * @param gcj_lat 火星坐标纬度
     * @return 百度坐标数组
     */
    public static Point gcj02tobd09(double gcj_lon, double gcj_lat) {
        Point point = new Point();
        point.setType(GisTypeEnums.BD09.value);

        double z = Math.sqrt(gcj_lon * gcj_lon + gcj_lat * gcj_lat) + 0.00002 * Math.sin(gcj_lat * x_pi);
        double theta = Math.atan2(gcj_lat, gcj_lon) + 0.000003 * Math.cos(gcj_lon * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        point.setLongitude(bd_lng);
        point.setLatitude(bd_lat);
        return point;
    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     * <p>
     * 百度——>谷歌、高德
     *
     * @param bd_lon 百度坐标纬度
     * @param bd_lat 百度坐标经度
     * @return 火星坐标数组
     */
    public static Point bd09togcj02(double bd_lon, double bd_lat) {
        Point point = new Point();
        point.setType(GisTypeEnums.GCJ02.value);

        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        point.setLongitude(gg_lng);
        point.setLatitude(gg_lat);
        return point;
    }

    /**
     * WGS坐标转百度坐标系(BD-09)
     *
     * @param wgs_lng WGS84坐标系的经度
     * @param wgs_lat WGS84坐标系的纬度
     * @return 百度坐标数组
     */
    public static Point wgs84tobd09(double wgs_lng, double wgs_lat) {
        Point gcj = wgs84togcj02(wgs_lng, wgs_lat);
        Point bd09 = gcj02tobd09(gcj.getLongitude(), gcj.getLatitude());
        return bd09;
    }

    /**
     * 百度坐标系(BD-09)转WGS坐标
     *
     * @param bd_lng 百度坐标纬度
     * @param bd_lat 百度坐标经度
     * @return WGS84坐标数组
     */
    public static Point bd09towgs84(double bd_lng, double bd_lat) {
        Point gcj = bd09togcj02(bd_lng, bd_lat);
        Point wgs84 = gcj02towgs84(gcj.getLongitude(), gcj.getLatitude());
        return wgs84;
    }

}
