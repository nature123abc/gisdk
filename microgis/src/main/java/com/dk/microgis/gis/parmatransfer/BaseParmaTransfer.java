package com.dk.microgis.gis.parmatransfer;

import Jama.Matrix;
import com.dk.common.DoubleUtils;
import com.dk.microgis.base.Point2D;
import com.dk.microgis.error.CommonException;
import com.dk.microgis.math.BaseAlgorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-10-26 9:16
 * @desc 三参数、四参数参数转换基础类
 */
public abstract class BaseParmaTransfer<T extends Point2D, P extends Point2D> {

    //X坐标增量，单位m  x移动量=
    public Double deltX;

    // Y坐标增量，单位m
    public Double deltY;

    // 旋转角度，单位弧度
    public Double angle;

    // 单位权中误差   测点点位精度
    public Double sigma;

    //改正数带权平方和
    public Double PVV;
    //观测点，独立坐标系下 需要转换的坐标
    public List<P> m_oldCoordLS;
    //CPIiii点 基准，
    public List<T> m_newCoordLS;
    //x方向改正数 符号相反
    public Double[] m_Vx;
    //Y方向改正数
    public Double[] m_Vy;


    protected double avgOldX;
    protected double avgOldY;


    public BaseParmaTransfer() {
    }

    public BaseParmaTransfer(List<P> m_oldCoordLS, List<T> m_newCoordLS) {
        this.m_oldCoordLS = m_oldCoordLS;
        this.m_newCoordLS = m_newCoordLS;

        if (m_oldCoordLS == null || m_oldCoordLS.size() < 2 || m_oldCoordLS.size() != m_newCoordLS.size()) {
            throw new CommonException("转换数据有误，请保证转换已知点不能少于2个");
        }
        computTransferParam();
    }

    /**
     * 根据点，以基准坐标系为基础，重新给老坐标系排序
     *
     * @param base
     * @param old
     * @param <T>
     * @param <P>
     */
    public static <T extends Point2D, P extends Point2D> List<T> orderByName(List<P> base, List<T> old) {
        List<T> ts = new ArrayList<>();
        for (int i = 0; i < base.size(); i++) {
            P p = base.get(i);
            T t = orderByName(p, old);
            ts.add(t);
        }
        return ts;
    }

    public static <T extends Point2D, P extends Point2D> T orderByName(P base, List<T> old) {
        for (int i = 0; i < old.size(); i++) {
            T t = old.get(i);
            if (base.name.equals(t.name)) {
                return t;
            }
        }
        throw new CommonException("两个坐标系中点名称不一致");
    }


    //计算旋转参数信息，
    protected abstract boolean computTransferParam();


    /**
     * 计算已知点转换看得误差，用于修正其他转换点
     */
    protected void computError(double scale) {
        m_Vx = new Double[m_oldCoordLS.size()];
        m_Vy = new Double[m_oldCoordLS.size()];

        PVV = 0.0;
        for (int i = 0; i < m_oldCoordLS.size(); i++) {
            Point2D pt = trantsferCoord(deltX, deltY, scale, angle, m_oldCoordLS.get(i));
            m_Vx[i] = m_newCoordLS.get(i).x - pt.x;
            m_Vy[i] = m_newCoordLS.get(i).y - pt.y;

            //PVV += m_Vx[i] * m_Vx[i] + m_Vy[i] * m_Vy[i];
            PVV += Math.pow(m_Vx[i], 2.0) + Math.pow(m_Vy[i], 2.0);
        }
        sigma = m_newCoordLS.size() <= 2 ? 0.0 : Math.sqrt(PVV / (m_newCoordLS.size() * 2 - 4));
    }


