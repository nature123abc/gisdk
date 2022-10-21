package com.dk.microgis.line.curve.cruve1.entity;


import com.dk.common.DoubleUtils;
import com.dk.microgis.base.Point2D;
import com.dk.microgis.line.curve.curve.common.FiveStakeType;
import com.dk.microgis.line.curve.curve.common.entity.Orientation;
import com.dk.microgis.line.curve.curve.common.entity.PointType;
import com.dk.microgis.math.Angle;
import com.dk.microgis.math.BaseAlgorithms;

/**
 * @author hq
 * @date 2021-09-06 16:37
 * @desc 线元法计算要素，一个完整曲线要素；
 * 参考：《测绘程序设计》 李英冰
 * 武汉大学测绘学院
 * https://wenku.baidu.com/view/ad4cef3cb94ae45c3b3567ec102de2bd9605deff.html
 * <p>
 * https://wenku.baidu.com/view/d00d9e62e45c3b3567ec8b8f.html
 * https://www.sohu.com/a/164486053_583961
 * https://www.xueceliang.cn/ce/yqxdyhhqxdqxzbzzbjsff.html  良心
 * https://www.sohu.com/a/164486053_583961   曲线要素坐标反算公式
 * <p>
 * 验证软件《道路曲线要素计算》
 */
public class RoadElement {
    public BaseJdPoint startP;//起点,如果是多个曲线元，则是上一个曲线元的HZ点，需要将前一个曲线的HZ点转换到该点
    public BaseJdPoint endP;//结束点 ，开始是下一个曲线元的JD，计算完成后转为HZ点,如果是最后一个曲线元，则是ZD  标记一个完整曲线元
    public Double ly;//圆曲线长
    public Double l;//曲线总长 = 圆曲线+缓和曲线长
    public Double t;//曲线切线长 从ZH点到JD连线
    public Double t2;//曲线切线长 从HZ点到JD连线
    public Double q;//切曲差,两条切线长和曲线长差值
    public Double ev;//外矢距  曲线中点到曲线切线交点的距离


    public Double m;//切垂距m=圆曲线圆心向切线所做垂足到（ZH，HZ）距离
    public Double m2;//切垂距m
    public Double b;//切线角 l/2r  缓和曲线长度/2r
    public Double b2;//切线角 l/2r  缓和曲线长度/2r
    public Double p;//内移距p,ZH到JD位置连线和HY位置切线距离
    public Double p2;//内移距p
    public Double a;//实际转角
    public Double r;//半径
    public Double ls;//缓和曲线长
    public Double ls2;//缓和曲线长
    public Double a1;//起点到交点方位角
    public Double a2;//交点到结束点方位角
    public boolean isZY;//是否是直圆类型曲线

    public BaseJdPoint zh;
    public BaseJdPoint hy;
    public BaseJdPoint qz;
    public BaseJdPoint yh;
    public BaseJdPoint hz;
    public BaseJdPoint jd;

    public Orientation or;//偏向

    public RoadElement() {
    }

    public RoadElement(BaseJdPoint startP, BaseJdPoint jd, BaseJdPoint endP) {
        this.startP = new BaseJdPoint(startP);
        this.jd = new BaseJdPoint(jd);
        this.endP = new BaseJdPoint(endP);
        this.ls = jd.ls1;
        this.ls2 = jd.ls2;
        this.r = jd.r;
    }

    public RoadElement(BaseJdPoint startP, BaseJdPoint endP) {
        this.startP = new BaseJdPoint(startP);
        this.endP = new BaseJdPoint(endP);
    }

    //region 交点法计算
    public void comput() {
        if (null == jd) {
            init0();
            return;
        }
        init1();
    }

    private void init0() {
        double a1 = azimuth(startP, endP);
        startP.azimuth = a1;
        endP.azimuth = a1;

        endP.dk = startP.dk + distence(startP, endP);
    }

