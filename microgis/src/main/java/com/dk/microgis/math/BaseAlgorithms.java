/**
 * All rights Reserved,由西安椤析时空软件科技有限公司进行设计
 *
 * @Title: BaseAlgorithms.java
 * @Package com.lxsk.railwayunifyservice.base.util
 * @Description:
 * @author: jason.wang
 * @date: 2019/3/12 12:01
 * @version V1.0
 * @Copyright: 2019 西安椤析时空软件科技有限公司. All rights reserved.
 * 注意：本内容仅限于西安椤析时空软件科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

package com.dk.microgis.math;


import Jama.Matrix;
import com.dk.common.DoubleUtils;
import com.dk.microgis.base.Point2D;
import com.dk.microgis.base.Point3D;
import com.dk.microgis.error.CommonException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 * @ClassName: BaseAlgorithms
 * @Description: 基础算法类
 * @date 2019/3/12
 */

public final class BaseAlgorithms {

    /**
     * 判断圆弧的计算方法顺时针还是逆时针方向 判断规则为:按ABC的次序在圆弧上顺时针移动。如果ABC是逆时针，移动的距离超过一圈.
     * 1.分别求出AO的斜率及A和O的位置关系，求出A角度。这个结果应该是一个[0, 360)的 值（或者说弧度表示0到2PI）； 2.同理求出B和C的角度，它们也应该是一个[0,
     * 360)的值。A\B\C的角度应该各不相同， 否则有两点重合； 3.判断B的角度是否小于A的角度，若小于就给B加360，直到B的角度大于A。 即 0 < B角 - A角 < 360
     * 4.同上，保证C角大于B角。 即 0 < C角 - B角 < 360 5.判断 C角-A角的值。 如果小于360， ABC顺时针；否则ABC逆时针；
     *
     * @param azimuthA 圆心与起点方位角；
     * @param azimuthB 圆心与第二点方位角；
     * @param azimuthC 圆心与第三点方位角； @ return 如果是顺时针，则返回值为1，否则返回值为0；
     */
    public static double computArcDirectionByThreeAzimuth(double azimuthA, double azimuthB, double azimuthC) {

        double result = 0;
        if (azimuthA > azimuthB) {
            azimuthB = azimuthB + 2.0 * Math.PI;
        }
        if (azimuthC < azimuthB) {
            azimuthC = azimuthC + 2.0 * Math.PI;
        }
        if (azimuthC < azimuthB) {
            azimuthC = azimuthC + 2.0 * Math.PI;
        }
        if ((azimuthC - azimuthA) < 2.0 * Math.PI) {
            result = 1;
        }
        return result;
    }


    /**
     * 计算两点间距离
     *
     * @param fPT
     * @param sPT
     * @return
     */
    public static <T extends Point2D> double computTwoPointDistance(T fPT, T sPT) {
        return Math.sqrt(Math.pow(fPT.x - sPT.x, 2.0) + Math.pow(fPT.y - sPT.y, 2.0));
    }

    /**
     * 计算两个点之间的方位角. 方位角只指以X轴为起点，
     * 有角度以正北方设为000°，顺时针转一圈后的角度为360°。
     * 　　　　因此：
     * 　　　　正北方：000°或360°
     * 　　　　正东方：090°
     * 　　　　正南方：180°
     * 　　　　正西方：270°
     *
     * @param startPoint 计算起点,类型为BMaP.Point;
     * @param endPoint   计算终点;类型为BMaP.Point;
     * @return 返回两个点之间的方位角; 弧度
     */
    public static <T extends Point2D, P extends Point2D> double computAzimuthByTwoPoint(T startPoint, P endPoint) {
        double deltX = endPoint.x - startPoint.x;
        double deltY = endPoint.y - startPoint.y;
        return computAzimuth(deltX, deltY);
    }

    public static double computAzimuth(double deltX, double deltY) {
        double azimuth = 0.0;
        if (DoubleUtils.isZero(deltX)) {
            if (deltY > 0.0) {
                azimuth = Math.PI / 2.0;
            } else {
                azimuth = 3.0 * Math.PI / 2.0;
            }
        } else {
            azimuth = Math.atan2(deltY, deltX);//返回角度，-pi 到pi
            if (azimuth < 0.0) {
                azimuth = azimuth + Math.PI * 2.0;
            }
        }
        return azimuth;
    }

    public static double computAzimuthByTwoPoint(double x1, double y1, double x2, double y2) {
        return computAzimuthByTwoPoint(new Point2D(x1, y1), new Point2D(x2, y2));
    }