    /**
     * 对转换坐标进行误差改正
     * sumPVx 改正数
     *
     * @param pointCoord
     * @return
     */
    protected AdjPoint2D correctionPoint(Point2D pointCoord) {
        double sumP = 0.0;
        double distance = 0.0;

        double sumPVx = 0.0;
        double sumPVy = 0.0;

        AdjPoint2D adjPoint2D = new AdjPoint2D(pointCoord);
        if (null != m_newCoordLS && m_newCoordLS.size() <= 2) {//旋转点少于2个不需要修正
            return adjPoint2D;
        }

        for (int i = 0; i < m_newCoordLS.size(); i++) {
            distance = BaseAlgorithms.computTwoPointDistance(pointCoord, m_newCoordLS.get(i));
            //distance = Math.sqrt(Math.pow(pointCoord.x - m_newCoordLS.get(i).x, 2.0) + Math.pow(pointCoord.y - m_newCoordLS.get(i).y, 2.0));
            sumP += 1.0 / Math.pow(distance, 2.0);
            sumPVx += m_Vx[i] * (1.0 / Math.pow(distance, 2.0));
            sumPVy += m_Vy[i] * (1.0 / Math.pow(distance, 2.0));
        }

        if (!DoubleUtils.isZero(sumP)) {
            double vX = sumPVx / sumP;
            double vY = sumPVy / sumP;

            adjPoint2D.remark = "Vx:" + DoubleUtils.getStringDigit(vX, 4) + ";" + "Vy:" + DoubleUtils.getStringDigit(vY, 4);
            adjPoint2D.x = pointCoord.x + vX;
            adjPoint2D.y = pointCoord.y + vY;
            adjPoint2D.adjX = vX;
            adjPoint2D.adjY = vY;
        }
        return adjPoint2D;
    }

    /**
     * 根据四参数转换坐标
     *
     * @param DeltX
     * @param DeltY
     * @param scale
     * @param Alfa
     * @param oldPoint 需要转换的坐标
     * @param <P>
     * @return
     */
    public static <P extends Point2D> Point2D trantsferCoord(double DeltX, double DeltY, double scale, double Alfa, P oldPoint) {

        double x = oldPoint.x;
        double y = oldPoint.y;

        Point2D coord = new Point2D();
        coord.name = oldPoint.name;

        coord.x = DeltX + scale * (x * Math.cos(Alfa) + y * Math.sin(Alfa));
        coord.y = DeltY + scale * (y * Math.cos(Alfa) - x * Math.sin(Alfa));
        return coord;
    }

    /**
     * 四参数和原坐标计算到新坐标系下
     *
     * @param i
     * @return
     */
    public static FourParmInfo trantsferCoord(FourParmInfo i) {
        Point2D point2D = trantsferCoord(i.detlX, i.detlY, i.scale, i.angle, new Point2D(i.oldX, i.oldY));
        i.newX = point2D.x;
        i.newY = point2D.y;
        return i;
    }


    /**
     * 四参数反算 已知四参数和转换后的坐标，计算原坐标
     *
     * @param detlX    偏移量
     * @param detlY
     * @param scale    尺度因子 在1附近
     * @param Alfa     旋转角度（弧度）
     * @param oldPoint 需要转换的点
     * @return
     */
    public static <T extends Point2D> Point2D fourParmInverseComput(double detlX, double detlY, double scale, double Alfa, T oldPoint) {
        Point2D coord = new Point2D();
        coord.name = oldPoint.name;

        double x1 = oldPoint.getX();
        double y1 = oldPoint.getY();

        double xtemp = (x1 - detlX) / scale;
        double ytemp = (y1 - detlY) / scale;


        double a = Math.sin(Alfa);
        double b = Math.cos(Alfa);

        //Xn = detlX + s(x0*cosa+y0*sina)
        //Yn = detlY + s(y0*cosa-x0*sina)

        double[][] array = {{a, b}, {b, -a}};
        double[][] bx = {{xtemp}, {ytemp}};

        Matrix A = new Matrix(array);
        Matrix mtrxL = new Matrix(bx);
        Matrix res = A.solve(mtrxL);//矩阵求逆

        double y0 = res.get(0, 0);
        double x0 = res.get(1, 0);

        coord.x = x0;
        coord.y = y0;
        return coord;
    }


    /**
     * 根据四参数 反算原坐标
     *
     * @param i
     * @return
     */
    public static FourParmInfo fourParmInverseComput(FourParmInfo i) {
        Point2D point2D = BaseParmaTransfer.fourParmInverseComput(i.detlX, i.detlY, i.scale, i.angle, new Point2D(i.newX, i.newY));
        i.oldX = point2D.x;
        i.oldY = point2D.y;
        return i;
    }

    /**
     * 根据四参数 反算原坐标
     *
     * @param fourParmInfos
     * @return
     */
    public static List<FourParmInfo> fourParmInverseComput(List<FourParmInfo> fourParmInfos) {
        List<FourParmInfo> fourParmInfos1 = new ArrayList<>();
        for (FourParmInfo i : fourParmInfos) {
            fourParmInfos1.add(fourParmInverseComput(i));
        }
        return fourParmInfos1;
    }


}
