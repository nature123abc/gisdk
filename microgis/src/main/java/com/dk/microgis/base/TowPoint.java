package com.dk.microgis.base;


import com.dk.microgis.math.BaseAlgorithms;

/**
 * @author hq
 * @date 2021-06-03 18:58
 * @desc
 */

public class TowPoint<T extends Point2D,K extends Point2D> {
    public T basePoint;
    public K p2;
    public Double detl2Ps;

    public TowPoint(T basePoint, K p2) {
        this.basePoint = basePoint;
        this.p2 = p2;
        detl2Ps = BaseAlgorithms.computTwoPointDistance(basePoint, p2);
    }
    public TowPoint( ) {
    }



}