    private void init1() {
        zh = new BaseJdPoint();
        hy = new BaseJdPoint();
        qz = new BaseJdPoint();
        yh = new BaseJdPoint();
        hz = new BaseJdPoint();
        if (ls < DoubleUtils.MIN_DIS && ls2 < DoubleUtils.MIN_DIS) {
            isZY = true;
        }

        //计算直线方位角
        a1 = azimuth(startP, jd);
        a2 = azimuth(jd, endP);
        double detl = a2 - a1;
        startP.azimuth = a1;
        endP.azimuth = a2;

        //计算转向角
        a = detl;
        detl = Angle.formAngle(detl);

        if (a > Angle.PI) {
            a = a - Angle.PI_DOUBLE;
        }
        // System.out.println("偏角" + Angle.RAD2DMS(a));
        if (a < -Angle.PI) {
            a = a + Angle.PI_DOUBLE;
        }

        if (detl >= 0.0 && detl <= Angle.PI) {
            or = Orientation.右;
        } else if (detl >= Angle.PI && detl <= Angle.PI_DOUBLE) {
            or = Orientation.左;
        }
        //算切垂距𝒎𝟏、𝒎𝟐、内移距𝒑𝟏、
        computCurve();

        //***************************计算里程
        jd.dk = startP.dk + distence(startP, jd);//交点里程
        //ZH
        zh.name = FiveStakeType.ZH.name();
        zh.dk = jd.dk - t;
        zh.r = 0.0 * computk(or);

        //HY
        hy.name = FiveStakeType.HY.name();
        hy.dk = zh.dk + ls;
        if (isZY) {
            hy.name = FiveStakeType.ZY.name();
        }
        hy.r = r * computk(or);

        //QZ
        qz.name = FiveStakeType.QZ.name();
        qz.dk = hy.dk + ly / 2.0; //缓和曲线dk+圆曲线一半
        qz.r = r * computk(or);
        //YH
        yh.name = FiveStakeType.YH.name();
        yh.dk = zh.dk + l - ls2;
        if (isZY) {
            yh.name = FiveStakeType.YZ.name();
        }
        yh.r = r * computk(or);

        //HZ
        hz.name = FiveStakeType.HZ.name();
        hz.dk = yh.dk + ls2;
        hz.r = 0.0 * computk(or);


        //***************************计算坐标
        //ZH坐标，方位角
        BaseJdPoint pZh = computQD2ZH(t);
        zh.x = pZh.x;
        zh.y = pZh.y;
        zh.azimuth = pZh.azimuth;//切线方位角=起点到交点方位角

        // curvPt.x = ls - (Math.pow(ls, 3) / (40 * Math.pow(r, 2))) + Math.pow(ls, 5) / (3456 * Math.pow(r, 4));//p点在ZH-HY坐标系上坐标
        //HY坐标，方位角

        BaseJdPoint pHY = null;//缓和点 点坐标
        if (isZY) {
            pHY= new BaseJdPoint(zh.x, zh.y);
        }else{
            pHY = computZH2HY(ls);//缓和点 点坐标
            pHY = convertCoor(pHY);
        }

        hy.x = pHY.x;
        hy.y = pHY.y;
        hy.azimuth = computZh2HYAng(ls);
        //切线方位角=zh方位角+切线角


        //QZ坐标，方位角
        BaseJdPoint pQZ = computHY2YH(qz.dk - zh.dk);
        pQZ = convertCoor(pQZ);
        qz.x = pQZ.x;
        qz.y = pQZ.y;
        qz.azimuth = pQZ.azimuth;//切线方位角=(hy方位角+yh方位角)/2


        //HZ坐标，方位角,先算hz点再计算YH点，
        BaseJdPoint pHZ = computHZD(t2);
        hz.x = pHZ.x;
        hz.y = pHZ.y;
        hz.azimuth = computYH2HZAng(0.0);//切线方位角=交点到终点方位角

        //YH坐标，方位角
        BaseJdPoint pYH = null;
        if (isZY) {
            pYH = new BaseJdPoint(hz);
        } else {
            pYH = computHY2YH(yh.dk - zh.dk);
            pYH = convertCoor(pYH);
        }
        yh.x = pYH.x;
        yh.y = pYH.y;
        yh.azimuth = pYH.azimuth;//切线方位角=hz方位角-切线角


        //曲线元计算完成
        endP.dk = hz.dk + distence(hz, endP);//设置 终点里程
        if (PointType.交点.equals(endP.pointType)) {//曲线结束点变为HZ点，如果最后一个曲线元，则是ZD
            endP = new BaseJdPoint(hz);
        }
        if (null == startP.r) {
            startP.r = 0.0;
        }
        if (null == endP.r) {
            endP.r = 0.0;
        }
    }

