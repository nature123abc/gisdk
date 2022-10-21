package com.dk.microgis.line.curve.curve.common;


import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.base.Point4D;

/**
 * @author hq
 * @date 2021-04-16 16:11
 * @desc 五大桩一个桩 信息
 */

public class StakeInfo extends Point4D {

    /**
     *起点名称
     */
    public String startPoName;
    /**
     *起点里程
     */
    public Double startPoDk;
    /**
     *下一点里程
     */
    public Double nextPoDk;
    /**
     *起点半径
     */
    public Double startPoRadius;
    /**
     *下一点半径
     */
    public Double nextPoRadius;
    /**
     *起点切线方位角
     */
    public Double startPoAzimuth;
    /**
     *起点X坐标
     */
    public Double startX;
    /**
     *起点Y坐标
     */
    public Double startY;
    /**
     *交点偏向
     */
    public Orientation jdDirection;
    /**
     *曲线的最大超高
     */
    public Double maxGapH;


    public String getStartPoName() {
        return startPoName;
    }

    public void setStartPoName(String startPoName) {
        this.startPoName = startPoName;
    }

    public Double getStartPoDk() {
        return startPoDk;
    }

    public void setStartPoDk(Double startPoDk) {
        this.startPoDk = startPoDk;
    }

    public Double getNextPoDk() {
        return nextPoDk;
    }

    public void setNextPoDk(Double nextPoDk) {
        this.nextPoDk = nextPoDk;
    }

    public Double getStartPoRadius() {
        return startPoRadius;
    }

    public void setStartPoRadius(Double startPoRadius) {
        this.startPoRadius = startPoRadius;
    }

    public Double getNextPoRadius() {
        return nextPoRadius;
    }

    public void setNextPoRadius(Double nextPoRadius) {
        this.nextPoRadius = nextPoRadius;
    }

    public Double getStartPoAzimuth() {
        return startPoAzimuth;
    }

    public void setStartPoAzimuth(Double startPoAzimuth) {
        this.startPoAzimuth = startPoAzimuth;
    }

    public Double getStartX() {
        return startX;
    }

    public void setStartX(Double startX) {
        this.startX = startX;
    }

    public Double getStartY() {
        return startY;
    }

    public void setStartY(Double startY) {
        this.startY = startY;
    }

    public Orientation getJdDirection() {
        return jdDirection;
    }

    public void setJdDirection(Orientation jdDirection) {
        this.jdDirection = jdDirection;
    }

    public Double getMaxGapH() {
        return maxGapH;
    }

    public void setMaxGapH(Double maxGapH) {
        this.maxGapH = maxGapH;
    }
}
