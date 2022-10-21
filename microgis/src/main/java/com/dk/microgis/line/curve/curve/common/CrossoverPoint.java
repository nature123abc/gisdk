package com.dk.microgis.line.curve.curve.common;


import com.dk.microgis.base.Point3D;
import com.dk.microgis.base.Point4D;

/**
 * @author hq
 * @date 2021-04-16 15:50
 * @desc 曲线设计基本要素，交点基本信息，传入的基本型
 * @Data
 */

public class CrossoverPoint extends Point4D {


    /**
     * 点的类型(起点=0：交点=1：终点=2)
     */
    // public PointType pointType;
    /**
     * 点里程
     */
    // public Double continuDk;
    /**
     * 点X坐标
     */
    // public Double x;
    /**
     * 点Y坐标
     */
    // public Double y;
    /**
     * 曲线半径(R)
     */
    public Double radius;
    /**
     * 第一缓和曲线长(m)
     */
    public Double curveL1;
    /**
     * 第二缓和曲线长(m)
     */
    public Double curveL2;

    /**
     * 曲线的最大超高
     */
    public Double maxh;

    /**
     * 直线顺超高长度起端
     */
    public Double startH;
    /**
     * 直线顺超高长度起端
     */
    public Double endH;


    public CrossoverPoint(CrossoverPoint crossoverPoint) {
        super(crossoverPoint);
        this.radius = crossoverPoint.radius;
        this.curveL1 = crossoverPoint.curveL1;
        this.curveL2 = crossoverPoint.curveL2;
        this.maxh = crossoverPoint.maxh;
        this.startH = crossoverPoint.startH;
        this.endH = crossoverPoint.endH;
    }

    public CrossoverPoint(Point3D point3D, Double continueDk, Double radius, Double curveL1, Double curveL2) {
        super(point3D, continueDk);
        this.radius = radius;
        this.curveL1 = curveL1;
        this.curveL2 = curveL2;

    }

    public CrossoverPoint() {
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getCurveL1() {
        return curveL1;
    }

    public void setCurveL1(Double curveL1) {
        this.curveL1 = curveL1;
    }

    public Double getCurveL2() {
        return curveL2;
    }

    public void setCurveL2(Double curveL2) {
        this.curveL2 = curveL2;
    }

    public Double getMaxh() {
        return maxh;
    }

    public void setMaxh(Double maxh) {
        this.maxh = maxh;
    }

    public Double getStartH() {
        return startH;
    }

    public void setStartH(Double startH) {
        this.startH = startH;
    }

    public Double getEndH() {
        return endH;
    }

    public void setEndH(Double endH) {
        this.endH = endH;
    }
}
