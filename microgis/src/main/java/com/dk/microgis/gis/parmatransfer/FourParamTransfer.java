package com.dk.microgis.gis.parmatransfer;


import Jama.Matrix;
import com.dk.microgis.base.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * GetError
 * 四参数坐标转换,支持正反算
 * Xb=X0*1 +Y0*0 + Xa*C -Ya*D
 * Yb=X0*0 +Ya*1 + Ya*C +Xa*D
 */
public class FourParamTransfer<T extends Point2D, P extends Point2D> extends BaseParmaTransfer<T, P> {

    //尺度比例因子，无量纲
    public double scale;

    //T表示基准坐标对应   新坐标(CPIII)，P表示需要转换的坐标数据
    public FourParamTransfer(List<P> m_oldCoordLS, List<T> m_newCoordLS) {
        super(m_oldCoordLS, m_newCoordLS);
    }

    public FourParamTransfer() {
    }

    @Override
    protected boolean computTransferParam() {
        double avgNewX = 0.0;
        double avgNewY = 0.0;
        for (int i = 0; i < m_oldCoordLS.size(); i++) {
            avgOldX += m_oldCoordLS.get(i).x;
            avgOldY += m_oldCoordLS.get(i).y;
            avgNewX += m_newCoordLS.get(i).x;
            avgNewY += m_newCoordLS.get(i).y;
        }
        avgOldX = avgOldX / m_newCoordLS.size();
        avgOldY = avgOldY / m_newCoordLS.size();
        avgNewX = avgNewX / m_newCoordLS.size();
        avgNewY = avgNewY / m_newCoordLS.size();

        avgOldX = 0.0;
        avgOldY = 0.0;
        avgNewX = 0.0;
        avgNewY = 0.0;

        int row = m_oldCoordLS.size() * 2; //确定系数矩阵B的行数

        double[][] mtrxB = new double[row][4];//系数矩阵，
        double[][] mtrxL = new double[row][1];//常数项矩阵
        for (int i = 0; i <= row - 2; i = i + 2) {

            mtrxB[i][0] = 1.0;
            mtrxB[i][1] = 0.0;
            mtrxB[i][2] = m_oldCoordLS.get(i / 2).x - avgOldX;
            mtrxB[i][3] = m_oldCoordLS.get(i / 2).y - avgOldY;

            mtrxB[i + 1][0] = 0.0;
            mtrxB[i + 1][1] = 1.0;
            mtrxB[i + 1][2] = m_oldCoordLS.get(i / 2).y - avgOldY;
            mtrxB[i + 1][3] = 0.0 - (m_oldCoordLS.get(i / 2).x - avgOldX);

            mtrxL[i][0] = m_newCoordLS.get(i / 2).x - avgNewX;
            mtrxL[i + 1][0] = m_newCoordLS.get(i / 2).y - avgNewY;
        }

        // Matrix transferParam = LeastSquear(new Matrix(mtrxB), new Matrix(mtrxL));
        //Matrix transferParam = LeastSquear(new Matrix(mtrxB), new Matrix(mtrxL));
        //如果是方阵，则返回解，否则返回最小二乘解
        Matrix matrixB = new Matrix(mtrxB);
        Matrix matrixL = new Matrix(mtrxL);
        Matrix transferParam = matrixB.solve(matrixL);//矩阵求逆


        deltX = transferParam.get(0, 0);
        deltY = transferParam.get(1, 0);

        //Alfa = Math.atan2(transferParam.get(3, 0), transferParam.get(2, 0));  (2,0)=(1+m)*cos seta     (3,0)=(1+m)*sin seta
        angle = Math.atan(transferParam.get(3, 0) / transferParam.get(2, 0));   //旋转角度
        // Alfa = Alfa < 0 ? Alfa + 2 * Math.PI : Alfa;
        scale = transferParam.get(2, 0) / Math.cos(angle);   //缩放系数  1+m

        //参考；《坐标平移量对平面四参数坐标转换残差的影响》
        Matrix delt = matrixB.times(transferParam).minus(matrixL);
        delt  = matrixL.minus(matrixB.times(transferParam));

        computError(scale);
        return true;
    }


    /**
     * 最小二乘原理解算方程
     *
     * @param mtrxB
     * @param mtrxL
     * @return
     */
    private Matrix LeastSquear(Matrix mtrxB, Matrix mtrxL) {
        Matrix mtrxBT = mtrxB.transpose();//B的转置
        Matrix mtrxBTB = mtrxBT.times(mtrxB);// B的转置×B
        Matrix matrixBN = mtrxBTB.inverse(); // 求(B的转置×B)的逆矩阵
        Matrix mtrxBTL = mtrxBT.times(mtrxL);//B的转置 * L
        return matrixBN.times(mtrxBTL);// 结果
    }

    /**
     * 将需要转换的坐标系转换到当前坐标系中
     *
     * @param oldPoint 原坐标系中数据
     * @return
     */
    public List<AdjPoint2D> transferCoord(List<P> oldPoint) {
        return transferCoord(oldPoint, true);
    }

    public List<AdjPoint2D> transferCoord(List<P> oldPoint, boolean isCorr) {
        List<AdjPoint2D> newP2d = new ArrayList<>();
        for (P p : oldPoint) {
            newP2d.add(transferCoord(p, isCorr));
        }
        return newP2d;
    }

    /**
     * 将坐标由旧坐标系转换到新坐标系
     *
     * @param oldPoint
     * @return 测站坐标
     */
    public AdjPoint2D transferCoord(P oldPoint, boolean isCorr) {
        Point2D point2D = trantsferCoord(deltX, deltY, scale, angle, oldPoint);
        if (isCorr) {
            return correctionPoint(point2D);
        }
        return new AdjPoint2D(point2D);
    }

    /**
     * 将坐标由旧坐标系转换到新坐标系,默认改正数据
     *
     * @param oldPoint
     * @return 测站坐标
     */
    public AdjPoint2D transferCoord(P oldPoint) {
        return transferCoord(oldPoint, true);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}