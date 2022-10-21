package com.dk.test;



import com.dk.microgis.line.curve.cruve1.comm.Jd2RoadElement;
import com.dk.microgis.line.curve.cruve1.entity.BaseJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.CommonJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.LinePoint;
import com.dk.microgis.line.curve.cruve1.entity.RoadElement;
import com.dk.microgis.math.Angle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-12-08 15:16
 * @desc
 */
public class Test {
    public static void main(String[] args) {


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
    }

    private static void encryption1() {
        try {

            Test muDemo = new Test();
            String jarName = "microgis-1.0.jar";
            String inFile = muDemo.getPath() + "libs/" + jarName;
            String outFile = muDemo.getPath() + "libs/" + "encrypted.jar";


//            XCryptos.encryption()
//                    .from(inFile)
//                    .use("io.xjar")
//                    .exclude("/static/**/*")
//                    .exclude("/META-INF/resources/**/*")
//                    .to(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPath() {
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        System.out.println(xmlpath);

        String path = xmlpath.getPath();
        return path.split("classes")[0];
    }
}