    private BaseJdPoint computYH(double v) {
        if (isZY) {

        }
        return computHY2YH(yh.dk - zh.dk);
    }


    /**
     * @param t2 到JD的距离
     * @return 缓直点坐标
     */
    private BaseJdPoint computHZD(Double t2) {
        BaseJdPoint bj = new BaseJdPoint();
        bj.x = jd.x + t2 * Math.cos(a2);
        bj.y = jd.y + t2 * Math.sin(a2);
        bj.azimuth = a2;//切线方位角=交点到终点方位角
        return bj;
    }


    //计算第二缓和曲线内点方位角，l=到缓直点距离
    private Double computYH2HZAng(double l) {
        if (DoubleUtils.isZero(ls2)) {
            return a2;
        }
        return a2 - computk(or) * Math.pow(l, 2) / (2.0 * ls2 * r);
    }


    /**
     * 计算起点到ZH点位置点坐标
     *
     * @param t
     * @return
     */
    private BaseJdPoint computQD2ZH(Double t) {
        return computPointByBase(jd, -t, a1);
    }

    //基础点之前，

    /**
     * @param basePoint     基础点
     * @param tarP2Base     目标点到基础点距离，存在正反，需要计算目标点-基础点里程差
     * @param angPoint2Base 基础点和目标点线路走向方位角
     * @return
     */
    private BaseJdPoint computPointByBase(BaseJdPoint basePoint, double tarP2Base, double angPoint2Base) {
        double x = basePoint.x + tarP2Base * Math.cos(angPoint2Base);
        double y = basePoint.y + tarP2Base * Math.sin(angPoint2Base);
        BaseJdPoint b = new BaseJdPoint(x, y);
        b.azimuth = Angle.formAngle(angPoint2Base);//切线方位角=起点到交点方位角
        return b;
    }


    /**
     * @param s2ZH 圆曲线上点到ZH里程差
     * @return
     */
    private BaseJdPoint computHY2YH(double s2ZH) {

        double β = (2.0 * s2ZH - ls) / (2 * r);
        double x = m + r * Math.sin(β);//p点在HY-YH坐标系上坐标
        double y = p + r * (1 - Math.cos(β));

        BaseJdPoint b = new BaseJdPoint(x, y);
        b.azimuth = computHY2YHAng(s2ZH);
        return b;
    }

    private double computHY2YHAng(double s2ZH) {
        if (isZY) {
            return zh.azimuth;
        }
        return zh.azimuth + computk(or) * (2.0 * s2ZH - ls) / (2.0 * r);
    }

    //曲线内坐标转换
    private BaseJdPoint convertCoor(BaseJdPoint turnPoing) {
        double aerf = a1;
        BaseJdPoint p1 = new BaseJdPoint(turnPoing);
        p1.x = zh.x + turnPoing.x * Math.cos(aerf) - computk(or) * turnPoing.y * Math.sin(aerf);
        p1.y = zh.y + turnPoing.x * Math.sin(aerf) + computk(or) * turnPoing.y * Math.cos(aerf);
        return p1;
    }


    /**
     * 计算直缓上的坐标方程 第一缓和曲线上信息
     *
     * @param s2ZH 缓和曲线上点，到zh点距离
     * @return
     */
    private BaseJdPoint computZH2HY(Double s2ZH) {

        return computHuanhe(s2ZH, ls);
    }

    //计算缓和曲线上点，s2ZH距离直线点距离
    private BaseJdPoint computHuanhe(Double s2ZH, double ls) {
        double x = s2ZH
                - Math.pow(s2ZH, 5) / (40.0 * Math.pow(r, 2) * Math.pow(ls, 2))
                + Math.pow(s2ZH, 9) / (3456 * Math.pow(r, 4) * Math.pow(ls, 4));
        double y = Math.pow(s2ZH, 3) / (6 * r * ls)
                - Math.pow(s2ZH, 7) / (336.0 * Math.pow(r, 3) * Math.pow(ls, 3))
                + Math.pow(s2ZH, 11) / (42240.0 * Math.pow(r, 5) * Math.pow(ls, 5));
        return new BaseJdPoint(x, y);
    }

