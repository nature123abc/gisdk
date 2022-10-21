package com.dk.microgis.math;

public class Angle {
    public static final double PI = Math.PI;
    public static final double Row = 180.0 * 60 * 60 / PI;//206264.806247096363
    public static final double PI_DOUBLE = Math.PI * 2;
    public static final double PI_HALF = Math.PI / 2;
    public static final double PI_THREE_SECONDS = Math.PI / 2 * 3;


    public static double DMS2RAD(double parm) {
        return DMS2D(parm) / 180 * Math.PI;
    }

    /**
     * 函数(DchangeDMS)将度化为度分秒"
     *
     * @param X 角度
     * @return 度分秒
     */
    public static double D2DMS(double X) {
        double sgn, d1, d2, d3;
        sgn = Math.signum(X);
        X = Math.abs(X);

        d1 = X - (X % 1);
        d2 = (X - d1) * 60 - ((X - d1) * 60 % 1);
        d3 = (X - d1 - d2 / 60) * 3600;
        return sgn * (d1 + d2 / 100 + d3 / 10000);
    }

    public static double RAD2DMS(double v) {
        return D2DMS(v * 180 / Math.PI);
    }

    /**
     * 函数(DMSchangeD)将分秒化为度"
     *
     * @param X
     * @return
     */
    public static double DMS2D(double X) {
        double sgn, d1, d2, d3;
        sgn = Math.signum(X);
        X = Math.abs(X);
        d1 = X - (X % 1);
        d2 = ((X * 100 - d1 * 100) - ((X * 100 - d1 * 100) % 1)) / 100;
        d3 = X - d1 - d2;
        return sgn * (d1 + d2 / 0.6 + d3 / 0.36);
    }

    /**
     * 弧度转为秒
     *
     * @param v
     * @return
     */
    public static double RAD2SEC(double v) {
        return v * Row;
    }

    public static double SEC2RAD(double v) {
        return v / Row;
    }

    public static double D2RAD(double d) {
        return d / 180 * Math.PI;
    }

    /**
     * 弧度转换位度
     *
     * @param d
     * @return
     */
    public static double RAD2D(double d) {
        return d * 180 / Math.PI;
    }


    public static double formAngle(double rad) {
        rad = rad % PI_DOUBLE;//取余直接转换到正负 2*PI
        if (rad < 0.0) {
            rad += Angle.PI_DOUBLE;
        }
        return rad;
    }

    /**
     * 判断两个方位角是否同方向
     *
     * @param angStart2Point 开始点到目标点方位角
     * @param startEndAng    开始点到结束点方位角
     * @return
     */
    public static boolean sameDirAzimuth(double angStart2Point, double startEndAng) {
        double detl = angStart2Point - startEndAng;
        detl = Angle.formAngle(detl);
        if (detl >= 0 && detl <= Angle.PI_HALF) {
            return true;
        }
        if (detl >= Angle.PI_HALF && detl <= Angle.PI_THREE_SECONDS) {
            return false;
        }
        if (detl >= Angle.PI_THREE_SECONDS && detl <= Angle.PI_DOUBLE) {
            return true;
        }
        return false;
    }

    /**
     * 弧度转化Wie秒
     *
     * @param rad
     * @return
     */
    public static double RAD2MM(double rad) {
        double d = Angle.RAD2D(rad);
        return d * 3600;//转化到秒
    }

    public static double formDetlAng(double detlAngl) {
        if (Math.abs(detlAngl) > Angle.PI * 3 / 2.0) {
            if (detlAngl < Angle.PI) {
                detlAngl += Angle.PI_DOUBLE;
            }
            if (detlAngl > Angle.PI) {
                detlAngl -= Angle.PI_DOUBLE;
            }
        }
        return detlAngl;
    }
}
