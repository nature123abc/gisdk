package com.dk.microgis.line.curve.cruve1.entity;

import com.dk.microgis.base.Point2D;
import com.dk.microgis.line.curve.curve.common.entity.PointType;


/**
 * @author hq
 * @date 2021-09-06 16:33
 * @desc 一个曲线要素初始化点, 有这个点信息，就能计算其他焦点信息
 */
public class BaseJdPoint {
    public String name;//点名
    public Double x;//x坐标
    public Double y;//y坐标
    public Double dk;//里程贯通里程
    public Double r;//圆曲线半径
    public Double ls1;//缓曲线长
    public Double ls2;//缓曲线长
    public Double azimuth;//切线方位角
    public PointType pointType;//交点类型


    public Object remark;//备注
    public Double maxh;//曲线的最大超高
    public Double endH;//直线顺超高长度起端
    public Double startH;//


    public BaseJdPoint(String name, Double x, Double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public BaseJdPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public BaseJdPoint(String name, Double x, Double y, Double r, Double ls1, Double ls2) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.r = r;
        this.ls1 = ls1;
        this.ls2 = ls2;
    }

    public BaseJdPoint() {
    }

    public Point2D toPoint2D() {
        return new Point2D(this.name, this.x, this.y);
    }

    public BaseJdPoint(BaseJdPoint baseJdPoint) {
        this.name = baseJdPoint.name;//点名
        this.x = baseJdPoint.x;//点名
        this.y = baseJdPoint.y;
        this.dk = baseJdPoint.dk;
        this.r = baseJdPoint.r;
        this.ls1 = null == baseJdPoint.ls1 ? 0.0 : baseJdPoint.ls1;
        this.ls2 = null == baseJdPoint.ls2 ? 0.0 : baseJdPoint.ls2;
        this.azimuth = baseJdPoint.azimuth;
        this.pointType = baseJdPoint.pointType;
        this.remark = baseJdPoint.remark;
        this.maxh = baseJdPoint.maxh;
        this.endH = baseJdPoint.endH;
        this.startH = baseJdPoint.startH;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getDk() {
        return dk;
    }

    public void setDk(Double dk) {
        this.dk = dk;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getLs1() {
        return ls1;
    }

    public void setLs1(Double ls1) {
        this.ls1 = ls1;
    }

    public Double getLs2() {
        return ls2;
    }

    public void setLs2(Double ls2) {
        this.ls2 = ls2;
    }

    public Double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Double azimuth) {
        this.azimuth = azimuth;
    }

    public PointType getPointType() {
        return pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Double getMaxh() {
        return maxh;
    }

    public void setMaxh(Double maxh) {
        this.maxh = maxh;
    }

    public Double getEndH() {
        return endH;
    }

    public void setEndH(Double endH) {
        this.endH = endH;
    }


    public Double getStartH() {
        return startH;
    }

    public void setStartH(Double startH) {
        this.startH = startH;
    }
}