    //第二缓和曲线内坐标转换
    private BaseJdPoint convertCoorHZ(BaseJdPoint turnPoing) {
        double aerf = a2;
        BaseJdPoint p1 = new BaseJdPoint();
        p1.x = hz.x - turnPoing.x * Math.cos(aerf) - computk(or) * turnPoing.y * Math.sin(aerf);
        p1.y = hz.y - turnPoing.x * Math.sin(aerf) + computk(or) * turnPoing.y * Math.cos(aerf);
        return p1;
    }

    //第一缓和曲线方位角
    private double computZh2HYAng(Double s2ZH) {
        if (DoubleUtils.isZero(ls)) {
            return zh.azimuth;
        }
        return zh.azimuth + computk(or) * Math.pow(s2ZH, 2.0) / (2 * ls * r);
    }

    private int computk(Orientation or) {
        return Orientation.左.equals(or) ? -1 : 1;
    }

    //计算曲线要素信息
    private void computCurve() {
        a = Math.abs(a);//偏角

        //切垂距 �
        m = ls / 2.0 - Math.pow(ls, 3.0) / (240 * Math.pow(r, 2)) + Math.pow(ls, 5.0) / (34560 * Math.pow(r, 4));//切垂距m
        m = computM(ls);//切垂距m
        m2 = computM(ls2);//切垂距m

        //内移距 �
        p = computP(ls);
        p2 = computP(ls2);//内移距p

        //切线长
        t = m + (((r + p2) - (r + p) * Math.cos(a)) / Math.sin(a));//曲线切线长
        t2 = m2 + (((r + p) - (r + p2) * Math.cos(a)) / Math.sin(a));//曲线切线长

        //缓和曲线角
        b = ls / (2.0 * r);//切线角 l/2r
        b2 = ls2 / (2.0 * r);//切线角 l/2r
        ly = (a) * r - ls / 2.0 - ls2 / 2.0;//圆曲线长
        l = ly + ls + ls2;//曲线总长（缓和曲线+元曲线长）
        //切曲差
        q = t + t2 - l;
        //外失距
        ev = (r + (p + p2) / 2.0) * (1.0 / Math.cos(a / 2.0)) - r;
    }

    private Double computP(Double ls) {
        return Math.pow(ls, 2.0) / (24.0 * r)
                - Math.pow(ls, 4.0) / (2688 * Math.pow(r, 3))
                + Math.pow(ls, 6.0) / (506880 * Math.pow(r, 5))
                - Math.pow(ls, 8.0) / (154828800.0 * Math.pow(r, 7))
                + Math.pow(ls, 10.0) / (18579456 * 3800.0 * Math.pow(r, 9))
                - Math.pow(ls, 12.0) / (37158912 * 50600.0 * Math.pow(r, 11))
                ;//内移距p

    }

    private Double computM(Double ls) {
        return ls / 2.0
                - Math.pow(ls, 3.0) / (240 * Math.pow(r, 2))
                + Math.pow(ls, 5.0) / (34560.0 * Math.pow(r, 4))
                - Math.pow(ls, 7.0) / (8386560.0 * Math.pow(r, 6))
                + Math.pow(ls, 9.0) / (3158507520.0 * Math.pow(r, 8))
                - Math.pow(ls, 11.0) / (37158912.0 * 46200 * Math.pow(r, 10))
                ;//切垂距m;
    }

    private Double distence(BaseJdPoint startP, BaseJdPoint jd) {
        return BaseAlgorithms.computTwoPointDistance(new Point2D(startP.x, startP.y), new Point2D(jd.x, jd.y));
    }