    /**
     * 判断点是否在直线区间内
     *
     * @param lineP1
     * @param lineP2
     * @param nearLineP
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T extends Point2D, P extends Point2D> boolean pointInSegment(T lineP1, T lineP2, P nearLineP) {

        double start2End = computAzimuthByTwoPoint(lineP1, lineP2);//线路点间方位角

        double start2Near = computAzimuthByTwoPoint(lineP1, nearLineP);//起点到附近点方位角


        double near2End = computAzimuthByTwoPoint(nearLineP, lineP2);//附近点到终点方位角


        double detl1 = start2End - start2Near;
        double detl2 = start2End - near2End;

        return Math.abs(detl1) <= Angle.PI_HALF && Math.abs(detl2) <= Angle.PI_HALF;

    }


    /**
     * 获取点到直线的距离
     *
     * @param lineP1 线路点1
     * @param lineP2 线路点2
     * @param outP   线路外点
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T extends Point2D, P extends Point2D> double computPointToLineDis(T lineP1, T lineP2, P outP) {

        double angFirst2P2 = computAzimuthByTwoPoint(lineP1, lineP2);//第一个点到第二个点方位角
        double angFirst2P3 = computAzimuthByTwoPoint(lineP1, outP);//第一个点到第二个点方位角

        double dis = computTwoPointDistance(lineP1, outP);
        double detlAng = Math.abs(angFirst2P2 - angFirst2P3);

        double detlDis = dis * Math.sin(Angle.formAngle(detlAng));
        detlDis = Math.abs(detlDis);
        detlDis = computPointLeftRightLine(lineP1, lineP2, outP) * detlDis;
        return detlDis;
    }


    /**
     * 获取点外一点到直线的垂足点
     *
     * @param p1  线路点
     * @param p2  线路点
     * @param p3  求该点的垂足
     * @param <T>
     * @return
     */
    public static <T extends Point2D, P extends Point2D> Point2D computFoot(T p1, T p2, P p3) {

        double angFirst2P2 = computAzimuthByTwoPoint(p1, p2);//第一个点到第二个点方位角
        double angFirst2P3 = computAzimuthByTwoPoint(p1, p3);//第一个点到第二个点方位角

        double dis = computTwoPointDistance(p1, p3);
        double detlAng = Math.abs(angFirst2P2 - angFirst2P3);
        double dis2Foot = dis * Math.cos(Angle.formAngle(detlAng));

        return computPointByDisAndAzimuth(p1, dis2Foot, angFirst2P2);

    }

    /**
     * 计算两条直线交点
     *
     * @param line1 直线上的点，大于等于2个
     * @param line2 直线上的点
     * @param <T>
     * @return 交点坐标
     */
    public static <T extends Point2D> Point2D computIntersection(List<T> line1, List<T> line2) {
        //拟合直线
        LinearFitting linearFit1 = LinearParmsComput.xyComputKb(line1);
        LinearFitting linearFit2 = LinearParmsComput.xyComputKb(line2);

        double k1 = linearFit1.getK();
        double k2 = linearFit2.getK();

        double[][] array = {{1.0, -k1}, {1.0, -k2}};
        double[][] bx = {{linearFit1.getB()}, {linearFit2.getB()}};

        Matrix A = new Matrix(array);//方程组系数矩阵，
        Matrix mtrxL = new Matrix(bx);//曾广矩阵的常数项。
        Matrix res = A.solve(mtrxL);//矩阵求逆 得到单位矩阵

        double y0 = res.get(0, 0);
        double x0 = res.get(1, 0);

        return new Point2D(x0, y0);
    }

    /**
     * 求解p点关于p1，p2点构成方程的对称点
     *
     * @param p1
     * @param p2
     * @param p  要对称的点
     * @return
     */
    public static Point2D computSymmetryPoint(Point2D p1, Point2D p2, Point2D p) {
        double A = p2.getY() - p1.getY();
        double B = p1.getX() - p2.getX();
        double C = p2.getX() * p1.getY() - p1.getX() * p2.getY();

        double x = ((B * B - A * A) * p.x - 2 * A * B * p.y - 2 * A * C) / (A * A + B * B);
        double y = (-2 * A * B * p.x + (A * A - B * B) * p.y - 2 * B * C) / (A * A + B * B);
        return new Point2D(x, y);
    }

    /**
     * 求在直线上的点坐标
     *
     * @param p1     已知点1
     * @param p2     已知点2
     * @param dis2P1 未知点距点1距离,可以正负
     * @return
     */
    public static <T extends Point2D, P extends Point2D> Point3D computOnlinePoint(T p1, P p2, double dis2P1) {


        Double x;
        Double y;
        double azimuth = computAzimuthByTwoPoint(p1, p2);

        x = p1.x + dis2P1 * Math.cos(azimuth);
        y = p1.y + dis2P1 * Math.sin(azimuth);


        Point2D point2D = new Point2D(x, y);
        if (p1 instanceof Point3D && p2 instanceof Point3D) {//如果是三维点，计算高程内插值
            Point3D p11 = ((Point3D) p1);
            Point3D p22 = ((Point3D) p2);

            if (null != p11.z && null != p22.z) {
                double dis = computTwoPointDistance(p1, p2);
                Point2D dkH1 = new Point2D(0.0, p11.z);//以0位置和p1高为第一点
                Point2D dkH2 = new Point2D(dis, p22.z);//以距离第一个点距离为第二个点
                LinearFitting fitting = LinearParmsComput.xyComputKb(Arrays.asList(dkH1, dkH2));

                double h = fitting.k * dis2P1 + fitting.b;
                return new Point3D(point2D, h);
            }
        }

        return new Point3D(point2D, null);
    }

