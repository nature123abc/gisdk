package com.dk.microgis.line.curve.curve;


import com.dk.common.DoubleUtils;
import com.dk.microgis.error.CommonException;
import com.dk.microgis.line.curve.curve.common.*;
import com.dk.microgis.line.curve.curve.common.entity.FiveStakeMile;
import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.line.curve.curve.common.entity.TangentCurveTL;
import com.dk.microgis.math.Angle;
import com.dk.microgis.math.BaseAlgorithms;
import com.dk.microgis.base.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-04-16 16:07
 * @desc 交点计算五大桩
 * 输入交点信息，计算到五大桩
 */
public class CrossoverPoint2Stake<T extends CrossoverPoint> {
    List<T> baseCross;//曲线交点基本信息，必须是一个分带数据


    List<CurveMetaInfo> multFiveStake;//曲线交点和五大桩信息如果是直线则只有一个桩信息
    List<CompleteCrossoverPoint> completeCross;//曲线交点完整信息，包括起点和终点


    public CrossoverPoint2Stake(List<T> baseCorss) throws Exception {
        this.baseCross = baseCorss;
        multFiveStake = new ArrayList<>();
        completeCross = new ArrayList<>();
        init();
    }

    private void init() throws CommonException {

        if (baseCross.size() < 2) {
            throw new CommonException("交点数不能小于2");
        }
        if (baseCross.size() == 2) {//直线
            computLineinfo(baseCross.get(0), baseCross.get(1));
        }
        //计算偏向偏角，将基本交点转换为完整交点
        computTurnInfo();

        computCorssInfo();
    }

