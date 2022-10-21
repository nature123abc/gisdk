package com.dk.microgis.line.curve.cruve1.comm;

import com.dk.microgis.base.Point2D;
import com.dk.microgis.line.curve.cruve1.entity.BaseJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.LinePoint;
import com.dk.microgis.line.curve.cruve1.entity.RoadElement;
import com.dk.microgis.math.BaseAlgorithms;


import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-10-28 15:13
 * @desc 根据曲线元里程坐标互算
 */
public class RoadEle2XyDk<P extends Point2D> {
    List<RoadElement> roadElementList;//线路曲线要素信息

    public RoadEle2XyDk(List<RoadElement> roadElementList) {
        this.roadElementList = roadElementList;
    }


    /**
     * 根据里程找完整曲线元，
     *
     * @param continDk
     * @return
     */
    public RoadElement findElement(double continDk) {
        for (RoadElement e : roadElementList) {
            if (e.dkInElement(continDk)) {
                return e;
            }
        }
        RoadElement first = roadElementList.get(0);
        RoadElement last = roadElementList.get(roadElementList.size() - 1);
        double detl0 = continDk - first.startP.dk;

        double detl1 = continDk - last.endP.dk;
        if (Math.abs(detl0) < Math.abs(detl1)) {
            return first;
        }
        return last;
    }


    private RoadElement findElement(Point2D point2D) {
        for (RoadElement e : roadElementList) {
            if (e.pointInElement(point2D)) {
                return e;
            }
        }
        RoadElement first = roadElementList.get(0);
        RoadElement last = roadElementList.get(roadElementList.size() - 1);
        double detl2First = BaseAlgorithms.computTwoPointDistance(first.startP.toPoint2D(), point2D);
        double delt2Last = BaseAlgorithms.computTwoPointDistance(last.endP.toPoint2D(), point2D);

        if (detl2First < delt2Last) {
            return first;
        }
        return last;
    }


    /**
     * 里程计算坐标
     *
     * @param continDk
     * @return
     */
    public LinePoint dk2Xy(double continDk) {
        RoadElement e = findElement(continDk);
        return e.dkComputXy(continDk);
    }

    public LinePoint dk2Xy(double dk, double offDis, double offAng) {
        RoadElement e = findElement(dk);
        return e.dkComputXy(dk, offDis, offAng);
    }

    public LinePoint dk2Xy(double dk, double offDis) {
        RoadElement e = findElement(dk);
        return e.dkComputXy(dk, offDis);
    }

    public List<LinePoint> dk2Xy(List<Double> continDks) {
        List<LinePoint> linePoints = new ArrayList<>();
        for (Double c : continDks) {
            linePoints.add(dk2Xy(c));
        }
        return linePoints;
    }

    /**
     * 坐标计算里程
     *
     * @param point2D
     * @return
     */
    public LinePoint xy2Dk(P point2D) {
        RoadElement e = findElement(point2D);
        return e.xyComputDK(point2D);
    }

    public List<LinePoint> xy2Dk(List<P> point2Ds) {
        List<LinePoint> linePoints = new ArrayList<>();
        for (P p : point2Ds) {
            linePoints.add(xy2Dk(p));
        }
        return linePoints;
    }

}
