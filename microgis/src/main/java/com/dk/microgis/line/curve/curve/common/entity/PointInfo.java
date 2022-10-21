package com.dk.microgis.line.curve.curve.common.entity;

/**
 * Created by hq on 2017/11/22.
 * 曲线上点坐标信息实体类
 */

public class PointInfo  {
    /**
     *点名称
     */
    public String name;               //
    public double dk;
    /**
     *距离设计中线的距离
     */
    public double pj;
    /**
     *点X坐标
     */
    public double x;
    /**
     *点Y坐标
     */
    public double y;
    /**
     *点高程
     */
    public double z;
    /**
     *[左右点指]右边为R，左边为L，中线点指交点偏向＂左偏\右偏＂
     */
    public String direct;
    /**
     *点的切线方位角
     */
    public double tangentAzimuth;


}