    private void computCorssInfo() {
        int size = completeCross.size();
        for (int i = 1; i < size - 1; i++) {//交点
            CompleteCrossoverPoint start = completeCross.get(i - 1);//起点
            CompleteCrossoverPoint current = completeCross.get(i);//交点
            CompleteCrossoverPoint end = completeCross.get(i + 1);//终点


            //计算曲线切线长和曲线信息
            //TangentCurveTL curveTL1000 = computFirstCurve(current);

            TangentCurveTL curveTL1 = computFirstCurve11(current, true);//第一缓和曲线
            TangentCurveTL curveTL2 = computFirstCurve11(current, false);//第一缓和曲线


            computAllCurve(current, curveTL1, curveTL2);


            //计算交点里程(临时使用）
            double jdDk = computJdDk(i, start, current);
            //计算五大桩里程信息
            FiveStakeMile fiveStakeMile = computFiveStakeDKL1L2(jdDk, curveTL1.t, curveTL2.l, curveTL1.l, current.curveL1, current.curveL2);

            //起点到交点方位角
            double jdZimuth = BaseAlgorithms.computAzimuthByTwoPoint(start, current);

            //直缓点
            TangentPoint ZHPoint = zHuanXYAzimuth(current, curveTL1.t, jdZimuth);
            ZHPoint.continuDk = fiveStakeMile.zhDk;

            if (i == 1) {//先计算第一部分，QD->HZ
                List<StakeInfo> stakeInfos = new ArrayList<>();
                StakeInfo stakeInfo = new StakeInfo();
                stakeInfo.name = start.getName();//交点名称
                stakeInfo.startPoName = FiveStakeType.QD.name();//起点名称
                stakeInfo.startPoDk = start.getContinuDk();//里程
                stakeInfo.nextPoDk = fiveStakeMile.zhDk;//里程
                stakeInfo.startPoRadius = DoubleUtils.MAX_VALUE;//起点半径
                stakeInfo.nextPoRadius = DoubleUtils.MAX_VALUE;//下一点半径
                stakeInfo.startPoAzimuth = ZHPoint.tangentAzimuth;
                stakeInfo.startX = start.x;//
                stakeInfo.startY = start.y;//
                stakeInfo.jdDirection = start.orientation;
                stakeInfo.maxGapH = 0.0;
                stakeInfos.add(stakeInfo);


                CurveMetaInfo oneCurve = new CurveMetaInfo(start, stakeInfos);
                multFiveStake.add(oneCurve);
            }

            List<StakeInfo> stakeInfos = new ArrayList<>(5);
            // 直缓点——缓圆点
            StakeInfo firstP = new StakeInfo();
            firstP.name = current.getName();//交点名称
            firstP.startPoName = FiveStakeType.ZH.name();//
            firstP.startPoDk = fiveStakeMile.zhDk;
            firstP.nextPoDk = fiveStakeMile.hyDk;

            firstP.startPoRadius = DoubleUtils.MAX_VALUE;
            firstP.nextPoRadius = current.radius;
            firstP.startPoAzimuth = ZHPoint.tangentAzimuth;
            firstP.startX = ZHPoint.x;
            firstP.startY = ZHPoint.y;
            firstP.jdDirection = current.orientation;
            firstP.maxGapH = current.maxh;
            stakeInfos.add(firstP);

            // 缓圆点——曲中点
            TangentPoint HYPoint = curveXYAzimuth(firstP);
            StakeInfo secondP = new StakeInfo();
            secondP.name = current.getName();//交点名称
            secondP.startPoName = FiveStakeType.HY.name();//

            secondP.startPoDk = fiveStakeMile.hyDk;
            secondP.nextPoDk = fiveStakeMile.qzDk;

            secondP.startPoRadius = current.radius;
            secondP.nextPoRadius = current.radius;
            secondP.startPoAzimuth = HYPoint.tangentAzimuth;
            secondP.startX = HYPoint.x;
            secondP.startY = HYPoint.y;
            secondP.jdDirection = current.orientation;
            secondP.maxGapH = current.maxh;
            stakeInfos.add(secondP);

            //曲中点——圆缓点
            TangentPoint QZPoint = curveXYAzimuth(secondP);

            StakeInfo thridP = new StakeInfo();
            thridP.name = current.getName();//交点名称
            thridP.startPoName = FiveStakeType.QZ.name();//
            thridP.setStartPoDk(fiveStakeMile.qzDk);
            thridP.setNextPoDk(fiveStakeMile.yhDk);

            thridP.startPoRadius = current.radius;
            thridP.nextPoRadius = current.radius;
            thridP.startPoAzimuth = QZPoint.tangentAzimuth;
            thridP.startX = QZPoint.x;
            thridP.startY = QZPoint.y;
            thridP.jdDirection = current.orientation;
            thridP.maxGapH = current.maxh;
            stakeInfos.add(thridP);

            //圆缓点——缓直点
            TangentPoint YHPoint = curveXYAzimuth(thridP);
            StakeInfo fourthP = new StakeInfo();
            fourthP.name = current.getName();//交点名称
            fourthP.startPoName = FiveStakeType.YH.name();//
            fourthP.setStartPoDk(fiveStakeMile.yhDk);
            fourthP.setNextPoDk(fiveStakeMile.hzDk);

            fourthP.startPoRadius = current.radius;
            fourthP.nextPoRadius = DoubleUtils.MAX_VALUE;
            fourthP.startPoAzimuth = YHPoint.tangentAzimuth;
            fourthP.startX = YHPoint.x;
            fourthP.startY = YHPoint.y;
            fourthP.jdDirection = current.orientation;
            fourthP.maxGapH = current.maxh;
            stakeInfos.add(fourthP);

            // 缓直点——下一个点(直缓点)
            TangentPoint HZPoint = curveXYAzimuth(fourthP);
            StakeInfo fifth = new StakeInfo();
            fifth.name = current.getName();//交点名称
            fifth.startPoName = FiveStakeType.HZ.name();//
            fifth.setStartPoDk(fiveStakeMile.hzDk);
            //fifth.setNextPoDk(fiveStakeMile.hzDk);

            fifth.startPoRadius = DoubleUtils.MAX_VALUE;
            fifth.nextPoRadius = DoubleUtils.MAX_VALUE;
            fifth.startPoAzimuth = HZPoint.tangentAzimuth;
            fifth.startX = HZPoint.x;
            fifth.startY = HZPoint.y;
            fifth.jdDirection = current.orientation;
            fifth.maxGapH = 0.0;
            stakeInfos.add(fifth);


            if (i > 1) {//补缺的里程
                multFiveStake.get(i - 1).getFiveStakeInfo().get(4).nextPoDk = fiveStakeMile.zhDk;
            }
            CurveMetaInfo oneCurve = new CurveMetaInfo(current, stakeInfos);
            multFiveStake.add(oneCurve);


            if (i == size - 2) {//当最后一个交点时，计算ZD的里程
                double dk = fiveStakeMile.hzDk + BaseAlgorithms.computTwoPointDistance(HZPoint, end);
                end.setContinuDk(dk);
                end.zhDk = dk;
                end.hzDk = dk;
                fifth.setNextPoDk(dk);

                CurveMetaInfo temp = new CurveMetaInfo(end, new ArrayList<>());
                multFiveStake.add(temp);
            }
        }
    }

