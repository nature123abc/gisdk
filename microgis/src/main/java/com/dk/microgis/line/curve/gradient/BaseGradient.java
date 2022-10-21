package com.dk.microgis.line.curve.gradient;


/**
 * @author hq
 * @date 2021-04-28 18:12
 * @desc 基础竖曲线表
 */

public class BaseGradient {
    public Integer order;//编号
    public Double scenceDk;//现场里程
    public Double continuDk;//贯通里程
    public Double slopeLen;//坡长
    public Double pointH;//变坡点标高
    public Double radius;// 竖曲线半径

    public BaseGradient(BaseGradient gradient) {
        this.order = gradient.order;
        this.scenceDk = gradient.scenceDk;
        this.continuDk = gradient.continuDk;
        this.slopeLen = gradient.slopeLen;
        this.pointH = gradient.pointH;
        this.radius = gradient.radius;
    }

    public BaseGradient() {
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Double getScenceDk() {
        return scenceDk;
    }

    public void setScenceDk(Double scenceDk) {
        this.scenceDk = scenceDk;
    }

    public Double getContinuDk() {
        return continuDk;
    }

    public void setContinuDk(Double continuDk) {
        this.continuDk = continuDk;
    }

    public Double getSlopeLen() {
        return slopeLen;
    }

    public void setSlopeLen(Double slopeLen) {
        this.slopeLen = slopeLen;
    }

    public Double getPointH() {
        return pointH;
    }

    public void setPointH(Double pointH) {
        this.pointH = pointH;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
