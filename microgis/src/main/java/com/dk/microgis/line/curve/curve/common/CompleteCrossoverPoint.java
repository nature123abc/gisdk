package com.dk.microgis.line.curve.curve.common;


import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.line.curve.curve.common.entity.TangentCurveTL;

/**
 * @author hq
 * @date 2021-04-16 16:47
 * @desc 完整交点信息
 */

public class CompleteCrossoverPoint extends CrossoverPoint {

    public Integer split;


    public Double zhDk;
    public Double hzDk;
    public Orientation orientation; //曲线偏向(左偏为=-1：右偏为=1)
    public Double angle;//偏角
    public Double curveLen;//曲线全长
    public Double circleLen;//圆曲线长


    public TangentCurveTL curveTL1;//第一缓和曲线
    public TangentCurveTL curveTL2;//第2缓和曲线


    public CompleteCrossoverPoint(CrossoverPoint crossoverPoint) {
        super(crossoverPoint);
    }


    public CompleteCrossoverPoint() {

    }

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    public Double getZhDk() {
        return zhDk;
    }

    public void setZhDk(Double zhDk) {
        this.zhDk = zhDk;
    }

    public Double getHzDk() {
        return hzDk;
    }

    public void setHzDk(Double hzDk) {
        this.hzDk = hzDk;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getCurveLen() {
        return curveLen;
    }

    public void setCurveLen(Double curveLen) {
        this.curveLen = curveLen;
    }

    public Double getCircleLen() {
        return circleLen;
    }

    public void setCircleLen(Double circleLen) {
        this.circleLen = circleLen;
    }

    public TangentCurveTL getCurveTL1() {
        return curveTL1;
    }

    public void setCurveTL1(TangentCurveTL curveTL1) {
        this.curveTL1 = curveTL1;
    }

    public TangentCurveTL getCurveTL2() {
        return curveTL2;
    }

    public void setCurveTL2(TangentCurveTL curveTL2) {
        this.curveTL2 = curveTL2;
    }
}