    private void computAllCurve(CompleteCrossoverPoint current, TangentCurveTL curveTL1, TangentCurveTL curveTL2) {
        double turnAng = current.getAngle();
        double radius = current.getRadius();

        double circleLen = (turnAng - curveTL1.b - curveTL2.b) * radius;
        current.circleLen = circleLen;
        current.curveTL1 = curveTL1;
        current.curveTL2 = curveTL2;
        current.curveLen = circleLen + current.curveL1 + current.curveL2;
    }


    public static TangentPoint curveXYAzimuth(StakeInfo stakeInfo) {
        return curveXYAzimuth(stakeInfo, stakeInfo.nextPoDk);
    }

    /**
     * 根据上一个曲线桩，计算下一个曲线桩坐标和方位角
     *
     * @param stakeInfo
     * @return
     */
    public static TangentPoint curveXYAzimuth(StakeInfo stakeInfo, Double dk) {
        TangentPoint midXY = new TangentPoint();

        double l = dk - stakeInfo.startPoDk;

        int sign = Orientation.右.equals(stakeInfo.jdDirection) ? -1 : 1;
        double ka = Math.abs(stakeInfo.startPoRadius) > (DoubleUtils.MAX_VALUE - 1) ? 0.0 : 1.0 / (stakeInfo.startPoRadius);
        double kb = Math.abs(stakeInfo.nextPoRadius) > (DoubleUtils.MAX_VALUE - 1) ? 0.0 : 1.0 / (stakeInfo.nextPoRadius);

        double dis = stakeInfo.nextPoDk - stakeInfo.startPoDk;//两点里程差
        double min = Math.pow(DoubleUtils.MIN_VALUE, 2.0);
        double h = Math.abs(dis) < DoubleUtils.MIN_DIS ? (kb - ka) / (2.0 * min) : (kb - ka) / (2.0 * (dis));


        double tempX = l - Math.pow(ka, 2.0) * Math.pow(l, 3) / 6 - h * ka * Math.pow(l, 4) / 4 + (Math.pow(ka, 4) - 12 * Math.pow(h, 2)) * Math.pow(l, 5) / 120
                + h * Math.pow(ka, 3) * Math.pow(l, 6) / 36 + Math.pow(h, 2) * Math.pow(ka, 2) * Math.pow(l, 7) / 28 + Math.pow(h, 3) * ka * Math.pow(l, 8) / 48
                + Math.pow(h, 4) * Math.pow(l, 9) / 216 - Math.pow(ka, 6) * Math.pow(l, 7) / 5040 - h * Math.pow(ka, 5) * Math.pow(l, 8) / 960
                - Math.pow(h, 2) * Math.pow(ka, 4) * Math.pow(l, 9) / 432 - Math.pow(h, 3) * Math.pow(ka, 3) * Math.pow(l, 10) / 360
                - Math.pow(h, 4) * Math.pow(ka, 2) * Math.pow(l, 11) / 528 - Math.pow(h, 5) * ka * Math.pow(l, 12) / 1440 - Math.pow(h, 6) * Math.pow(l, 13) / 9360;

        double tempY = ka * Math.pow(l, 2) / 2 + h * Math.pow(l, 3) / 3 - Math.pow(ka, 3) * Math.pow(l, 4) / 24 - h * Math.pow(ka, 2) * Math.pow(l, 5) / 10
                + (Math.pow(ka, 5) - 60 * Math.pow(h, 2) * ka) * Math.pow(l, 6) / 720 + (h * Math.pow(ka, 4) - 4 * Math.pow(h, 3)) * Math.pow(l, 7) / 168
                + Math.pow(h, 2) * Math.pow(ka, 3) * Math.pow(l, 8) / 96 + Math.pow(h, 3) * Math.pow(ka, 2) * Math.pow(l, 9) / 108
                + Math.pow(h, 4) * ka * Math.pow(l, 10) / 240 + Math.pow(h, 5) * Math.pow(l, 11) / 1320 - Math.pow(ka, 7) * Math.pow(l, 8) / 40320
                - h * Math.pow(ka, 6) * Math.pow(l, 9) / 6480 - Math.pow(h, 2) * Math.pow(ka, 5) * Math.pow(l, 10) / 2400 - Math.pow(h, 3) * Math.pow(ka, 4) * Math.pow(l, 11) / 1584
                - Math.pow(h, 4) * Math.pow(ka, 3) * Math.pow(l, 12) / 1728 - Math.pow(h, 5) * Math.pow(ka, 2) * Math.pow(l, 13) / 3120
                - Math.pow(h, 6) * ka * Math.pow(l, 14) / 10080 - Math.pow(h, 7) * Math.pow(l, 15) / 75600;

        double tempAng = Angle.formAngle(stakeInfo.startPoAzimuth);


        midXY.x = stakeInfo.startX + tempX * Math.cos(tempAng) + tempY * Math.sin(tempAng) * sign;
        midXY.y = stakeInfo.startY + tempX * Math.sin(tempAng) - tempY * Math.cos(tempAng) * sign;

        double azimuth = tempAng - sign * (ka * l + h * Math.pow(l, 2.0));
        azimuth = Angle.formAngle(azimuth);

        midXY.tangentAzimuth = azimuth;
        midXY.continuDk = dk;
        return midXY;
    }


