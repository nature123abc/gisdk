package com.dk.microgis.line.curve.curve.common;

import java.util.List;

/**
 * @author hq
 * @date 2021-04-16 16:17
 * @desc 曲线元信息, 一个交点对应一个五大桩，
 */

public class CurveMetaInfo {

    CompleteCrossoverPoint currentPoint;//交点信息


    List<StakeInfo> fiveStakeInfo;//五大桩信息 一般size==5

    public CurveMetaInfo(CompleteCrossoverPoint currentPoint, List<StakeInfo> fiveStakeInfo) {
        this.currentPoint = currentPoint;
        this.fiveStakeInfo = fiveStakeInfo;
    }

    public CompleteCrossoverPoint getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(CompleteCrossoverPoint currentPoint) {
        this.currentPoint = currentPoint;
    }

    public List<StakeInfo> getFiveStakeInfo() {
        return fiveStakeInfo;
    }

    public void setFiveStakeInfo(List<StakeInfo> fiveStakeInfo) {
        this.fiveStakeInfo = fiveStakeInfo;
    }
}