    private Double azimuth(BaseJdPoint startP, BaseJdPoint jd) {
        return BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(startP.x, startP.y), new Point2D(jd.x, jd.y));
    }
    //endregion


    //region  里程计算坐标

    /**
     * 根据里程计算线路中心点坐标
     *
     * @param dk 里程
     * @return
     */


    public LinePoint dkComputXy(double dk) {
        double superH = computeSuperH(this, dk);
        LinePoint linePoint = new LinePoint();
        linePoint.continuDk = dk;
        linePoint.offDis = 0.0;
        linePoint.offAng = 0.0;
        linePoint.superH = superH;
        if (null == jd) {//直线
            BaseJdPoint bp = computPointByBase(startP, dk - startP.dk, startP.azimuth);
            linePoint.x = bp.x;
            linePoint.y = bp.y;
            linePoint.azimuth = bp.azimuth;
            return linePoint;
        }
        double detlDk = dk - zh.dk;
        if (dk <= zh.dk) {//QD-ZH
            BaseJdPoint bp = computPointByBase(zh, dk - zh.dk, zh.azimuth);
            linePoint.x = bp.x;
            linePoint.y = bp.y;
            linePoint.azimuth = bp.azimuth;
            return linePoint;
        }
        if (dk <= hy.dk) {//ZH-HY
            BaseJdPoint bj = computZH2HY(detlDk);
            bj = convertCoor(bj);
            linePoint.x = bj.x;
            linePoint.y = bj.y;
            linePoint.azimuth = computZh2HYAng(detlDk);
            return linePoint;
        }
        if (dk <= yh.dk) {//HY-YH
            BaseJdPoint bj = computHY2YH(detlDk);
            bj = convertCoor(bj);
            linePoint.x = bj.x;
            linePoint.y = bj.y;
            linePoint.azimuth = bj.azimuth;
            return linePoint;
        }

        if (dk <= hz.dk) {//YH-HZ
            detlDk = hz.dk - dk;
            BaseJdPoint bj = computYH2HZ(detlDk);
            bj = convertCoorHZ(bj);
            linePoint.x = bj.x;
            linePoint.y = bj.y;
            linePoint.azimuth = computYH2HZAng(detlDk);
            return linePoint;
        }
        //HZ点之后的点
        detlDk = dk - hz.dk;
        BaseJdPoint bj = computPointByBase(hz, detlDk, hz.azimuth);
        linePoint.x = bj.x;
        linePoint.y = bj.y;
        linePoint.azimuth = bj.azimuth;
        return linePoint;
    }


    /**
     * 根据曲线元，计算超高
     *
     * @param element
     * @return 指定里程位置超高
     */
    public static double computeSuperH(RoadElement element, double dk) {
        double superH = 0.0;
        if (dk > element.zh.dk && dk <= element.hy.dk) {
            return (dk - element.zh.dk) * element.jd.maxh / (element.hy.dk - element.zh.dk);
        }
        if (dk > element.hy.dk && dk <= element.yh.dk) {
            return element.jd.maxh;
        }
        if (dk > element.yh.dk && dk <= element.hz.dk) {
            return element.jd.maxh - (dk - element.yh.dk) * element.jd.maxh / (element.yh.dk - element.hz.dk);
        }
        return superH;
    }

    /**
     * 根据里程，偏角，偏距计算点
     *
     * @param dk
     * @param offDis 全是正值，使用角度控制方向
     * @param offAng 偏移角度（弧度) 左-，右+
     * @return
     */
    public LinePoint dkComputXy(double dk, double offDis, double offAng) {
        offDis = Math.abs(offDis);
        LinePoint linePoint = dkComputXy(dk);
        double ang = linePoint.azimuth + offAng;
        Point2D offP = BaseAlgorithms.computPointByDisAndAzimuth(new Point2D(linePoint.x, linePoint.y), offDis, ang);
        linePoint.x = offP.x;
        linePoint.y = offP.y;
        offAng = Angle.formAngle(offAng);
        double sign = 1;
        if (offAng >= Angle.PI && offAng <= Angle.PI_DOUBLE) {
            sign = -1;
        }
        linePoint.offDis = offDis * sign;
        linePoint.offAng = offAng;
        return linePoint;
    }

    /**
     * @param dk     里程
     * @param offDis 偏距，带正负号
     * @return
     */
    public LinePoint dkComputXy(double dk, double offDis) {
        double offAng = offDis < 0.0 ? -Angle.PI_HALF : Angle.PI_HALF;
        return dkComputXy(dk, offDis, offAng);
    }


    /**
     * 第二缓和曲线内计算，
     *
     * @param hz2S 距离HZ点里程差
     * @return
     */
    private BaseJdPoint computYH2HZ(double hz2S) {
        if (DoubleUtils.isZero(ls2)) {//第二缓和曲线为0表示，该点就是HZ点
            return new BaseJdPoint(hz.x, hz.y);
        }
        return computHuanhe(hz2S, ls2);
    }

    //endregion

    //region 坐标计算里程

    private LinePoint xy2DkNotJd(double dk) {
        LinePoint linePoint = new LinePoint();
        BaseJdPoint bp = computQD2ZH(dk - startP.dk);
        linePoint.x = bp.x;
        linePoint.y = bp.y;
        linePoint.azimuth = bp.azimuth;
        return linePoint;
    }

    private void setOffInfo(LinePoint linePoint) {
        LinePoint lp = dkComputXy(linePoint.continuDk);//根据里程计算坐标，线路上点
        linePoint.azimuth = lp.azimuth;
        double offDis = BaseAlgorithms.computTwoPointDistance(linePoint, lp);//偏距

        double ang = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(lp.x, lp.y), new Point2D(linePoint.x, linePoint.y));//线路中心点到当前点方位角
        ang = Angle.formAngle(ang);

        offDis = offDis * BaseAlgorithms.computPointLeftRightLine(lp.azimuth, ang);
        linePoint.offDis = offDis;
        double angCent2Line = ang;//线路到边界点方位角
        linePoint.offAng = angCent2Line - lp.azimuth;//线路偏移点偏角
        linePoint.x = lp.x;
        linePoint.y = lp.y;
    }

    private BaseJdPoint findNearPoint(BaseJdPoint start, Point2D p, BaseJdPoint end) {

        double centDk = (start.dk + end.dk) / 2.0;
        double detlDk = end.dk - start.dk;

        LinePoint ppp = dkComputXy(centDk);//中间点
        BaseJdPoint centP = new BaseJdPoint(ppp.x, ppp.y);//中间点
        centP.azimuth = ppp.azimuth;
        centP.dk = ppp.continuDk;

        if (Math.abs(detlDk) < DoubleUtils.MIN_DIS / 100.0) {
            return centP;
        }

        boolean startPCent = pointInElement(start, p, centP);//起点和中间点之间
        boolean centPEnd = pointInElement(centP, p, end);//
        if (startPCent) {
            return findNearPoint(start, p, centP);
        } else if (centPEnd) {
            return findNearPoint(centP, p, end);
        }
        return centP;
    }

    private static boolean isSameDir(double ang) {
        ang = Angle.formAngle(ang);
        if ((ang <= Angle.PI_HALF && ang >= 0.0) || (ang >= Angle.PI_THREE_SECONDS && ang <= Angle.PI_DOUBLE)) {
            return true;
        }
        return false;
    }


    private double computDkByBaseAng(double detlAng, double dis) {
        return dis * Math.cos(detlAng);
    }


    private LinePoint computZQBack(BaseJdPoint hz, LinePoint linePoint) {
        //计算终点之后
        Point2D point2D = new Point2D(linePoint.x, linePoint.y);
        double angEnd2Point = BaseAlgorithms.computAzimuthByTwoPoint(hz.toPoint2D(), point2D);
        double baseAng = hz.azimuth;
        double detl2End = angEnd2Point - baseAng;

        double dis = BaseAlgorithms.computTwoPointDistance(hz.toPoint2D(), point2D);
        double detlDk = computDkByBaseAng(detl2End, dis);
        double dk = hz.dk + detlDk;
        linePoint.continuDk = dk;
        linePoint.remark = "在ZH点之后点";
        setOffInfo(linePoint);
        return linePoint;
    }

    //计算起点之前
    private LinePoint computQDFornt(BaseJdPoint startP, LinePoint linePoint) {

        Point2D point2D = new Point2D(linePoint.x, linePoint.y);
        double angStart2Point = BaseAlgorithms.computAzimuthByTwoPoint(startP.toPoint2D(), point2D);
        double baseAng = startP.azimuth - Angle.PI;
        double detl2Start = angStart2Point - baseAng;

        double dis = BaseAlgorithms.computTwoPointDistance(startP.toPoint2D(), point2D);
        double detlDk = computDkByBaseAng(Angle.formAngle(detl2Start), dis);
        double dk = startP.dk - detlDk;
        linePoint.continuDk = dk;
        linePoint.remark = "QD或者HZ点之前";

        setOffInfo(linePoint);
        return linePoint;
    }

    private LinePoint xy2DkNotJd(Point2D point2D) {
        LinePoint linePoint = new LinePoint(point2D.x, point2D.y, null, null);

        double angStart2Point = BaseAlgorithms.computAzimuthByTwoPoint(startP.toPoint2D(), point2D);

        boolean startBackP = isSameDir(angStart2Point - startP.azimuth);//开始点之后的点
        if (startBackP) {
            return computZQBack(startP, linePoint);
        }
        return computQDFornt(startP, linePoint);
    }

    /**
     * 判断点是否在该大曲线元中
     *
     * @param startP
     * @param point2D
     * @param endP
     * @return
     */
    static boolean pointInElement(BaseJdPoint startP, Point2D point2D, BaseJdPoint endP) {
        if (null == startP || null == endP) {
            return false;//没有起终点，表示该曲线元不完整
        }

        double angStart2Point = BaseAlgorithms.computAzimuthByTwoPoint(startP.toPoint2D(), point2D);
        double detl2Start = angStart2Point - startP.azimuth;
        boolean same2Start = isSameDir(detl2Start);

        double angEnd2Point = BaseAlgorithms.computAzimuthByTwoPoint(endP.toPoint2D(), point2D);
        double detl2End = angEnd2Point - (endP.azimuth - Angle.PI);//反向一下
        boolean same2End = isSameDir(detl2End);
        if (same2Start && same2End) {
            return true;
        }
        return false;
    }


    /**
     * 点在该曲线元内判断
     *
     * @param point2D
     * @return
     */
    public boolean pointInElement(Point2D point2D) {
        return pointInElement(startP, point2D, endP);
    }

    /**
     * 判断该里程是否在该曲线元内
     *
     * @param dk
     * @return
     */
    public boolean dkInElement(Double dk) {
        return (dk <= endP.dk && dk >= startP.dk);
    }


    /**
     * 根据坐标计算该点在线路上里程、偏距、方位角信息
     *
     * @param point2D
     * @return
     */
    public LinePoint xyComputDK(Point2D point2D) {
        if (null == jd) {//直线
            return xy2DkNotJd(point2D);
        }

        LinePoint linePoint = new LinePoint(point2D.x, point2D.y, null, null);
        //找是否在该小的曲线元内
        boolean sameAngHZ2ZH = pointInElement(startP, point2D, zh);
        if (sameAngHZ2ZH) {//在直缓内QD-ZH
            double dis = BaseAlgorithms.computTwoPointDistance(startP.toPoint2D(), point2D);
            double angStart2Cent = BaseAlgorithms.computAzimuthByTwoPoint(startP.toPoint2D(), point2D);

            double detlAng = angStart2Cent - startP.azimuth;
            double detlDk = dis * Math.cos(detlAng);

            double dk = startP.dk + detlDk;
            linePoint.continuDk = dk;
            linePoint.remark = "在直缓内QD-ZH";
            setOffInfo(linePoint);
            return linePoint;
        }

        boolean sameAngZH2HY = pointInElement(zh, point2D, hy);
        if (sameAngZH2HY) {//缓和曲线上ZH-HY
            double dk = computDkOffDis(zh, hy, point2D);
            linePoint.continuDk = dk;
            linePoint.remark = "缓和曲线上ZH-HY";
            setOffInfo(linePoint);
            return linePoint;
        }
        boolean sameAngHY2YH = pointInElement(hy, point2D, yh);
        if (sameAngHY2YH) {//圆曲线上HY-YH
            double afaBO = hy.azimuth;

            afaBO = afaBO + Angle.PI_HALF * computk(or);
            double cirX = hy.x + r * Math.cos(afaBO);
            double cirY = hy.y + r * Math.sin(afaBO);    //圆心坐标

            double cirafas = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(cirX, cirY), hy.toPoint2D());
            double cirafae = BaseAlgorithms.computAzimuthByTwoPoint(new Point2D(cirX, cirY), point2D);

            double cirBeta = or.equals(Orientation.左) ? cirafas - cirafae : cirafae - cirafas;
            cirBeta = Angle.formAngle(cirBeta);

            double dk = hy.dk + r * cirBeta;
            linePoint.continuDk = dk;

            linePoint.remark = "圆曲线上HY-YH";
            setOffInfo(linePoint);
            return linePoint;
        }
        boolean sameAngYH2HZ = pointInElement(yh, point2D, hz);
        if (sameAngYH2HZ) {//缓和曲线上YH-HZ
            double dk = computDkOffDis(yh, hz, point2D);
            linePoint.continuDk = dk;
            linePoint.remark = "缓和曲线上YH-HZ";
            setOffInfo(linePoint);
            return linePoint;
        }
        boolean sameAngHZ2ZD = pointInElement(hz, point2D, endP);
        if (sameAngHZ2ZD) {//直线上HZ-ZD
            double dis = BaseAlgorithms.computTwoPointDistance(hz.toPoint2D(), point2D);

            double angEnd2Point = BaseAlgorithms.computAzimuthByTwoPoint(hz.toPoint2D(), point2D);
            double detl2End = angEnd2Point - hz.azimuth;
            double detlDk = computDkByBaseAng(detl2End, dis);

            double dk = hz.dk + detlDk;
            linePoint.continuDk = dk;
            linePoint.remark = "直线上HZ-ZD";
            setOffInfo(linePoint);
            return linePoint;
        }

        double angStart2Point = BaseAlgorithms.computAzimuthByTwoPoint(startP.toPoint2D(), point2D);
        double baseAng = startP.azimuth - Angle.PI;
        boolean same2Start = isSameDir(angStart2Point - baseAng);
        if (same2Start) {//在QD或者HZ点之前
            return computQDFornt(startP, linePoint);
        }
        double angEnd2Point = BaseAlgorithms.computAzimuthByTwoPoint(hz.toPoint2D(), point2D);
        boolean same2End = isSameDir(angEnd2Point - hz.azimuth);
        if (same2End) {  //在ZH点之后点 ,如果在HZ到ZQ，上面已经计算
            return computZQBack(hz, linePoint);
        }
        return null;
    }

    private double computDkOffDis(BaseJdPoint zh, BaseJdPoint hy, Point2D point2D) {
        double x = point2D.x;
        double y = point2D.y;
        double l0 = (y - zh.y) * Math.cos(zh.azimuth - Angle.PI_HALF) - (x - zh.x) * Math.sin(zh.azimuth - Angle.PI_HALF);
        double d = 0.001;

        double dk = 0.0;
        while (Math.abs(d) > DoubleUtils.MIN_DIS / 10.0) {
            dk = zh.dk + l0;
            BaseJdPoint bj = GetStaCoor(zh, hy, dk);

            double k = zh.azimuth - Angle.PI_HALF + l0 * (computRo(zh.r) + (computRo(hy.r) - computRo(zh.r)) * l0 / 2 / (hy.dk - zh.dk));
            d = (y - bj.y) * Math.cos(k) - (x - bj.x) * Math.sin(k);
            l0 = l0 + d;
        }
        return dk;
    }

    /**
     * 求线元内任意长度L内的坐标及切线方位角，采用Gauss-Legendre公式计算
     *
     * @param startP
     * @param endP
     * @param dk
     * @return
     */
    private BaseJdPoint GetStaCoor(BaseJdPoint startP, BaseJdPoint endP, double dk) {

        double[][] f = new double[2][4];
        double l = dk - startP.dk;
        double ll = endP.dk - startP.dk;

        BaseJdPoint sta1 = new BaseJdPoint();

        if (Math.abs(ll) < DoubleUtils.MIN_DIS / 1000.0) {
            return new BaseJdPoint(startP);
        } else {
            f[0][0] = 0.0694318442;
            f[0][1] = 0.3300094782;
            f[0][2] = 1 - f[0][1];
            f[0][3] = 1 - f[0][0];

            f[1][0] = 0.1739274226;
            f[1][1] = 0.3260725774;
            f[1][2] = f[1][1];
            f[1][3] = f[1][0];

            double a0 = startP.azimuth;
            double c0 = computRo(startP.r);
            double c1 = computRo(endP.r);

            double h = (c1 - c0) * l * l / (2.0 * ll);

            double S1 = 0.0;
            double S2 = 0.0;
            for (int i = 0; i < 4; i++) {
                S1 = S1 + f[1][i] * Math.cos(a0 + c0 * l * f[0][i] + h * f[0][i] * f[0][i]);
                S2 = S2 + f[1][i] * Math.sin(a0 + c0 * l * f[0][i] + h * f[0][i] * f[0][i]);
            }
            sta1.x = startP.x + l * S1;
            sta1.y = startP.y + l * S2;
        }
        return sta1;
    }


    private double computR(Double r) {
        if (null == r || r == 0) {
            return Math.pow(10, 45);
        }
        return r;
    }

    private double computRo(Double r) {
        return Math.abs(r) < DoubleUtils.MIN_VALUE ? 0.0 : 1 / r;
    }
    //endregion


}
