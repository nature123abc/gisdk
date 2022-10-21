package com.dk.microgis.base;


import com.dk.common.DoubleUtils;

/**
 * @author hq
 * @date 2020-12-23 9:38
 * @desc
 */
public class Point3D extends Point2D {
    public Double z;

    public Point3D(String name, Double x, Double y, Double z) {
        super(name, x, y);
        this.z = z;
    }

    public Point3D(String name, Double x, Double y) {
        super(name, x, y);
        this.z = z;
    }

    public Point3D(Double x, Double y, Double z) {
        super(x, y);
        this.z = z;
    }

    public Point3D(Point2D point2D, Double z) {
        super(point2D);
        this.z = z;
    }

    public Point3D(Point3D point3D) {
        super(point3D);
        this.z = point3D.getZ();
    }

    public Point3D() {
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }


    public boolean isSamePoint(Point3D point3D) {
        boolean isS = super.isSamePoint(point3D);
        if (isS) {
            boolean b4 = true;
            if (null != point3D.getZ()) {
                b4 = DoubleUtils.deMquals(point3D.getZ(), this.getZ());
            }
            return b4;
        }
        return isS;
    }

}