    /**
     * 求在直线上的点坐标
     *
     * @param p1    已知点1
     * @param p2    已知点2
     * @param scale 未知点到已知点1的比例
     * @return
     */
    public static <T extends Point2D, P extends Point2D> Point3D computOnlinePointByScale(T p1, P p2, double scale) {
        double dis2FirstPoint = computTwoPointDistance(p1, p2);
        Point3D point2D = computOnlinePoint(p1, p2, dis2FirstPoint * scale);
        return point2D;
    }


    public static double computTwoPointDistance(double x1, double y1, double x2, double y2) {
        return computTwoPointDistance(new Point2D(x1, y1), new Point2D(x2, y2));
    }

    /**
     * 根据距离和方位角计算点坐标
     *
     * @param standard  顶点
     * @param offsetDis 距离
     * @param ang       方位角
     * @return
     */
    public static Point2D computPointByDisAndAzimuth(Point2D standard, double offsetDis, double ang) {
        double detlX = offsetDis * Math.cos(ang);
        double detlY = offsetDis * Math.sin(ang);
        Point2D point2D = new Point2D(standard.getX() + detlX, standard.getY() + detlY);
        return point2D;
    }


    public static Point2D avgPoint(Point2D p1, Point2D p2) {
        return new Point2D(avg(p1.x, p2.x), avg(p1.y, p2.y));
    }

    public static Point3D avgPoint3D(Point3D p1, Point3D p2) {
        return new Point3D(avgPoint(p1, p2), avg(p1.z, p2.z));
    }

    public static double avg(double x1, double x2) {
        return (x1 + x2) / 2;
    }

    public static <P extends Point3D> Point3D avgPoint3D(List<P> allPoints) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (P p : allPoints) {
            x += p.x;
            y += p.y;
            z += p.z;
        }
        int size = allPoints.size();
        return new Point3D(x / size, y / size, z / size);
    }


    /**
     * 判断线外点，在直线的那一侧
     *
     * @param lineStart
     * @param lineEnd
     * @param outP
     * @param <P>
     * @param <T>
     * @return -1左侧，1右侧，0表示在线路上
     */
    public static <P extends Point2D, T extends Point2D> int computPointLeftRightLine(P lineStart, P lineEnd, T outP) {
        double lineAzimuth = computAzimuthByTwoPoint(lineStart, lineEnd);//线路走向方位角
        double start2OutAzimuthByTwoPoint = computAzimuthByTwoPoint(lineStart, outP);//线路点到线外点方位角

        return computPointLeftRightLine(lineAzimuth, start2OutAzimuthByTwoPoint);
    }

    /**
     * @param lineAzimuth                线路走向方位角
     * @param start2OutAzimuthByTwoPoint 线路点到线外点方位角
     * @return
     */
    public static int computPointLeftRightLine(double lineAzimuth, double start2OutAzimuthByTwoPoint) {
        lineAzimuth = Angle.formAngle(lineAzimuth);
        start2OutAzimuthByTwoPoint = Angle.formAngle(start2OutAzimuthByTwoPoint);
        double detl = lineAzimuth - start2OutAzimuthByTwoPoint;
        if (detl < -Angle.PI) {
            lineAzimuth += Angle.PI_DOUBLE;
        }
        if (detl > Angle.PI) {
            lineAzimuth -= Angle.PI_DOUBLE;
        }
        detl = lineAzimuth - start2OutAzimuthByTwoPoint;
        if (detl < 0.0) {
            return -1;
        }
        if (detl > 0.0) {
            return 1;
        }
        return 0;//表示在线路上
    }


    /**
     * 余弦定理求第三边距离
     *
     * @param disA a
     * @param disB b
     * @param α    ab夹角
     * @return c边长
     */
    public static double cosine2Dis(Double disA, Double disB, Double α) {
        double c2c = Math.pow(disA, 2.0) + Math.pow(disB, 2.0) - 2.0 * disA * disB * Math.cos(α);
        if (c2c < 0.0) {
            throw new CommonException("使用余弦定理计算出错");
        }

        return Math.sqrt(c2c);
    }

    /**
     * @param disA a
     * @param disB b
     * @param disC c
     * @return a夹角
     */
    public static double cosine2Angle(Double disA, Double disB, Double disC) {
        double cosα = (Math.pow(disB, 2.0) + Math.pow(disC, 2.0) - Math.pow(disA, 2.0)) / (2.0 * disB * disC);

        double detl = (Math.abs(cosα) - 1.0);

        if (DoubleUtils.isZero(detl)) {
            throw new CommonException("使用余弦定理计算出错");
        }
        return Math.acos(cosα);
    }


    public static void main(String[] args) {
        double disLong = BaseAlgorithms.cosine2Angle(0.6763220664647633, 587.4297475220133, 586.7534254555954);//计算长边弦长
    }

}

