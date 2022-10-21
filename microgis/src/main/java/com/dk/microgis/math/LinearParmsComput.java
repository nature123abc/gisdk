package com.dk.microgis.math;


import com.dk.common.DoubleUtils;
import com.dk.microgis.base.Point2D;
import com.dk.microgis.base.Point3D;
import com.dk.microgis.base.Point4D;
import com.dk.microgis.error.CommonException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author hq
 * @date 2021-06-28 9:53
 * @desc 线性参数计算，计算线形参数K和吧B
 */
public class LinearParmsComput<T extends Point2D> {


    /**
     * 根据二维坐标拟合直线，得到直线参数信息,如果是多个点进行拟合，则显示 回归结果
     *
     * @param ps
     * @param <T>
     * @return
     */
    public static <T extends Point2D> LinearFitting xyComputKb(List<T> ps) {
        if (null == ps || ps.size() < 2) {
            throw new CommonException("线形拟合点不能少于两个");
        }

        if (ps.size() == 2) {
            T p1 = ps.get(0);
            T p2 = ps.get(1);
            double detlX = p1.x - p2.x;
            if (DoubleUtils.isZero(detlX)) {
                throw new CommonException("参数斜率不存在");
            }
            double k = (p1.y - p2.y) / detlX;
            double b = p1.y - k * p1.x;

            LinearFitting fitting = new LinearFitting(ps);
            fitting.setK(k);
            fitting.setB(b);
            return fitting;
        }
        LinearFitting linearFitting = new LinearFitting(ps);
        return linearFitting;
    }


    /**
     * 根据线路点，拟合目标点在线路上三维坐标
     *
     * @param nearPoits 参与拟合的线路点
     * @param fitP      拟合目标点
     * @param <P>
     * @param <K>
     * @return
     */
    public static <P extends Point3D, K extends Point3D> Point3D computFitPoint(List<P> nearPoits, K fitP) {

        LinearFitting fitting = xyComputKb(nearPoits);
        double k = fitting.getK();
        double b = fitting.getB();


        List<Point4D> linePoint = new ArrayList<>();//坐标点已经拟合到直线上
        if (Math.abs(k) < DoubleUtils.MIN_VALUE) {
            k += DoubleUtils.MIN_VALUE;
        }
        for (P p : nearPoits) {
            Point4D point3D = new Point4D(p.x, k * p.x + b, p.z, null);
            linePoint.add(point3D);
        }
        //计算目标点到线路上
        Point4D target = new Point4D(fitP.x, k * fitP.x + b, null, null);
        linePoint.add(target);

        //线路点按照Y排序,从小到大，
        Collections.sort(linePoint, Comparator.comparing(o -> o.y));

        Point4D start = null;
        for (int i = 0; i < linePoint.size(); i++) {
            Point4D p = linePoint.get(i);
            if (i == 0) {
                start = p;
                p.continuDk = 0.0;
                continue;
            }
            p.continuDk = BaseAlgorithms.computTwoPointDistance(p, start);
        }

        List<Point2D> dkH = new ArrayList<>();
        for (Point4D p : linePoint) {//找只有Z值和里程的点拟合
            if (null != p.z) {
                dkH.add(new Point2D(p.continuDk, p.z));
            }
        }

        LinearFitting fittingH = LinearParmsComput.xyComputKb(dkH);

        double targetH = fittingH.x2y(target.continuDk);
        target.z = targetH;
        return target;

    }


}