    /**
     * 计算超高
     * 超高在缓和曲线上按照距离均匀变化，在圆曲线上为超高最大值
     *
     * @param dk
     * @param stake
     * @return
     */
    public static double computeGapH(StakeInfo stake, double dk) {
        double superH = 0.0;
        if (stake.startPoName.equals("ZH")) {                                //if..直缓-缓圆，内插超高
            superH = (dk - stake.startPoDk) * stake.maxGapH / (stake.nextPoDk - stake.startPoDk);
        } else if (stake.startPoName.equals("HY") || stake.startPoName.equals("QZ")) {  //if..在圆曲线上，内插超高
            superH = stake.maxGapH;
        } else if (stake.startPoName.equals("YH")) {                          //if..缓圆-直缓，内插超高
            superH = stake.maxGapH - (dk - stake.startPoDk) * stake.maxGapH / (stake.nextPoDk - stake.startPoDk);
        }
        return superH;
    }


    /**
     * @param current
     * @param t       曲线切线长
     * @param azimuth 起点到交点方位角
     * @return 计算ZH点坐标
     */
    private TangentPoint zHuanXYAzimuth(CompleteCrossoverPoint current, double t, double azimuth) {
        TangentPoint PtXYA = new TangentPoint();
        double azimuthJdZH = azimuth + Math.PI;
        azimuthJdZH = Angle.formAngle(azimuthJdZH);


        PtXYA.x = current.x + t * Math.cos(azimuthJdZH);
        PtXYA.y = current.y + t * Math.sin(azimuthJdZH);
        PtXYA.tangentAzimuth = azimuth;

        return PtXYA;
    }


