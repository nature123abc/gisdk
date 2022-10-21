package com.dk.microgis.line.point;


import com.dk.microgis.base.Point2D;
import com.dk.microgis.math.BaseAlgorithms;

import java.util.List;

/**
 * @author hq
 * @date 2021-08-10 10:10
 * @desc 点间距
 */

public class PointSpacing<T extends Point2D> {
    List<T> linePoints;
    double sumDis;
    double detlDis;


    public PointSpacing(List<T> linePoints) {
        this.linePoints = linePoints;
        init1();
    }

    private void init1() {
        sumDis = 0.0;
        if (null ==linePoints || linePoints.size() < 2) {
            return;
        }

        for (int i = 1; i < linePoints.size(); i++) {
            sumDis += BaseAlgorithms.computTwoPointDistance(linePoints.get(i - 1), linePoints.get(i));
        }
        detlDis = sumDis / (linePoints.size() - 1);
    }


    public double getSumDis() {
        return sumDis;
    }

    public double getDetlDis() {
        return detlDis;
    }
}
