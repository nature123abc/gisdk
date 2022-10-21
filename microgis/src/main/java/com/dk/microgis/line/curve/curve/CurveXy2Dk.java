package com.dk.microgis.line.curve.curve;




import com.dk.common.DoubleUtils;
import com.dk.microgis.error.CommonException;
import com.dk.microgis.line.curve.curve.common.CurveMetaInfo;
import com.dk.microgis.line.curve.curve.common.FiveStakeType;
import com.dk.microgis.line.curve.curve.common.StakeInfo;
import com.dk.microgis.line.curve.curve.common.TangentPoint;
import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.math.Angle;
import com.dk.microgis.math.BaseAlgorithms;
import com.dk.microgis.base.Point2D;
import com.dk.microgis.base.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-04-16 18:12
 * @desc 根据曲线元计算线路信息
 */
public class CurveXy2Dk<T extends Point2D> {
    List<CurveMetaInfo> multFiveStake;//五大桩

    List<StakeInfo> stakeInfos;//曲线元要素

    public CurveXy2Dk(List<CurveMetaInfo> multFiveStake) {
        this.multFiveStake = multFiveStake;
        stakeInfos = new ArrayList<>();
        for (CurveMetaInfo i : multFiveStake) {
            stakeInfos.addAll(i.getFiveStakeInfo());
        }
    }


    public TangentPoint cumputXyByDk(double dk) {
        return cumputXyByDk(dk, 0.0);
    }

    public TangentPoint cumputXyByDk(double dk, double offsetDis) {
        return cumputXyByDk(stakeInfos, dk, offsetDis);
    }


    public TangentPoint computeDkByXY(T point, double broderRegion) {
        return computeDkByXY(stakeInfos, point, 100.0, broderRegion);
    }

    static <T extends Point2D> TangentPoint computeDkByXY(List<StakeInfo> stakes, T inputPo, double ToleranceSD, double broderRegion) {

        int count = stakes.size();
        TangentPoint linePoint = null;

        if (count < 1) {
            return linePoint;
        }

        if (stakes.size() == 1) {//直线情况，特殊处理
            return computBroderPoint(stakes, inputPo, broderRegion);
        }

        for (int i = 0; i < count; i++) {
            StakeInfo currentS = stakes.get(i);
            if (i < count - 1) {//前面曲线元
                if (Math.abs(currentS.startPoDk - currentS.nextPoDk) < 0.001) {
                    continue;
                }    //主要处理缓和曲线为0的情况

                double firstAzimuth = Angle.formAngle(currentS.startPoAzimuth);//起点切线方位角
                double secondAzimuth = Angle.formAngle(stakes.get(i + 1).startPoAzimuth);//终点切线方位角

                double afaBP = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(currentS.startX, currentS.startY), inputPo);//起点到目标点方位角

                double afaEP = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(stakes.get(i + 1).startX, stakes.get(i + 1).startY), inputPo);//终点到目标点方位角

                double betaB = afaBP - firstAzimuth;//起点到目标点α-起点切线方位角
                betaB = Angle.formAngle(betaB);

                double betaE = afaEP - secondAzimuth;//终点到目标点α-终点切线方位角
                betaE = Angle.formAngle(betaE);


                //起点0-90/180-270 并且终点在90-180直接的方位角之差，则该点在该曲线元内。
                if (((betaB >= 0.0 && betaB <= Angle.PI_HALF)
                        || (betaB >= Angle.PI_THREE_SECONDS && betaB <= Angle.PI_DOUBLE))
                        && (betaE >= Angle.PI_HALF && betaE <= Angle.PI_THREE_SECONDS)) {

                    linePoint = new TangentPoint();
                    if (currentS.startPoName.equals(FiveStakeType.QD.toString())
                            || currentS.startPoName.equals(FiveStakeType.HZ.toString())) {          //点在直线段内
                        double DistBP = BaseAlgorithms.computTwoPointDistance(new Point2D(currentS.startX, currentS.startY), inputPo);

                        linePoint.continuDk = currentS.startPoDk + DistBP * Math.cos(betaB);
                        linePoint.remark = "点在直线段内";

                    } else if (currentS.startPoName.equals(FiveStakeType.HY.toString())
                            || currentS.startPoName.equals(FiveStakeType.QZ.toString())) {    //点在圆曲线段内
                        double afaBO = firstAzimuth;
                        if (currentS.jdDirection.equals(Orientation.左)) {
                            afaBO = afaBO - Angle.PI_HALF;
                        } else if (currentS.jdDirection.equals(Orientation.右)) {
                            afaBO = afaBO + Angle.PI_HALF;
                        }

                        double CirX = currentS.startX + currentS.startPoRadius * Math.cos(afaBO);
                        double CirY = currentS.startY + currentS.startPoRadius * Math.sin(afaBO);    //圆心坐标

                        double CirAfaS = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(CirX, CirY), new Point2D(currentS.startX, currentS.startY));
                        double CirAfaE = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(CirX, CirY), new Point2D(inputPo.x, inputPo.y));

                        double CirBeta = 0.0;
                        if (currentS.jdDirection.equals(Orientation.左)) {
                            CirBeta = CirAfaS - CirAfaE;
                        } else if (currentS.jdDirection.equals(Orientation.右)) {
                            CirBeta = CirAfaE - CirAfaS;
                        }
                        CirBeta = Angle.formAngle(CirBeta);

                        linePoint.continuDk = currentS.startPoDk + currentS.startPoRadius * CirBeta;
                        linePoint.remark = "点在圆曲线段内";
                    } else if (currentS.startPoName.equals(FiveStakeType.ZH.toString())
                            || currentS.startPoName.equals(FiveStakeType.YH.toString())) {    //点在缓和曲线段内
                        double FindDK = 0.0;
                        FindDK = FindShortestDistPoint(currentS, inputPo, currentS.startPoDk, currentS.nextPoDk, FindDK);
                        linePoint.continuDk = FindDK;
                        linePoint.remark = "点在缓和曲线段内";
                    }

