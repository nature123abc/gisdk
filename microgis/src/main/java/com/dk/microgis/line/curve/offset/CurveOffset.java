package com.dk.microgis.line.curve.offset;


import com.dk.microgis.base.Point2D;
import com.dk.microgis.line.curve.curve.CrossoverPoint2Stake;
import com.dk.microgis.line.curve.curve.common.CompleteCrossoverPoint;
import com.dk.microgis.line.curve.curve.common.CrossoverPoint;
import com.dk.microgis.line.curve.curve.common.CurveMetaInfo;
import com.dk.microgis.line.curve.curve.common.StakeInfo;
import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.math.Angle;
import com.dk.microgis.math.BaseAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hq
 * @date 2021-09-02 9:01
 * @desc 平移曲线要素, 已知上行计算下行或已知下行计算上行，只能计算等间距曲线
 */
public class CurveOffset<T extends CrossoverPoint> {
    List<T> baseCorss;//基础交点信息
    List<CrossoverPoint> offsetCorss;//偏移后曲线要素
    Integer leftOrRight = -1;//左偏是-1
    Double offsetDis;//平移距离

    public CurveOffset(List<T> baseCorss, Double offsetDis, Integer leftOrRight) {
        this.baseCorss = baseCorss;
        this.offsetDis = offsetDis;
        this.leftOrRight = leftOrRight;
        init2();
    }

    private void init2() {
        offsetCorss = new ArrayList<>();
        if (baseCorss.size() == 2) {//直线
            T baseStart = baseCorss.get(0);
            T baseEnd = baseCorss.get(baseCorss.size() - 1);
            Point2D qd = new Point2D(baseStart.x, baseStart.y);
            Point2D zd = new Point2D(baseEnd.x, baseEnd.y);

            double ang = BaseAlgorithms.computAzimuthByTwoPoint(qd, zd);
            ang = computStartTurnAng(ang);

            Point2D baseNewQD = computOffsetPoint(qd, ang);
            Point2D baseNewZD = computOffsetPoint(zd, ang);


            CrossoverPoint pointStart = obtCrossPoint(baseStart, baseNewQD);
            pointStart.continuDk = baseStart.continuDk;

            CrossoverPoint pointEnd = obtCrossPoint(baseEnd, baseNewZD);
            offsetCorss.add(pointStart);
            offsetCorss.add(pointEnd);
            return;
        }


        for (int i = 1; i < baseCorss.size() - 1; i++) {
            T metaInfoLast = baseCorss.get(i - 1);
            T metaInfo = baseCorss.get(i);
            T metaInfoNext = baseCorss.get(i + 1);

            Point2D start = new Point2D(metaInfoLast.x, metaInfoLast.y);
            Point2D current = new Point2D(metaInfo.x, metaInfo.y);
            Point2D end = new Point2D(metaInfoNext.x, metaInfoNext.y);

            double angel1 = BaseAlgorithms.computAzimuthByTwoPoint(start, current);
            angel1 = computStartTurnAng(angel1);
            double angel2 = BaseAlgorithms.computAzimuthByTwoPoint(current, end);
            angel2 = computStartTurnAng(angel2);

            Point2D startOff = computOffsetPoint(start, angel1);
            Point2D currentOff = computOffsetPoint(current, angel1);
            Point2D currentOff2 = computOffsetPoint(current, angel2);
            Point2D endOff = computOffsetPoint(end, angel2);


            List<Point2D> start2Cur = Arrays.asList(startOff, currentOff);
            List<Point2D> cur2End = Arrays.asList(currentOff2, endOff);


            Point2D jd = BaseAlgorithms.computIntersection(start2Cur, cur2End);

            //计算偏向
            Orientation orientation = computDirect(start, current, end);

            //左移右偏半径增大，左移动，左偏半径减小
            double orgR = metaInfo.radius;
            if (Orientation.左.equals(orientation)) {
                orgR += offsetDis * leftOrRight;
            } else {//右偏左移增大，右偏右一定减小
                orgR += offsetDis * (leftOrRight * -1);
            }

            if (i == 1) {
                offsetCorss.add(obtCrossPoint(metaInfoLast, startOff));
            }
            offsetCorss.add(obtCrossPoint(metaInfo, jd, orgR));
            if (i == baseCorss.size() - 2) {
                offsetCorss.add(obtCrossPoint(metaInfoNext, endOff));
            }
        }
    }


    public static Orientation computDirect(Point2D start, Point2D current, Point2D end) {
        double angle2Current = BaseAlgorithms.computAzimuthByTwoPoint(start, current);
        double angle2End = BaseAlgorithms.computAzimuthByTwoPoint(current, end);

        double detlAng = angle2End + (Angle.PI_DOUBLE - angle2Current);
        detlAng = Angle.formAngle(detlAng);

        //测定线路前进方向的右角为β，当β小于180°，为有偏角，当β大于180则为左偏角。
        Orientation or = Orientation.左;
        if (detlAng >= 0.0 && detlAng <= Angle.PI) {
            or = Orientation.右;
        } else if (detlAng >= Angle.PI && detlAng <= Angle.PI_DOUBLE) {
            or = Orientation.左;
        }
        return or;
    }

    private CrossoverPoint obtCrossPoint(T baseP, Point2D baseNewStart, Double r) {
        CrossoverPoint pointStart = new CrossoverPoint();
        pointStart.name = baseP.name;
        pointStart.remark = baseP.remark;
        pointStart.curveL1 = baseP.curveL1;
        pointStart.curveL2 = baseP.curveL2;
        pointStart.maxh = baseP.maxh;
        pointStart.startH = baseP.startH;
        pointStart.endH = baseP.endH;
        pointStart.radius = r;
        pointStart.continuDk = baseP.continuDk;

        pointStart.x = baseNewStart.x;
        pointStart.y = baseNewStart.y;
        return pointStart;
    }

    private CrossoverPoint obtCrossPoint(T baseP, Point2D baseNewStart) {
        return obtCrossPoint(baseP, baseNewStart, null);
    }

    private Point2D computOffsetPoint(Point2D baseP, double ang) {
        return BaseAlgorithms.computPointByDisAndAzimuth(baseP, offsetDis, Angle.formAngle(ang));
    }

    private double computStartTurnAng(double base) {
        return base + Angle.PI_HALF * leftOrRight;
    }

    public List<CrossoverPoint> getOffsetCorss() {
        return offsetCorss;
    }
}