    /**
     * 由交点坐标的里程、切线长度L1、切线长度L2、曲线长L1,曲线长L2、第一缓和曲线长、第二缓和曲线计算五大桩的里程"
     *
     * @param jdDk   交点坐标的里程
     * @param tangL1 切线长度L1
     * @param FrontL 曲线长L1  从ZH点到曲中
     * @param BackL  曲线长L2  从QZ点到HZ
     * @param curve1 第一缓和曲线长
     * @param curve2 第二缓和曲线
     * @return
     */
    FiveStakeMile computFiveStakeDKL1L2(double jdDk, double tangL1, double FrontL, double BackL, double curve1, double curve2) {
        FiveStakeMile fiveDk = new FiveStakeMile();

        fiveDk.zhDk = jdDk - tangL1;
        fiveDk.hyDk = fiveDk.zhDk + curve1;
        fiveDk.qzDk = fiveDk.zhDk + FrontL;
        fiveDk.yhDk = fiveDk.qzDk + BackL - curve2;
        fiveDk.hzDk = fiveDk.zhDk + FrontL + BackL;

        return fiveDk;
    }

    /**
     * 计算交点里程，临时使用
     *
     * @param i
     * @param start
     * @param current
     * @return 该交点的了里程
     */
    private double computJdDk(int i, CompleteCrossoverPoint start, CompleteCrossoverPoint current) {
        if (i == 1) {//第一个交点里程=起点Dk+起点到jD1的距离
            if (null == start.continuDk) {
                throw new CommonException("起点坐标起始里程不能为空");
            }
            return start.getContinuDk() + BaseAlgorithms.computTwoPointDistance(start, current);//到JD 位置里程
        }

        CurveMetaInfo last = multFiveStake.get(i - 1);

        double lastDk = last.getFiveStakeInfo().get(4).getStartPoDk();//上一个交点的里程

        return lastDk + BaseAlgorithms.computTwoPointDistance(current
                , new Point2D(last.getFiveStakeInfo().get(4).startX, last.getFiveStakeInfo().get(4).startY));
    }


    /**
     * https://wenku.baidu.com/view/bd21593431126edb6f1a102a.html
     *
     * @param current 根据转角，圆曲线半径，第一、第二缓和曲线长，计算曲线要素
     * @return
     */
    private TangentCurveTL computFirstCurve(CompleteCrossoverPoint current) {
        double turnAng = current.getAngle();
        double radius = current.getRadius();
        double arc1 = current.getCurveL1();
        double arc2 = current.getCurveL2();


        TangentCurveTL Derive = new TangentCurveTL();

        double bete1 = arc1 / (2.0 * radius); //切线角 l/2r  缓和曲线长度/2r
        double PFront = Math.pow(arc1, 2.0) / radius / 24.0;//内移距p

        double MFront = arc1 / 2 - Math.pow(arc1, 3.0) / Math.pow(radius, 2) / 240.0;//切线增量


        double PBack = Math.pow(arc2, 2) / radius / 24;//内移距p
        double ctaFront = Math.atan(((radius + PBack) / (radius + PFront) - Math.cos(turnAng)) / Math.sin(turnAng));

        if (ctaFront < 0) {
            ctaFront = ctaFront + Math.PI;
        }


        Derive.b = bete1;
        Derive.p = PFront;//内移距
        Derive.q = MFront;//切线增量
        Derive.t = MFront + (radius + PFront) * Math.tan(ctaFront);//曲线切线长
        Derive.l = radius * ctaFront + arc1 / 2;//曲线长L

        double r = Math.atan((radius + PFront) / (Derive.t - MFront));
        Derive.e = (radius + PFront) / (Math.sin(r)) - radius;//外矢距
        return Derive;
    }

