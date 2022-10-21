/**
 * Copyright 2020--2020
 * Zhongtie Trimble Digital Engineering and Construction Co., Ltd
 *
 * @description 用于高斯投影坐标相关计算
 * @author jwang
 * @date Jan 10, 2017 11:14:31 AM
 */
package com.dk.microgis.gis.gausscomput;
/*
 * *@Component
 * 高斯投影坐标正反算类.
 */


import com.dk.microgis.base.Point2D;

public class GaussProjection {

    /**
     * @fileoverview 高斯投影坐标转换功能，对外开放。 只支持同一个椭球面上坐标转换。
     * @created:2020/1/7
     * @version 1.0
     */

    static double arcSeconds = 206264.8062471;

    /**
     * 根据椭球面上的大地坐标，计算对应的高斯平面直角坐标。
     *
     * @param ellipsoidType 椭球类型，类型为整数类型，1 代表WGS-84椭球；2代表北京54坐标 椭球；3代表西安80坐标椭球；4
     *                      代表国家2000坐标椭球；
     * @param lat           ,大地坐标系里面的纬度，单位为度。例如：23.67892131；
     * @param lng           ，大地坐标系里面的纬度，单位为度。例如:120.44421134;
     * @param lng0          , 投影到高斯平面坐标系的中央经度。单位为度。如120.0；
     * @return 返回投影到高斯平面直角坐标系中的直角坐标{jsonObjec}数据。
     */
    public static Point2D bl2Xy(int ellipsoidType, double lat, double lng, double lng0) {

        EllipsoidParam ellipsoidParam = EllipsoidParams(ellipsoidType);
        double a = ellipsoidParam.getA();
        double b = ellipsoidParam.getB();

        double l = (lng - lng0) * 3600.0; // 经度差转换为秒；
        double arcLat = lat * 2.0 * Math.PI / 360.0;
        double ee = (Math.pow(a, 2.0) - Math.pow(b, 2.0)) / Math.pow(a, 2.0);
        double ee2 = (Math.pow(a, 2.0) - Math.pow(b, 2.0)) / Math.pow(b, 2.0);
        double N = a / (Math.sqrt(1.0 - ee * Math.pow(Math.sin(arcLat), 2.0)));
        double t = Math.tan(arcLat);
        double η2 = ee2 * Math.pow(Math.cos(arcLat), 2.0);
        double centerLine = centerMeridian(ellipsoidType, arcLat);

        double x = centerLine
                + ((N * Math.sin(arcLat) * Math.cos(arcLat) * Math.pow(l, 2.0)) / (2.0 * Math
                .pow(arcSeconds, 2.0)))
                + ((N
                * Math.sin(arcLat)
                * Math.pow(Math.cos(arcLat), 3.0)
                * (5.0 - Math.pow(t, 2.0) + 9.0 * η2 + 4 * Math.pow(η2,
                2.0)) * Math.pow(l, 4.0)) / (24.0 * Math.pow(
                arcSeconds, 4.0)))
                + ((N * Math.sin(arcLat) * Math.pow(Math.cos(arcLat), 5.0)
                * (61.0 - 58.0 * Math.pow(t, 2.0) + Math.pow(t, 4.0)) * Math
                .pow(l, 4.0)) / (720.0 * Math.pow(arcSeconds, 6.0)));

        double y = ((N * Math.cos(arcLat) * l) / arcSeconds)
                + ((N * Math.pow(Math.cos(arcLat), 3.0)
                * (1.0 - Math.pow(t, 2.0) + η2) * Math.pow(l, 3.0)) / (6.0 * Math
                .pow(arcSeconds, 3.0)))
                + (((N * Math.pow(Math.cos(arcLat), 5.0))
                * (5.0 - 18.0 * Math.pow(t, 2.0) + Math.pow(t, 4.0)
                + 14.0 * η2 - 58.0 * η2 * Math.pow(t, 2.0)) * Math
                .pow(l, 5.0)) / (120.0 * Math.pow(arcSeconds, 5.0)));


        double e2 = Math.sqrt(a * a - b * b) / b;  //椭球第二偏心率
        double q = e2 * e2 * Math.pow(Math.cos(arcLat), 2);

        double y1 = N * Math.cos(arcLat) * l / arcSeconds + N * Math.pow(Math.cos(arcLat), 3) * (1 - t * t + q) * l * l * l / (6 * Math.pow(arcSeconds, 3))
                + N * Math.pow(Math.cos(arcLat), 5) * (5 - 18 * t * t + t * t * t * t + 14 * q - 58 * t * t * q) * Math.pow(l, 5) / (120 * Math.pow(arcSeconds, 5));

        Point2D point = new Point2D();
        point.setX(x);
        point.setY(y);
        return point;
    }

