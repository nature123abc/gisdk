package com.dk.microgis.line.curve;


import com.dk.microgis.base.Point2D;
import com.dk.microgis.math.LinearParmsComput;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2020-11-02 14:52
 * @desc
 */
public class Test {
    public static void main(String[] args) {
       // test1();
        // test2();
        List<Point2D> points = new ArrayList<Point2D>() {
            {
                add(new Point2D(1.0, 5.0));
                add(new Point2D(1.0, 6.0));

            }
        };

        LinearParmsComput.xyComputKb(points);

    }

    private static void test2() {
       /* new VerticalCurveElement(46400, 423.36, 20000),
                new VerticalCurveElement(49920, 426.88, 20000),
                new VerticalCurveElement(53200, 446.232, 20000),*/

      /*  List<GradientInfo> gr = new ArrayList<>();
        gr.add(new GradientInfo(46400, 423.36, 20000));
        gr.add(new GradientInfo(49920, 426.88, 20000));
        gr.add(new GradientInfo(53200, 446.232, 20000));
        GradientEleComput.computeGradientInfo(gr);*/
    }

    private static void test1() {
    /*    List<DesignPointInfo> designPointInfos = new ArrayList<>();
        designPointInfos.add(new DesignPointInfo(11600.000, 3461749.6535, 484147.4952, PointType.起点, 10000000, -1, -1, 0.0));
        designPointInfos.add( new DesignPointInfo(-1, 3461943.5810, 483425.4060, PointType.交点, 9995.3, 140, 140, 50));
        designPointInfos.add(  new DesignPointInfo(-1, 3462206.3870, 482164.8714, PointType.交点, 7004.7, 190, 190, 70));
        designPointInfos.add( new DesignPointInfo(-1, 3462935.5100, 479384.0290, PointType.交点, 11995.3, 120, 120, 40));
        designPointInfos.add( new DesignPointInfo(-1, 3463777.7210, 475773.3835, PointType.交点, 12000.7, 200, 200, 60));
        designPointInfos.add( new  DesignPointInfo(-1, 3464422.3060, 473572.4702, PointType.交点, 7995.3, 300, 300, 100));
        designPointInfos.add(new DesignPointInfo(-1, 3464832.2651, 471625.4632, PointType.终点, -1, -1, -1, 0.0));


        CurveElemComput.disignComputeToCurveMD(designPointInfos);*/
    }
}
