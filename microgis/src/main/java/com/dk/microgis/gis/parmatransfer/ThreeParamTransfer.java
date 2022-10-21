package com.dk.microgis.gis.parmatransfer;


import com.dk.microgis.base.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * GetError
 * 三参数坐标转换
 * 由于三参数不能直接列方程组进行计算，则可以先求旋转角，使用最小二乘
 * 参考  三参数转换模型及其在ＩＳＯ１７１２３－５中的应用 北京测绘
 * 再求平移量
 */
public class ThreeParamTransfer<T extends Point2D, P extends Point2D> extends BaseParmaTransfer<T, P> {

    public ThreeParamTransfer() {
    }

    public ThreeParamTransfer(List m_oldCoordLS, List m_newCoordLS) {
        super(m_oldCoordLS, m_newCoordLS);
    }

    /**
     * 计算转换参数
     *
     * @return
     */
    @Override
    protected boolean computTransferParam() {
        double avgX = 0.0;
        double avgY = 0.0;
        double avgS = 0.0;
        double avgT = 0.0;

        int size = m_oldCoordLS.size();
        for (int i = 0; i < size; i++) {
            double si = m_newCoordLS.get(i).x;
            double ti = m_newCoordLS.get(i).y;

            double xi = m_oldCoordLS.get(i).x;
            double yi = m_oldCoordLS.get(i).y;

            avgX += xi;
            avgY += yi;
            avgS += si;
            avgT += ti;
        }
        avgX = avgX / size;
        avgY = avgY / size;
        avgS = avgS / size;
        avgT = avgT / size;
        //求旋转角
        double XiSiYiTi = 0.0;
        double YiSiXiTi = 0.0;
        double XiXiYiYi = 0.0;

        //参考  三参数转换模型及其在ＩＳＯ１７１２３－５中的应用 北京测绘
        for (int i = 0; i < size; i++) {
            double si = m_newCoordLS.get(i).x - avgS;
            double ti = m_newCoordLS.get(i).y - avgT;

            double xi = m_oldCoordLS.get(i).x - avgX;
            double yi = m_oldCoordLS.get(i).y - avgY;


            XiSiYiTi += (xi * si + yi * ti);
            YiSiXiTi += (yi * si - xi * ti);
            XiXiYiYi += (xi * xi + yi * yi);
        }

        double cos = XiSiYiTi / XiXiYiYi;
        double sin = YiSiXiTi / XiXiYiYi;


        angle = Math.atan(sin / cos);

        double detX = 0.0;
        double detY = 0.0;
        //参考  三参数转换模型及其在ＩＳＯ１７１２３－５中的应用 北京测绘

        for (int i = 0; i < size; i++) {
            double oldX = m_oldCoordLS.get(i).x;
            double oldY = m_oldCoordLS.get(i).y;

            double newX = m_newCoordLS.get(i).x;
            double newY = m_newCoordLS.get(i).y;

            detX += newX - (oldX * Math.cos(angle) + oldY * Math.sin(angle));
            detY += newY - (oldY * Math.cos(angle) - oldX * Math.sin(angle));
        }


        deltX = detX / size;
        deltY = detY / size;
        computError(1);
        return true;
    }

    /**
     * 将坐标由旧坐标系转换到新坐标系
     *
     * @param oldPoint
     * @param isCorrect 是否修正
     * @return 测站坐标
     */
    public final AdjPoint2D transferCoord(Point2D oldPoint, boolean isCorrect) {
        Point2D coord = trantsferCoord(deltX, deltY, 1, angle, oldPoint);
        if (isCorrect) {
            return correctionPoint(coord);
        }
        return new AdjPoint2D(coord);
    }

    public List<AdjPoint2D> transferCoord(List<Point2D> oldPoint, boolean isCorrect) {
        List<AdjPoint2D> newP2d = new ArrayList<>();
        for (Point2D p2d : oldPoint) {
            newP2d.add(transferCoord(p2d, isCorrect));
        }
        return newP2d;
    }

}