    /**
     * https://wenku.baidu.com/view/bd21593431126edb6f1a102a.html
     * https://www.sohu.com/a/164486053_583961
     *
     * @param current 当前交点,包括第一，第二缓和曲线长，曲线半径
     * @return 曲线要素相关信息
     */
    private TangentCurveTL computFirstCurve11(CompleteCrossoverPoint current, boolean isFirst) {

        double turnAng = current.getAngle();
        double radius = current.getRadius();
        double curve1 = current.getCurveL1();
        double curve2 = current.getCurveL2();


        TangentCurveTL tangentCurveTL = new TangentCurveTL();

        double bete1 = curve1 / (2.0 * radius); //切线角 l/2r  缓和曲线长度/2r
        double bete2 = curve2 / (2.0 * radius); //切线角 l/2r  缓和曲线长度/2r


        //可以提高精度
        double q1 = curve1 / 2.0 - Math.pow(curve1, 3.0) / (Math.pow(radius, 2) * 240.0);//切线增量
        double q2 = curve2 / 2.0 - Math.pow(curve2, 3.0) / (Math.pow(radius, 2) * 240.0);//切线增量
      /*  double q1 = curve1 / 2.0 - Math.pow(curve1, 3.0) / (Math.pow(radius, 2) * 240.0) + Math.pow(curve1, 5.0) / (Math.pow(radius, 4.0) * 34560.0);//切线增量
        double q2 = curve2 / 2.0 - Math.pow(curve2, 3.0) / (Math.pow(radius, 2) * 240.0) + Math.pow(curve2, 5.0) / (Math.pow(radius, 4.0) * 34560.0);//切线增量
*/
        double p1 = Math.pow(curve1, 2.0) / radius / 24.0;//内移距p
        double p2 = Math.pow(curve2, 2.0) / radius / 24.0;//内移距p

      /*  double p1 = Math.pow(curve1, 2.0) / radius / 24.0 + Math.pow(curve1, 4.0) / (2688.0 * Math.pow(radius, 3.0));//内移距p
        double p2 = Math.pow(curve2, 2.0) / radius / 24.0 + Math.pow(curve2, 4.0) / (2688.0 * Math.pow(radius, 3.0));//内移距p
*/

        double harfAng = turnAng / 2.0;


        double t1 = (radius + p1) * Math.tan(harfAng) + q1 - ((p1 - p2) / Math.sin(turnAng));
        double t2 = (radius + p2) * Math.tan(harfAng) + q2 + ((p1 - p2) / Math.sin(turnAng));


        double r1 = Math.atan((radius + p1) / (t1 - q1));
        double r2 = Math.atan((radius + p2) / (t2 - q2));

        double e1 = (radius + p1) / (Math.sin(r1)) - radius;
        double e2 = (radius + p2) / (Math.sin(r2)) - radius;


        double curL = (turnAng - bete1 - bete2) * radius;//圆曲线长
        double l1 = curL / 2 + curve1;//曲线全长，直到曲中
        double l2 = curL / 2 + curve2;//曲线全长，直到曲中


        if (isFirst) {
            tangentCurveTL.b = bete1;
            tangentCurveTL.p = p1;//内移距
            tangentCurveTL.q = q1;//切线增量
            tangentCurveTL.t = t1;//曲线切线长 从ZH点到JD连线

            tangentCurveTL.l = l1;
            tangentCurveTL.e = e1;//外矢距
            return tangentCurveTL;
        }
        tangentCurveTL.b = bete2;
        tangentCurveTL.p = p2;//内移距
        tangentCurveTL.q = q2;//切线增量
        tangentCurveTL.t = t2;//曲线切线长

        tangentCurveTL.l = l2;//曲线长L
        tangentCurveTL.e = e2;//外矢距

        return tangentCurveTL;
    }

