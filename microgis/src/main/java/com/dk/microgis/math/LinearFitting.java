package com.dk.microgis.math;


import com.dk.common.DoubleUtils;
import com.dk.microgis.base.Point2D;
import com.dk.microgis.error.CommonException;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;

/**
 * @author hq
 * @date 2021-04-22 9:55
 * 简单线性回归
 * @desc 线性拟合
 */
public class LinearFitting<P extends Point2D> {
    List<P> pList;
    RegressionResults regressionResults;

    public Double k;
    public Double b;

    public Point2D lineFirstP;//线路点序列第一个点在拟合直线上点
    public Point2D lineLastP;//线路点序列最后一个点在拟合直线上点


    public LinearFitting() {

    }

    public LinearFitting(List<P> pList) {
        this.pList = pList;
        if (pList.size() > 2) {
            fittingLineByPoint();
        }
    }

    public double x2y(Double x) {
        if (!DoubleUtils.isIegalKey(k)) {
            throw new CommonException("k值不是一个数值类型");
        }
        return k * x + b;
    }

    public double y2x(Double y) {
        if (!DoubleUtils.isIegalKey(k)) {
            throw new CommonException("k值不是一个数值类型");
        }
        if (DoubleUtils.isZero(k)) {
            return b;
        }
        return (y - b) / k;
    }

    /**
     * 根据直线附近点，计算直线附近点的的垂足
     * 已经处理南北走向，k值是无穷情况，
     *
     * @param p
     * @return 垂足
     */
    public Point2D computFoot(P p) {
        computStartEndP();
        //拟合后，起终点
        return BaseAlgorithms.computFoot(lineFirstP, lineLastP, p); //当前点到起点和结束点垂足，
    }

    public void computStartEndP() {
        P t1 = pList.get(0);
        P t2 = pList.get(pList.size() - 1);


        if (DoubleUtils.isIegalKey(k)) {
            lineFirstP = new Point2D(t1.x, x2y(t1.x));
            lineLastP = new Point2D(t2.x, x2y(t2.x));
        } else {//处理南北走向
            lineFirstP = new Point2D(t1);
            lineLastP = new Point2D(t2);
        }
    }

    /**
     * 参考：https://blog.csdn.net/wufeiwua/article/details/109004452
     */
    void fittingLineByPoint() {
        double[][] points = getPoints(pList);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(points); // 数据集
        regressionResults = regression.regress();
        b = regressionResults.getParameterEstimate(0);//截距
        k = regressionResults.getParameterEstimate(1);//斜率
    }

    private double[][] getPoints(List<P> pList) {
        int size = pList.size();
        double[][] xy = new double[size][2];
        for (int i = 0; i < size; i++) {
            xy[i][0] = pList.get(i).x; // x
            xy[i][1] = pList.get(i).y; // y
        }
        return xy;
    }

    public RegressionResults getRegressionResults() {
        return regressionResults;
    }

    public void setRegressionResults(RegressionResults regressionResults) {
        this.regressionResults = regressionResults;
    }

    public Double getK() {
        return k;
    }

    public void setK(Double k) {
        this.k = k;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }
}
