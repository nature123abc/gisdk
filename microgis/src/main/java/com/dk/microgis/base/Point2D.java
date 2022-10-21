package com.dk.microgis.base;


import com.dk.common.DoubleUtils;
import com.dk.microgis.error.CommonException;
import com.dk.microgis.line.point.PointSpacing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Point2D implements IPoint {
    public Double x;
    public Double y;
    public String name;
    public Object remark;

    public Point2D(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Integer x, Integer y) {
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }

    public Point2D(String name, Double x, Double y, Object remark) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.remark = remark;
    }

    public Point2D(String name, Double x, Double y) {
        this(name, x, y, null);
    }


    public Point2D(Point2D point2D) {
        this(point2D.name, point2D.x, point2D.y, point2D.remark);
    }

    public Point2D() {

    }


    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    @Override
    public boolean isSamePoint(Point2D point2D) {
        boolean b1 = true;
        boolean b2 = true;
        boolean b3 = true;
        if (null != point2D.getName()) {
            b1 = point2D.getName().equals(this.getName());
        }

        if (null != point2D.getX()) {
            b2 = DoubleUtils.deMquals(point2D.getX(), this.getX());
        }

        if (null != point2D.getY()) {
            b3 = DoubleUtils.deMquals(point2D.getY(), this.getY());
        }
        return b1 && b2 && b3;
    }

    public static <T extends Point2D> T findSamePoint(List<T> list, T p) {
        for (T t : list) {
            if (p.isSamePoint(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T extends Point2D> int findSamePointIndex(List<T> list, Point2D p) {
        int start = list.indexOf(p);
        int end = list.lastIndexOf(p);
        if (start >= 0 && start == end) {
            return start;
        }
        for (int i = 0; i < list.size(); i++) {
            if (p.isSamePoint(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 找线路最近点索引
     *
     * @param list 线路点
     * @param p    目标点
     * @param <T>
     * @return
     */
    public static <T extends Point2D> int findNearstPointIndex(List<T> list, T p) {
        int index = findSamePointIndex(list, p);
        if (index < 0) {//该点不在线路点内，使用最近点定方位角
            T nearPoint = findLineNearPoint(list, p);
            if (null == nearPoint) {
                return -1;
            }
            index = Point2D.findSamePointIndex(list, nearPoint);
        }
        return index;
    }

    /**
     * 找距离当前点最近的线路点
     *
     * @param linePoint 线路点
     * @param current   当前点
     * @return 距离当前点最近的线路点
     */
    private static <T extends Point2D> T findLineNearPoint(List<T> linePoint, T current) {
        List<TowPoint<T, T>> nearTows = new ArrayList<>();
        for (T k : linePoint) {
            nearTows.add(new TowPoint<>(current, k));
        }
        Collections.sort(nearTows, Comparator.comparing(o -> o.detl2Ps));
        if (nearTows.size() > 0) {
            return nearTows.get(0).p2;
        }
        return null;
    }

    public static <T extends Point2D> String xy(T p) {
        return p.x + "," + p.y;
    }

    public static <T extends Point2D> String xy(List<T> ps) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ps.size(); i++) {
            T p = ps.get(i);
            sb.append(xy(p));
            if (i != ps.size() - 1) {
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

    /**
     * 平均点间距
     *
     * @param orderPoint
     * @param <T>
     * @return
     */
    public static <T extends Point2D> double computAvgDetlDis(List<T> orderPoint) {
        PointSpacing spacing = new PointSpacing(orderPoint);
        return spacing.getDetlDis();
    }

    /**
     * 多个点去平均值
     *
     * @param orderPoint
     * @param <T>
     * @return
     */
    public static <T extends Point2D> Point2D avgPoints(List<T> orderPoint) {
        if (null == orderPoint || orderPoint.size() == 0) {
            throw new CommonException("平均点要素为空");
        }
        double sumX = 0.0;
        double sumY = 0.0;
        for (int i = 0; i < orderPoint.size(); i++) {
            sumX += orderPoint.get(i).x;
            sumY += orderPoint.get(i).y;
        }
        return new Point2D(sumX / orderPoint.size(), sumY / orderPoint.size());
    }

}
