package com.dk.microgis.base;


import com.dk.common.DoubleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2020-11-06 15:06
 * @desc CPiii基本信息
 */

public class Point4D extends Point3D {
    public Double continuDk;
    public Double sceneDk;

    public Point4D(Double x, Double y, Double z, Double continuDk) {
        super(x, y, z);
        this.continuDk = continuDk;
    }

    public Point4D() {
    }

    public Point4D(Point3D point3D) {
        super(point3D);
    }

    public Point4D(Point3D point3D, Double continueDk) {
        super(point3D);
        this.continuDk = continueDk;
    }

    public Point4D(Point4D point4D) {
        this(point4D, point4D.getContinuDk());
        this.sceneDk = point4D.getSceneDk();
    }


    public static <P extends Point2D> String point4d2xy(List<Point4D> point4DS) {
        StringBuilder sb = new StringBuilder();
        for (Point4D p : point4DS) {
            sb.append(point4d2xy(p));
            sb.append("\r\n");
        }
        return sb.toString();
    }

    private static <P extends Point2D> String point4d2xy(P p) {
        StringBuilder sb = new StringBuilder();
        sb.append(DoubleUtils.getDemi(p.x, 4));
        sb.append(",");
        sb.append(DoubleUtils.getDemi(p.y, 4));
        return sb.toString();
    }


    public Double getContinuDk() {
        return continuDk;
    }

    public void setContinuDk(Double continuDk) {
        this.continuDk = continuDk;
    }


    public Double getSceneDk() {
        return sceneDk;
    }

    public void setSceneDk(Double sceneDk) {
        this.sceneDk = sceneDk;
    }

    public static Point4D convert3DTo4D(Point3D point3D) {
        return new Point4D(point3D, null);
    }

    public static List<Point4D> convert3DTo4D(List<Point3D> point3Ds) {
        List<Point4D> point4DS = new ArrayList<>();
        for (Point3D p : point3Ds) {
            point4DS.add(convert3DTo4D(p));
        }
        return point4DS;
    }

    public static <T extends Point4D> String point4d2dkxyz(List<T> point4DS) {
        StringBuilder sb = new StringBuilder();
        for (T p : point4DS) {
            sb.append(point4d2dkxyz(p));
            sb.append("\r\n");
        }
        return sb.toString();
    }

    private static <P extends Point4D> String point4d2dkxyz(P p) {
        StringBuilder sb = new StringBuilder();
        sb.append(DoubleUtils.getDemi(p.continuDk, 4));
        sb.append(",");
        sb.append(DoubleUtils.getDemi(p.x, 4));
        sb.append(",");
        sb.append(DoubleUtils.getDemi(p.y, 4));
        sb.append(",");
        sb.append(DoubleUtils.getDemi(p.z, 4));
        return sb.toString();
    }
}
