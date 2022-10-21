package com.dk.microgis.gis.parmatransfer;

import Jama.Matrix;
import com.dk.microgis.base.Point3D;

import java.util.List;

/**
 * 七参数
 * 参考：https://blog.csdn.net/u010534192/article/details/77196064
 *
 * @param <P>
 * @param <T>
 */
public class SevenParamTransfer<P extends Point3D, T extends Point3D> {

    //X坐标增量，单位m
    public Double deltX;

    public Double deltZ;

    // Y坐标增量，单位m
    public Double deltY;

    public double scale;

    // 旋转角度，单位弧度
    public Double angleX;
    public Double angleY;
    public Double angleZ;
    // 单位权中误差   测点点位精度
    public Double sigma;

    //改正数带权平方和
    public Double PVV;
    //观测点，独立坐标系下
    public List<P> m_oldCoordLS;
    //CPIiii点
    public List<T> m_newCoordLS;
    //x方向改正数 符号相反
    public Double[] m_Vx;
    //Y方向改正数
    public Double[] m_Vy;

    public Double[] m_Vz;

    public SevenParamTransfer(List<P> m_oldCoordLS, List<T> m_newCoordLS) {
        this.m_oldCoordLS = m_oldCoordLS;
        this.m_newCoordLS = m_newCoordLS;
        comput();
    }

    private void comput() {
        int row = m_oldCoordLS.size() * 3; //确定系数矩阵B的行数

        double[][] mtrxB = new double[row][7];//系数矩阵，
        double[][] mtrxL = new double[row][1];//常数项矩阵

        m_Vx = new Double[m_oldCoordLS.size()];
        m_Vy = new Double[m_oldCoordLS.size()];
        m_Vz = new Double[m_oldCoordLS.size()];
        for (int i = 0; i < row; i++) {

            if (i % 3 == 0) {
                mtrxB[i][0] = 1.0;
                mtrxB[i][1] = 0.0;
                mtrxB[i][2] = 0.0;
                mtrxB[i][3] = m_oldCoordLS.get(i / 3).x;
                mtrxB[i][4] = 0.0;
                mtrxB[i][5] = -m_oldCoordLS.get(i / 3).z;
                mtrxB[i][6] = m_oldCoordLS.get(i / 3).y;
            }
            if (i % 3 == 1) {
                mtrxB[i][0] = 0.0;
                mtrxB[i][1] = 1.0;
                mtrxB[i][2] = 0.0;
                mtrxB[i][3] = m_oldCoordLS.get(i / 3).y;
                mtrxB[i][4] = m_oldCoordLS.get(i / 3).z;
                mtrxB[i][5] = 0.0;
                mtrxB[i][6] = -m_oldCoordLS.get(i / 3).x;
            }
            if (i % 3 == 2) {
                mtrxB[i][0] = 0.0;
                mtrxB[i][1] = 0.0;
                mtrxB[i][2] = 1.0;
                mtrxB[i][3] = m_oldCoordLS.get(i / 3).z;
                mtrxB[i][4] = -m_oldCoordLS.get(i / 3).y;
                mtrxB[i][5] = m_oldCoordLS.get(i / 3).x;
                mtrxB[i][6] = 0.0;
            }

            if (i % 3 == 0) {
                mtrxL[i][0] = m_newCoordLS.get(i / 3).x;
            }
            if (i % 3 == 1) {
                mtrxL[i][0] = m_newCoordLS.get(i / 3).y;
            }
            if (i % 3 == 2) {
                mtrxL[i][0] = m_newCoordLS.get(i / 3).z;
            }
        }

        // Matrix transferParam = LeastSquear(new Matrix(mtrxB), new Matrix(mtrxL));
        //Matrix transferParam = LeastSquear(new Matrix(mtrxB), new Matrix(mtrxL));
        //如果是方阵，则返回解，否则返回最小二乘解
        Matrix matrixB = new Matrix(mtrxB);
        Matrix matrixL = new Matrix(mtrxL);
        Matrix transferParam = matrixB.solve(matrixL);//矩阵求逆

        Matrix delt = matrixB.times(transferParam).minus(matrixL);
        delt = matrixL.minus(matrixB.times(transferParam));

        for (int i = 0; i < delt.getRowDimension(); i++) {
            if (i % 3 == 0) {
                m_Vx[i / 3] = delt.get(i, 0);
            }
            if (i % 3 == 1) {
                m_Vy[i / 3] = delt.get(i, 0);
            }
            if (i % 3 == 2) {
                m_Vz[i / 3] =delt.get(i, 0);
            }
        }


        deltX = transferParam.get(0, 0);
        deltY = transferParam.get(1, 0);
        deltZ = transferParam.get(2, 0);
        scale = transferParam.get(3, 0);

        angleX = transferParam.get(4, 0) / scale;
        angleY = transferParam.get(5, 0) / scale;
        angleZ = transferParam.get(6, 0) / scale;

      /*  System.out.println(Angle.RAD2DMS(angleX));
        System.out.println(Angle.RAD2DMS(angleY));
        System.out.println(Angle.RAD2DMS(angleZ));*/

        PVV = 0.0;
        for (int i = 0; i < m_oldCoordLS.size(); i++) {

            //PVV += m_Vx[i] * m_Vx[i] + m_Vy[i] * m_Vy[i];
            PVV += Math.pow(m_Vx[i], 2.0) + Math.pow(m_Vy[i], 2.0) + Math.pow(m_Vz[i],2.0);
        }
        sigma = m_newCoordLS.size() <= 3 ? 0.0 : Math.sqrt(PVV / (m_newCoordLS.size() * 3 - 7));

        //System.out.println(sigma);
    }
}