    private void computTurnInfo() {
        for (CrossoverPoint point : baseCross) {
            completeCross.add(new CompleteCrossoverPoint(point));
        }
        //计算偏向,偏角
        for (int i = 1; i < completeCross.size() - 1; i++) {//从起点+交点+终点 计算所有的交点偏向
            CompleteCrossoverPoint start = completeCross.get(i - 1);
            CompleteCrossoverPoint current = completeCross.get(i);
            CompleteCrossoverPoint end = completeCross.get(i + 1);

            Orientation or = computDirect(start, current, end);
            double ang = computTurnAng(start, current, end, or);

            current.setOrientation(or);
            current.setAngle(ang);
        }
    }

    /**
     * 计算偏向
     *
     * @param start
     * @param current
     * @param end
     * @return
     */
    public static Orientation computDirect(Point2D start, Point2D current, Point2D end) {
        double angle2Current = BaseAlgorithms.computAzimuthByTwoPoint(start, current);
        double angle2End = BaseAlgorithms.computAzimuthByTwoPoint(current, end);

        double detlAng = angle2End + (Angle.PI_DOUBLE - angle2Current);
        detlAng = Angle.formAngle(detlAng);


        //测定线路前进方向的右角为β，当β小于180°，为右偏角（线路向左偏移），当β大于180则为左偏角（线路向右偏移）。
        Orientation or = Orientation.左;
        if (detlAng >= 0.0 && detlAng <= Angle.PI) {
            or = Orientation.右;
        } else if (detlAng >= Angle.PI && detlAng <= Angle.PI_DOUBLE) {
            or = Orientation.左;
        }
        return or;
    }

    private void computLineinfo(CrossoverPoint startP, CrossoverPoint endP) {


        StakeInfo stakeInfo = new StakeInfo();
        stakeInfo.setName(startP.getName());//交点名称
        stakeInfo.setJdDirection(null);//交点方向


        stakeInfo.setStartPoName(startP.getName());//起点名称
        stakeInfo.setStartPoDk(startP.getContinuDk());//起点里程
        stakeInfo.setStartX(startP.getX());//
        stakeInfo.setStartY(startP.getY());//
        stakeInfo.setStartPoRadius(-DoubleUtils.MAX_VALUE);//


        double endDk = BaseAlgorithms.computTwoPointDistance(startP, endP);
        stakeInfo.setNextPoDk(startP.getContinuDk() + endDk);//结束点里程
        stakeInfo.setNextPoRadius(-DoubleUtils.MAX_VALUE);

        double angle = BaseAlgorithms.computAzimuthByTwoPoint(startP, endP);
        stakeInfo.startPoAzimuth = angle;
        stakeInfo.setMaxGapH(0.0);


        List<StakeInfo> stakeInfos = new ArrayList<>();
        stakeInfos.add(stakeInfo);
        CurveMetaInfo metaInfo = new CurveMetaInfo(new CompleteCrossoverPoint(), stakeInfos);
        multFiveStake.add(metaInfo);
    }


    /**
     * 计算偏角
     *
     * @param PX
     * @return
     */
    public static double computTurnAng(Point2D p1, Point2D p2, Point2D p3, Orientation PX) {
        double turnAng = 0.0;

        double firstAzimuth = BaseAlgorithms.computAzimuthByTwoPoint(p1, p2);
        double secondAzimuth = BaseAlgorithms.computAzimuthByTwoPoint(p2, p3);

        if (PX.equals(Orientation.左)) {       //左偏
            turnAng = firstAzimuth - secondAzimuth;
        } else if (PX.equals(Orientation.右)) {    //右偏
            turnAng = secondAzimuth - firstAzimuth;
        }

        return Angle.formAngle(turnAng);
    }

    public List<CompleteCrossoverPoint> getCompleteCross() {
        return completeCross;
    }

    public List<CurveMetaInfo> getMultFiveStake() {
        return multFiveStake;
    }
}