    /*
     * 根据高斯平面直角坐标反算对应的经纬度坐标，单位为弧度，计算出的经度只是经度差值，实际的经度还必须加上中央子午线经度。
     *
     * @param elliposidType 椭球类型，类型为整数类型，1 代表WGS-84椭球；2代表北京54坐标 椭球；3代表西安80坐标椭球；4
     * 代表国家2000坐标椭球；
     *
     * @parma x, 高斯平面直角坐标的北坐标X，
     *
     * @param y, 高斯平面直角坐标系中的东坐标Y，
     *
     * @return 高斯大地坐标的经度差和纬度{JsonObject}，单位为弧度
     */
    public static double[] XYToBL(int elliposidType, Point2D point) {
        EllipsoidParam ellipsoidParam = EllipsoidParams(elliposidType);
        double longAxisa = ellipsoidParam.getA();
        double shortAxisb = ellipsoidParam.getB();
        double x = point.getX();
        double y = point.getY();
        double ee = (longAxisa * longAxisa - shortAxisb * shortAxisb)
                / (longAxisa * longAxisa);
        double Bf = calculateBf(elliposidType, x);
        double ee2 = (Math.pow(longAxisa, 2.0) - Math.pow(shortAxisb, 2.0))
                / Math.pow(shortAxisb, 2.0);
        double V = Math
                .sqrt(1.0
                        + ((longAxisa * longAxisa - shortAxisb * shortAxisb) / (shortAxisb * shortAxisb))
                        * Math.pow(Math.cos(Bf), 2.0));

        double η2 = ee2 * Math.pow(Math.cos(Bf), 2.0);
        double N = longAxisa / Math.sqrt(1 - ee * Math.pow(Math.sin(Bf), 2.0));
        double t = Math.tan(Bf);
        double lat = Bf - V * V * t * (Math.pow((y / N), 2.0)
                - (5.0 + 3.0 * t * t + η2 - 9.0 * η2 * t * t)
                * Math.pow((y / N), 4.0)
                / 12.0
                + (61.0 + 90.0 * t * t + 45.0 * Math.pow(t, 4.0) + 46.0
                * η2 - 252.0 * η2 * Math.pow(t, 2.0) - 90.0
                * η2 * Math.pow(t, 4.0)) * Math.pow((y / N), 6)
                / 360.0 + (1385.0 + 7266.0 * Math.pow(t, 2.0) + 10920.0
                * Math.pow(t, 4.0) + 5040.0 * Math.pow(t, 6.0))
                * Math.pow((y / N), 8.0) / 20200.0) / 2.0;

        double lng = ((y / N)
                - (1.0 + 2.0 * t * t + η2)
                * Math.pow((y / N), 3.0)
                / 6.0
                + (5.0 + 28.0 * t * t + 24.0 * Math.pow(t, 4.0) + 6.0 * η2 + 8.0 * η2
                * t * t) * Math.pow((y / N), 5.0) / 120.0 - (61.0 + 662.0
                * Math.pow(t, 2.0) + 1320.0 * Math.pow(t, 4.0) + 720.0 * Math
                .pow(t, 6.0)) * Math.pow((y / N), 7.0) / 5040.0)
                / Math.cos(Bf);

        double[] result = new double[2];
        result[0] = lng;
        result[1] = lat;

        return result;
    }

    /**
     * 高斯投影中反算中，计算底点纬度的。
     *
     * @param ellipsoidType 椭球类型，类型为整数类型，1 代表WGS-84椭球；2代表北京54坐标 椭球；3代表西安80坐标椭球；4
     *                      代表国家2000坐标椭球；
     * @param x             高斯平面直角坐标系中的北坐标X；
     *                      <p>
     *                      return 返回底点纬度的弧度值，计算精度优于 0.000000000000001弧度，
     *                      满足误差小于0.0001m，足够工程上应用；
     */
    public static double calculateBf(int ellipsoidType, double x) {
        EllipsoidParam ellipsoidParam = EllipsoidParams(ellipsoidType);
        double longAxisa = ellipsoidParam.getA();
        double shortAxisb = ellipsoidParam.getB();
        double ee = (longAxisa * longAxisa - shortAxisb * shortAxisb)
                / (longAxisa * longAxisa);
        double A = 1.0 + 3.0 * ee / 4.0 + 45.0 * Math.pow(ee, 2.0) / 64.0
                + 175.0 * Math.pow(ee, 3.0) / 256.0 + 11025.0
                * Math.pow(ee, 4.0) / 16384.0 + 43659.0 * Math.pow(ee, 5.0)
                / 65536.0;

        double B = 3.0 * ee / 4.0 + 15.0 * Math.pow(ee, 2.0) / 16.0 + 525.0
                * Math.pow(ee, 3.0) / 512.0 + 2205.0 * Math.pow(ee, 4.0)
                / 2048.0 + 72765.0 * Math.pow(ee, 5.0) / 65536.0;

        double C = 15.0 * Math.pow(ee, 2.0) / 64.0 + 105.0 * Math.pow(ee, 3.0)
                / 256.0 + 2205.0 * Math.pow(ee, 4.0) / 4096.0 + 10395.0
                * Math.pow(ee, 5.0) / 16384.0;

        double D = 35.0 * Math.pow(ee, 3.0) / 512.0 + 315 * Math.pow(ee, 4.0)
                / 2048.0 + 31185.0 * Math.pow(ee, 5.0) / 131072.0;
        double E = 315.0 * Math.pow(ee, 4.0) / 16384.0 + 3465.0
                * Math.pow(ee, 5.0) / 65536.0;

        double F = 693.0 * Math.pow(ee, 5.0) / 131072.0;

        // 迭代法求Bf
        double Bf = x / (longAxisa * (1.0 - ee) * A);
        double X1 = Bf;
        do {
            X1 = Bf;
            Bf = x
                    / (longAxisa * (1.0 - ee) * A)
                    + (B * Math.sin(2.0 * Bf) / 2.0 - C * Math.sin(4.0 * Bf)
                    / 4.0 + D * Math.sin(6.0 * Bf) / 6.0 - E
                    * Math.sin(8.0 * Bf) / 8.0 + F
                    * Math.sin(10.0 * Bf) / 10.0) / A;

        } while (Math.abs(Bf - X1) >= 0.000000000000001);

        return Bf;
    }

