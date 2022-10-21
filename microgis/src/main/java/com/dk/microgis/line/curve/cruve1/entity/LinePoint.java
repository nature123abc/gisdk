package com.dk.microgis.line.curve.cruve1.entity;

import com.dk.microgis.base.Point4D;

/**
 * @author hq
 * @date 2021-10-28 15:15
 * @desc
 */
public class LinePoint extends Point4D {
    public Double azimuth;//切线方位角
    public Double offDis;//距离中心线距离
    public Double offAng;//偏角
    public Double superH;//超高

    public LinePoint() {
    }


    public LinePoint(Double x, Double y, Double z, Double continuDk) {
        super(x, y, z, continuDk);
    }


}
