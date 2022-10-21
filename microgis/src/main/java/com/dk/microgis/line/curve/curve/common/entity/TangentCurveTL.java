package com.dk.microgis.line.curve.curve.common.entity;

/**
 * Created by hq on 2017/11/22.
 * 切线和曲线基本信息  http://www.360doc.com/document/12/0816/10/10593573_230445493.shtml
 */

public class TangentCurveTL {
    /**
     * 缓和曲线角(弧度)  l/2r  缓和曲线长度/2r
     * 缓和曲线上任一点P处的切线与过起点切线的交角β称为切线角，β值与缓和曲线上该点的曲线长（弧长）所对的圆心角相等。
     */
    public double b; //缓和曲线角：缓和曲线段对应圆心角度值  l/2r  缓和曲线长度/2r
    /**
     * 缓和曲内移距  圆曲线切线和缓和曲线切线间距离 p=l*l/24r
     */
    public double p;
    /**
     * 切线增长（ZH点到缓和曲线转角起点距离） l/2 - (l*l*l)/(240*r*r)
     */
    public double q;
    /**
     * 曲线切线长T  ZH点到交点距离
     */
    public double t;
    /**
     * 曲线长L  圆曲线和缓和曲线长,只计算到曲中点
     */
    public double l;
    /**
     * 外矢距  曲线中点到曲线切线交点的距离
     */
    public double e; //


}
