package com.dk.microgis.line.curve.cruve1;

import com.dk.microgis.base.Point2D;
import com.dk.microgis.line.curve.cruve1.comm.Jd2RoadElement;
import com.dk.microgis.line.curve.cruve1.entity.BaseJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.CommonJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.LinePoint;
import com.dk.microgis.line.curve.cruve1.entity.RoadElement;
import com.dk.microgis.math.Angle;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hq
 * @date 2021-09-07 12:22
 * @desc
 */
public class Test {
    public static void main(String[] args) {

        double ang = Angle.RAD2DMS(Angle.PI);
        System.out.println(ang);
      /*  test1();

         test2();*/
        test2();
    }

    private static void test2() {
        BaseJdPoint start1 = new BaseJdPoint("PS", 3547930.1568248, 522921.9168976);
        start1.dk = 362999.999862581;




        BaseJdPoint jd1 = new BaseJdPoint("JD1", 3546134.5455418, 515784.4556015, 7996.0, 260.0, 260.0);
        BaseJdPoint jd2 = new BaseJdPoint("JD2", 3545402.9651687, 509501.7159643, 5502.0, 310.0, 310.0);
        BaseJdPoint jd3 = new BaseJdPoint("JD3",  3544564.6783967,507777.2742962, 7039.0, 240.0, 240.0);
        BaseJdPoint jd4 = new BaseJdPoint("JD3",  3542790.8507542,502520.9354488, 5504.0, 290.0, 290.0);
        BaseJdPoint jd5 = new BaseJdPoint("JD3",  3542783.8855590,499171.4206639, 1000000.0, 0.0, 0.0);
        BaseJdPoint jd6 = new BaseJdPoint("JD3",  3542775.7620544,495456.3459668, 5999.0, 290.0, 290.0);
        BaseJdPoint jd7 = new BaseJdPoint("JD3",  3541079.6889559,491247.6191787, 8006.0, 230.0, 230.0);
        BaseJdPoint jd8 = new BaseJdPoint("JD3",  3540831.2425061,487696.4037934, 7001.0, 260.0, 260.0);
        BaseJdPoint jd9 = new BaseJdPoint("JD3",  3539171.6657311,483077.0876890, 8005.0, 250.0, 250.0);
        BaseJdPoint end = new BaseJdPoint("PE", 3538289.5267559, 478277.1930893);
        List<BaseJdPoint> jdPoints =new ArrayList<>();
        jdPoints.add(start1);
        jdPoints.add(jd1);
        jdPoints.add(jd2);
        jdPoints.add(jd3);
        jdPoints.add(jd4);
        jdPoints.add(jd5);
        jdPoints.add(jd6);
        jdPoints.add(jd7);
        jdPoints.add(jd8);
        jdPoints.add(jd9);
        jdPoints.add(end);
        Jd2RoadElement<BaseJdPoint> ts = new Jd2RoadElement<>(jdPoints);
        List<CommonJdPoint> commonJdPoints = ts.commonJdPoints;


        RoadElement element1 = new RoadElement(start1, jd1, jd2);

        for (int i = 9000; i < 10000; i = i + 20) {
            double dk = i;
            LinePoint dkComputXy = element1.dkComputXy(dk);
            double ang = Angle.RAD2DMS(dkComputXy.azimuth);
           // System.out.println(dk + "," + ang + "," + dkComputXy.x + "," + dkComputXy.y);
        }


        LinePoint dkComputXy = element1.dkComputXy(9000.0);
        LinePoint l =  element1.xyComputDK(new Point2D(3373600.2619,458500.6039));

        RoadElement element2 = new RoadElement(element1.hz, jd2, jd3);
        RoadElement element3 = new RoadElement(element2.hz, jd3, end);
    }

    private static void test1() {
     /*   RoadPoint start1 = new RoadPoint("PS", 3448566.179, 523762.1468);
        start1.pointType = PointType.起点;
        start1.dk = 0.0;


        RoadPoint jd1 = new RoadPoint("JD1", 3449350.124, 517884.0147, 10000.0, 470.0, 470.0);
        jd1.pointType = PointType.交点;
        RoadPoint jd2 = new RoadPoint("JD2", 3447915.651, 511348.328, 10000.0, 470.0, 470.0);
        jd2.pointType = PointType.交点;
        RoadPoint jd3 = new RoadPoint("JD3", 3446424.464, 508068.635, 9000.0, 530.0, 530.0);
        jd3.pointType = PointType.交点;

        RoadPoint end = new RoadPoint("PE", 3445264.711, 497143.8423);
        end.pointType = PointType.终点;

        RoadElement element1 = new RoadElement(start1, jd1, jd2);

        for (int i = 4000; i < 17890.1; i = i + 10) {
            double dk = i;




            LinePoint dkComputXy = element1.dkComputXy(dk);
            double ang = Angle.RAD2DMS(dkComputXy.azimuth);

            System.out.print("里程计算坐标" + dk + "," + ang + "," + dkComputXy.x + "," + dkComputXy.y+",");
            LinePoint tep = element1.xyComputDK(new Point2D(dkComputXy.x, dkComputXy.y));

            if (null == tep){
                continue;
            }

            System.out.println("坐标计算里程" + tep.continuDk + "," + Angle.RAD2DMS(tep.azimuth) + "," + tep.x + "," + tep.y + "," + tep.remark);
        }
        return;*/
      /*  RoadElement element2 = new RoadElement(element1.hz, jd2, jd3);

        for (int i = 7890; i < 13860; i = i + 10) {
            double dk = i;




            LinePoint dkComputXy = element2.dkComputXy(dk);
            double ang = Angle.RAD2DMS(dkComputXy.azimuth);

            System.out.print("里程计算坐标" + dk + "," + ang + "," + dkComputXy.x + "," + dkComputXy.y+",");

            LinePoint tep = element2.xyComputDK(new Point2D(dkComputXy.x, dkComputXy.y));

            if (null == tep){
                continue;
            }

            System.out.println("坐标计算里程" + tep.continuDk + "," + Angle.RAD2DMS(tep.azimuth) + "," + tep.x + "," + tep.y + "," + tep.remark);
        }


        RoadElement element3 = new RoadElement(element2.hz, jd3, end);
        for (int i = 13868; i < 17870; i = i + 10) {
            double dk = i;




            LinePoint dkComputXy = element3.dkComputXy(dk);
            double ang = Angle.RAD2DMS(dkComputXy.azimuth);

            System.out.print("里程计算坐标" + dk + "," + ang + "," + dkComputXy.x + "," + dkComputXy.y+",");
            LinePoint tep = element3.xyComputDK(new Point2D(dkComputXy.x, dkComputXy.y));

            if (null == tep){
                continue;
            }

            System.out.println("坐标计算里程" + tep.continuDk + "," + Angle.RAD2DMS(tep.azimuth) + "," + tep.x + "," + tep.y + "," + tep.remark);
        }


        System.out.println(element1.endP.dk + ",");
        System.out.println(element2.endP.dk + ",");
        System.out.println(element3.endP.dk + ",");*/

    }
}