    /**
     * 计算中央子午线弧长。
     *
     * @param ellipsoidType 椭球类型，类型为整数类型，1 代表WGS-84椭球；2代表北京54坐标 椭球；3代表西安80坐标椭球；4
     *                      代表国家2000坐标椭球；
     * @param lng           大地坐标系中纬度值，单位为弧度；
     * @return 返回中央子午线弧长，单位为m，误差小于0.0001m；
     */
    public static double centerMeridian(Integer ellipsoidType, double lng) {
        EllipsoidParam ellipsoidParam = EllipsoidParams(ellipsoidType);
        double a = ellipsoidParam.getA();
        double b = ellipsoidParam.getB();
        double ee = (Math.pow(a, 2.0) - Math.pow(b, 2.0)) / Math.pow(a, 2.0);

        double A0 = (1.0 + (3.0 * ee / 4.0) + 45.0 * Math.pow(ee, 2.0) / 64.0
                + 175.0 * Math.pow(ee, 3.0) / 256.0 + 11025.0
                * Math.pow(ee, 4.0) / 16384.0 + 43659.0 * Math.pow(ee, 5.0) / 65536.0)
                * a * (1.0 - ee);

        double A1 = (3.0 * ee / 4.0 + 15.0 * Math.pow(ee, 2.0) / 16.0 + 525.0
                * Math.pow(ee, 3.0) / 512.0 + 2205.0 * Math.pow(ee, 4.0) / 2048 + 72765.0 * Math
                .pow(ee, 5.0) / 65536.0) * a * (1.0 - ee) / 2.0;

        double A2 = (15.0 * Math.pow(ee, 2.0) / 64.0 + 105.0
                * Math.pow(ee, 3.0) / 256.0 + 2205.0 * Math.pow(ee, 4.0) / 4096 + 10395.0 * Math
                .pow(ee, 5.0) / 16384.0) * a * (1.0 - ee) / 4.0;

        double A3 = (35.0 * Math.pow(ee, 3.0) / 512.0 + 315.0
                * Math.pow(ee, 4.0) / 2048 + 31185.0 * Math.pow(ee, 5.0) / 131072.0)
                * a * (1.0 - ee) / 6.0;

        double A4 = (315.0 * Math.pow(ee, 4.0) / 16384 + 3465.0 * Math.pow(ee,
                5.0) / 65536.0) * a * (1.0 - ee) / 8.0;

        double A5 = (693.0 * Math.pow(ee, 5.0) / 131072.0) * a * (1.0 - ee)
                / 10.0;

        double centerLine = A0 * lng - A1 * Math.sin(2.0 * lng) + A2
                * Math.sin(4.0 * lng) - A3 * Math.sin(6.0 * lng) + A4
                * Math.sin(8.0 * lng) - A5 * Math.sin(10.0 * lng);

        return centerLine;
    }

    /**
     * 获取椭球参数
     *
     * @param type 椭球类型，类型为整数类型，1 代表WGS-84椭球；2代表北京54坐标 椭球；3代表西安80坐标椭球；4
     *             代表国家2000坐标椭球；
     * @return 返回对应的椭球参数{JsonObject}数据，包含 椭球长半轴长a，短半轴长度b，扁率f；
     */
    static EllipsoidParam EllipsoidParams(int type) {

        EllipsoidParam result = new EllipsoidParam(6378137, 6356752.31424518, 0.00335281066474748);

        switch (type) {
            case 1: {
                result = new EllipsoidParam(6378137, 6356752.31424518, 0.00335281066474748);
                break;
            }

            case 2: {
                result = new EllipsoidParam(6378245, 6356863.018773, 0.00335232986926658);
                break;
            }

            case 3: {
                result = new EllipsoidParam(6378140, 6356755.2881575, 0.335275798326966);
                break;
            }

            case 4: {
                result = new EllipsoidParam(6378137, 6356752.31414036, 0.00335281068118232);
                break;
            }
        }
        return result;
    }

}