                    TangentPoint TempPI = curveXYAzimuth(currentS, linePoint.continuDk);//线路中心点信息
                    double ToleranceDistBP = BaseAlgorithms.computTwoPointDistance(TempPI, inputPo);

                    if (ToleranceDistBP > ToleranceSD) {
                        continue;
                    }

                    linePoint.x = TempPI.x;
                    linePoint.y = TempPI.y;
                    linePoint.tangentAzimuth = TempPI.tangentAzimuth;
                    break;
                }
            } else if (i == count - 1) {//最后一个
                double EafaBP = BaseAlgorithms.computAzimuthByTwoPoint(currentS.startX, currentS.startY,inputPo.x,  inputPo.y);
                double EbetaB = EafaBP - currentS.startPoAzimuth;
                EbetaB = Angle.formAngle(EbetaB);

                double EDistBP = BaseAlgorithms.computTwoPointDistance(currentS.startX, currentS.startY, inputPo.x, inputPo.y);
                double dk = currentS.startPoDk + Math.abs(EDistBP * Math.cos(EbetaB));  //20141227+绝对值
                if (dk <= currentS.nextPoDk) {
                    TangentPoint TempPI = curveXYAzimuth(currentS, dk);
                    double ToleranceDistBP = BaseAlgorithms.computTwoPointDistance(TempPI.x, TempPI.y, inputPo.x, inputPo.y);
                    if (ToleranceDistBP > ToleranceSD) {
                        continue;
                    }
                    linePoint = new TangentPoint();
                    linePoint.continuDk = dk;
                    linePoint.x = TempPI.x;
                    linePoint.y = TempPI.y;
                    linePoint.tangentAzimuth = TempPI.tangentAzimuth;
                    linePoint.remark = "最后一个曲线内";
                } /*else if (dk > currentS.nextPoDk) {
                    throw new CommonException("点[" + inputPo.name + "]坐标超出曲线范围");
                }*/
            }
        }
        if (null == linePoint) {
            if (broderRegion > 0.0) {//判断点是否在曲线元范围外
                TangentPoint temp = computBroderPoint(stakes, inputPo, broderRegion);
                if (null != temp) {
                    return temp;
                }
            }
        }

        computPointAngDis(linePoint, inputPo);
        return linePoint;
    }

    private static <T extends Point2D> void computPointAngDis(TangentPoint lienCenterPoint, T inputPo) {
        if (null != lienCenterPoint) {
            double ang = lienCenterPoint.tangentAzimuth;//切点方位角
            double detl = BaseAlgorithms.computTwoPointDistance(lienCenterPoint, inputPo);
            double ang2LinePoint = BaseAlgorithms.computAzimuthByTwoPoint(lienCenterPoint, inputPo);//线路中心点到目标点方位角

            if (ang2LinePoint > ang) {//
                detl = -detl;
            }
            lienCenterPoint.p2CenterAzimuth = ang2LinePoint;
            lienCenterPoint.setDetlDis(detl);
        }
    }


    /**
     * 专门处理边界点
     *
     * @param stakes
     * @param inputPo
     * @param broderRegion
     * @param <T>
     * @return
     */
    private static <T extends Point2D> TangentPoint computBroderPoint(List<StakeInfo> stakes, T inputPo, double broderRegion) {

        StakeInfo startS = stakes.get(0);
        Point2D pStart = new Point2D(startS.startX, startS.startY);
        TangentPoint linePoint = null;
        if (stakes.size() == 1) {
            double firstAzimuth = Angle.formAngle(startS.startPoAzimuth);//起点切线方位角\
            double afaBP = BaseAlgorithms.computAzimuthByTwoPoint(pStart, inputPo);//起点到目标点方位角
            double betaB = afaBP - firstAzimuth;//起点到目标点α-起点切线方位角
            betaB = Angle.formAngle(betaB);

            double disStart2IP = BaseAlgorithms.computTwoPointDistance(pStart, inputPo);
            double disFoot = Math.abs(disStart2IP * Math.cos(betaB));


            double azimuth = firstAzimuth;
            int dir = 1;
            boolean sameDir = isSameDir(betaB);
            if (!sameDir) {//和起点信息相反
                azimuth = firstAzimuth - Angle.PI;
                dir = -1;
            }
            Point2D foot = BaseAlgorithms.computPointByDisAndAzimuth(pStart, disFoot, Angle.formAngle(azimuth));
            linePoint = new TangentPoint(new Point3D(foot, null));
            linePoint.continuDk = startS.startPoDk + disFoot * dir;
            double towAzimuth = BaseAlgorithms.computAzimuthByTwoPoint(pStart, foot);
            linePoint.tangentAzimuth = towAzimuth;
            linePoint.remark = "点在起点外";
            computPointAngDis(linePoint, inputPo);
            return linePoint;
        }


        StakeInfo endS = stakes.get(stakes.size() - 1);
        Point2D pEnd = new Point2D(endS.startX, endS.startY);
        double dFirst = BaseAlgorithms.computTwoPointDistance(pStart, inputPo);

        double dEnd = BaseAlgorithms.computTwoPointDistance(pEnd, inputPo);
        dEnd = dEnd - (endS.nextPoDk - endS.startPoDk);

        if (dFirst > broderRegion && dEnd > broderRegion) {
            return null;
        }

        if (dFirst <= broderRegion) {
            double firstAzimuth = Angle.formAngle(startS.startPoAzimuth);//起点切线方位角\
            double afaBP = BaseAlgorithms.computAzimuthByTwoPoint(pStart, inputPo);//起点到目标点方位角
            double betaB = afaBP - firstAzimuth;//起点到目标点α-起点切线方位角
            betaB = Angle.formAngle(betaB);
            //和起点切线不同方向
            if (!isSameDir(betaB)) {
                double disStart2IP = BaseAlgorithms.computTwoPointDistance(pStart, inputPo);
                double disFoot = Math.abs(disStart2IP * Math.cos(betaB));
                Point2D foot = BaseAlgorithms.computPointByDisAndAzimuth(pStart, disFoot, Angle.formAngle(firstAzimuth - Angle.PI));

                linePoint = new TangentPoint(new Point3D(foot, null));
                linePoint.continuDk = startS.startPoDk - disFoot;
                linePoint.tangentAzimuth = BaseAlgorithms.computAzimuthByTwoPoint(foot, pStart);
                linePoint.remark = "点在起点外";
            }
            computPointAngDis(linePoint, inputPo);
            return linePoint;
        }
        if (dEnd <= broderRegion) {
            double secondAzimuth = Angle.formAngle(endS.startPoAzimuth);//终点切线方位角
            double afaEP = BaseAlgorithms.computAzimuthByTwoPoint(pEnd, inputPo);//终点到目标点方位角
            double betaE = afaEP - secondAzimuth;//终点到目标点α-终点切线方位角
            betaE = Angle.formAngle(betaE);

            if (isSameDir(betaE)) {

                double disStart2IP = BaseAlgorithms.computTwoPointDistance(pEnd, inputPo);
                double disFoot = Math.abs(disStart2IP * Math.cos(betaE));
                Point2D foot = BaseAlgorithms.computPointByDisAndAzimuth(pEnd, disFoot, secondAzimuth);

                linePoint = new TangentPoint(new Point3D(foot, null));
                linePoint.tangentAzimuth = BaseAlgorithms.computAzimuthByTwoPoint(foot, pEnd);
                linePoint.continuDk = endS.startPoDk + disFoot;
                linePoint.remark = "点在终点外";
            }
            computPointAngDis(linePoint, inputPo);
            return linePoint;
        }
        return null;
    }

    private static boolean isSameDir(double betaB) {
        if (((betaB >= 0.0 && betaB <= Angle.PI_HALF) || (betaB >= Angle.PI_THREE_SECONDS && betaB <= Angle.PI_DOUBLE))) {
            return true;
        } else {//和起点信息相反
            return false;
        }
    }

    /**
     * 在缓和曲线上通过跌代求解最近的里程
     *
     * @param currentS
     * @param inputPo
     * @param startDk
     * @param endDk
     * @param findDk
     * @return
     */
    private static double FindShortestDistPoint(StakeInfo currentS, Point2D inputPo, double startDk, double endDk, double findDk) {
        double MidDK = (startDk + endDk) / 2;
        TangentPoint StaP = curveXYAzimuth(currentS, startDk);
        TangentPoint MidP = curveXYAzimuth(currentS, MidDK);
        TangentPoint EndP = curveXYAzimuth(currentS, endDk);

        double afaSP = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(StaP.x, StaP.y), new Point2D(inputPo.x, inputPo.y));
        double afaMP = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(MidP.x, MidP.y), new Point2D(inputPo.x, inputPo.y));
        double afaEP = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(EndP.x, EndP.y), new Point2D(inputPo.x, inputPo.y));


        double SbetaB = afaSP - StaP.tangentAzimuth;
        SbetaB = Angle.formAngle(SbetaB);


        double SbetaM = afaMP - MidP.tangentAzimuth;
        SbetaM = Angle.formAngle(SbetaM);

        double SbetaE = afaEP - EndP.tangentAzimuth;
        SbetaE = Angle.formAngle(SbetaE);

        if (((SbetaB >= 0.0 && SbetaB <= Angle.PI_HALF) || (SbetaB >= Angle.PI_THREE_SECONDS && SbetaB <= Angle.PI_DOUBLE))
                && (SbetaM >= Angle.PI_HALF && SbetaM <= Angle.PI_THREE_SECONDS)) {
            double DetaSEDK = Math.abs(startDk - MidDK);
            if (DetaSEDK < DoubleUtils.MIN_DIS / 100) {
                findDk = (startDk + MidDK) / 2.0;
            } else {
                findDk = FindShortestDistPoint(currentS, inputPo, startDk, MidDK, findDk);
            }
        }
        if (((SbetaM >= 0.0 && SbetaM <= Angle.PI_HALF) || (SbetaM >= Angle.PI_THREE_SECONDS && SbetaM <= Angle.PI_DOUBLE))
                && (SbetaE >= Angle.PI_HALF && SbetaE <= Angle.PI_THREE_SECONDS)) {
            double DetaSEDK = Math.abs(MidDK - endDk);
            if (DetaSEDK < DoubleUtils.MIN_DIS / 100) {
                findDk = (MidDK + endDk) / 2.0;
            } else {
                findDk = FindShortestDistPoint(currentS, inputPo, MidDK, endDk, findDk);
            }
        }
        return findDk;
    }

    static TangentPoint cumputXyByDk(List<StakeInfo> curveMetadatas, double dk, double offsetDis) {
        StakeInfo curveMetadata = null;
        for (int i = 0; i < curveMetadatas.size(); i++) {
            if (curveMetadatas.get(i).startPoDk <= dk && curveMetadatas.get(i).nextPoDk >= dk) {
                curveMetadata = curveMetadatas.get(i);//所在曲线
                break;
            }
        }
        if (null == curveMetadata) {
            throw new CommonException("当前里程不在曲线内");
        }

        TangentPoint tangentPoint = curveXYAzimuth(curveMetadata, dk);
        double offSetAng = tangentPoint.tangentAzimuth;
        if (offsetDis < 0.0) {
            offSetAng -= Math.PI / 2;
        }
        if (offsetDis > 0.0) {
            offSetAng += Math.PI / 2;
        }
        //根据偏距计算实际坐标
        Point2D lPoint = BaseAlgorithms.computPointByDisAndAzimuth(tangentPoint, offsetDis, offSetAng);
        tangentPoint.x = lPoint.x;
        tangentPoint.y = lPoint.y;
        return tangentPoint;
    }


    /**
     * 根据曲线元要素，及其上一点的里程，求该点的坐标，及切线方位角"
     *
     * @param Element
     * @param dk
     * @return
     */
    static TangentPoint curveXYAzimuth(StakeInfo Element, double dk) {
        return CrossoverPoint2Stake.curveXYAzimuth(Element, dk);
    }


}
