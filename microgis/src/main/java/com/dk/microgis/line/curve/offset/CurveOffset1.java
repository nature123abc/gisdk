package com.dk.microgis.line.curve.offset;


import com.dk.microgis.base.Point2D;
import com.dk.microgis.line.curve.cruve1.entity.BaseJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.RoadElement;
import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.math.Angle;
import com.dk.microgis.math.BaseAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hq
 * @date 2021-09-02 9:01
 * 2022.8.修改111，测试后发布
 * @desc 平移曲线要素, 已知上行计算下行或已知下行计算上行，只能计算等间距曲线
 */

public class CurveOffset1 {
    List<RoadElement> baseCorss;//基础交点信息
    List<BaseJdPoint> offsetCorss;//偏移后曲线要素
    Integer leftOrRight = -1;//左偏是-1
    Double offsetDis;//平移距离

    public CurveOffset1(List<RoadElement> baseCorss, Double offsetDis, Integer leftOrRight) {
        this.baseCorss = baseCorss;
        this.offsetDis = offsetDis;
        this.leftOrRight = leftOrRight;
        init2();
    }

    private void init2() {
        offsetCorss = new ArrayList<>();
        if (baseCorss.size() == 2) {//直线
            BaseJdPoint baseStart = baseCorss.get(0).startP;
            BaseJdPoint baseEnd = baseCorss.get(1).endP;
            Point2D qd = new Point2D(baseStart.x, baseStart.y);
            Point2D zd = new Point2D(baseEnd.x, baseEnd.y);

            double ang = BaseAlgorithms.computAzimuthByTwoPoint(qd, zd);//起点到结束点方位角
            ang = computStartTurnAng(ang);

            Point2D baseNewQD = computOffsetPoint(qd, ang);
            Point2D baseNewZD = computOffsetPoint(zd, ang);


            BaseJdPoint pointStart = obtCrossPoint(baseStart, baseNewQD);
            BaseJdPoint pointEnd = obtCrossPoint(baseEnd, baseNewZD);
            offsetCorss.add(pointStart);
            offsetCorss.add(pointEnd);
            return;
        }

        //多个交点情况
        for (int i = 1; i < baseCorss.size() - 1; i++) {
            BaseJdPoint metaInfoLast = baseCorss.get(i).startP;//开始点
            BaseJdPoint metaInfo = baseCorss.get(i).jd;//交点
            BaseJdPoint metaInfoNext = baseCorss.get(i).endP;//HZ点

            Point2D start = new Point2D(metaInfoLast.x, metaInfoLast.y);
            Point2D current = new Point2D(metaInfo.x, metaInfo.y);
            Point2D end = new Point2D(metaInfoNext.x, metaInfoNext.y);

            double angel1 = BaseAlgorithms.computAzimuthByTwoPoint(start, current);
            angel1 = computStartTurnAng(angel1);//起点到JD方位角偏移后方位角
            double angel2 = BaseAlgorithms.computAzimuthByTwoPoint(current, end);
            angel2 = computStartTurnAng(angel2);

            Point2D startOff = computOffsetPoint(start, angel1);
            Point2D currentOff = computOffsetPoint(current, angel1);
            Point2D currentOff2 = computOffsetPoint(current, angel2);
            Point2D endOff = computOffsetPoint(end, angel2);


            List<Point2D> start2Cur = Arrays.asList(startOff, currentOff);
            List<Point2D> cur2End = Arrays.asList(currentOff2, endOff);


            Point2D jd = BaseAlgorithms.computIntersection(start2Cur, cur2End);//新交点坐标

            //计算偏向
            Orientation orientation = computDirect(start, current, end);

            //左移右偏半径增大，左移动，左偏半径减小
            double orgR = metaInfo.r;
            if (Orientation.左.equals(orientation)) {
                orgR += offsetDis * leftOrRight;
            } else {//右偏左移增大，右偏右一定减小
                orgR += offsetDis * (leftOrRight * -1);
            }

            if (i == 1) {//开始点
                offsetCorss.add(obtCrossPoint(metaInfoLast, startOff));
            }
            offsetCorss.add(obtCrossPoint(metaInfo, jd, orgR));
            if (i == baseCorss.size() - 2) {//结束点
                offsetCorss.add(obtCrossPoint(metaInfoNext, endOff));
            }
        }
    }


    public static Orientation computDirect(Point2D start, Point2D jd, Point2D end) {
        double angle2Current = BaseAlgorithms.computAzimuthByTwoPoint(start, jd);
        double angle2End = BaseAlgorithms.computAzimuthByTwoPoint(jd, end);

        double detlAng = angle2End + (Angle.PI_DOUBLE - angle2Current);
        detlAng = Angle.formAngle(detlAng);

        //测定线路前进方向的右角为β，当β小于180°，为右偏角，当β大于180则为左偏角。
        Orientation or = Orientation.左;
        if (detlAng >= 0.0 && detlAng <= Angle.PI) {
            or = Orientation.右;
        } else if (detlAng >= Angle.PI && detlAng <= Angle.PI_DOUBLE) {
            or = Orientation.左;
        }
        return or;
    }

    /**
     * 新建一个交点
     *
     * @param baseP
     * @param baseNewStart
     * @param r
     * @return
     */
    private BaseJdPoint obtCrossPoint(BaseJdPoint baseP, Point2D baseNewStart, Double r) {
        BaseJdPoint pointStart = new BaseJdPoint();
        pointStart.name = baseP.name;
        pointStart.ls1 = baseP.ls1;
        pointStart.ls2 = baseP.ls2;
        pointStart.r = r;
        pointStart.dk = baseP.dk;

        pointStart.x = baseNewStart.x;
        pointStart.y = baseNewStart.y;
        return pointStart;
    }


    private BaseJdPoint obtCrossPoint(BaseJdPoint baseP, Point2D baseNewStart) {
        return obtCrossPoint(baseP, baseNewStart, null);
    }

    /**
     * 按照方位角进行偏移
     *
     * @param baseP
     * @param ang
     * @return
     */
    private Point2D computOffsetPoint(Point2D baseP, double ang) {
        return BaseAlgorithms.computPointByDisAndAzimuth(baseP, offsetDis, Angle.formAngle(ang));
    }

    private double computStartTurnAng(double base) {
        return base + Angle.PI_HALF * leftOrRight;
    }

    public List<BaseJdPoint> getOffsetCorss() {
        return offsetCorss;
    }
}
