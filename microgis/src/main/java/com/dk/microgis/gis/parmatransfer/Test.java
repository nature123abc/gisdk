package com.dk.microgis.gis.parmatransfer;

import com.dk.microgis.base.Point2D;
import com.dk.microgis.base.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * A001  2886585.7645  531481.3787  29459.2790  31382.6500
 * A002  2891839.8474  479813.8408  34711.8360 -20285.9390
 * A003  2861398.5526  509387.9347   4271.0320   9289.5760
 * A004  2819325.6136  524901.0211 -37801.9780  24804.2570
 * B001  2860046.5165  524597.2659   2919.4310  24499.2250
 */
public class Test {
    public static void main(String[] args) {

       // test4Parma();
        //test7Parma();

        double detl = Double.NEGATIVE_INFINITY * 1000;
        System.out.println(detl);

    }

    private static void test7Parma() {
        List<Point3D> point3DS = new ArrayList<>();
        Point3D point3D11 = new Point3D("14", -2850017.4720, 4690744.5225, 3237959.9725);
        Point3D point3D12 = new Point3D("15", -2838514.0744, 4704426.8235, 3228266.3341);
        Point3D point3D13 = new Point3D("17", -2865534.8833, 4672522.4133, 3250504.9271);
        Point3D point3D14 = new Point3D("27", -2831988.2672, 4639803.8041, 3325394.3271);
        Point3D point3D15 = new Point3D("28", -2828112.6536, 4649775.2984, 3314823.5928);
        Point3D point3D16 = new Point3D("30", -2855329.6573, 4630358.1765, 3318638.7826);
        Point3D point3D17 = new Point3D("35", -2862670.7160, 4637795.9658, 3302008.5427);
        Point3D point3D18 = new Point3D("36", -2878560.5480, 4633657.4681, 3294040.3134);
        Point3D point3D19 = new Point3D("38", -2848021.5252, 4645925.4533, 3303239.1758);

        point3DS.add(point3D11);
        point3DS.add(point3D12);
        point3DS.add(point3D13);
        point3DS.add(point3D14);
        point3DS.add(point3D15);
        point3DS.add(point3D16);
        point3DS.add(point3D17);
        point3DS.add(point3D18);
        point3DS.add(point3D19);

        List<Point3D> point3DS1 = new ArrayList<>();
        Point3D point3D111 = new Point3D("14", -2850023.9497, 4680915.4702, 3237036.8089);
        Point3D point3D121 = new Point3D("15", -2838520.5570, 4694597.9568, 3227343.6466);
        Point3D point3D131 = new Point3D("17", -2865541.3037, 4662693.0793, 3249581.2094);
        Point3D point3D141 = new Point3D("27", -2831996.3411, 4629974.7937, 3324471.1958);
        Point3D point3D151 = new Point3D("28", -2828120.6115, 4639946.4678, 3313900.6500);
        Point3D point3D161 = new Point3D("30", -2855337.4429, 4620529.0691, 3317715.1436);
        Point3D point3D171 = new Point3D("35", -2862678.2257, 4627966.7705, 3301084.7754);
        Point3D point3D181 = new Point3D("36", -2878567.6126, 4623828.0607, 3293116.2182);
        Point3D point3D191 = new Point3D("38", -2848029.1224, 4636096.4085, 3302315.8090);
        point3DS1.add(point3D111);
        point3DS1.add(point3D121);
        point3DS1.add(point3D131);
        point3DS1.add(point3D141);
        point3DS1.add(point3D151);
        point3DS1.add(point3D161);
        point3DS1.add(point3D171);
        point3DS1.add(point3D181);
        point3DS1.add(point3D191);

        SevenParamTransfer transfer = new SevenParamTransfer<>(point3DS,point3DS1);


    }

    private static void test4Parma() {
        Point2D point2D1 = new Point2D("A001", 2886585.7645, 531481.3787);
        Point2D point2D2 = new Point2D("A002", 2891839.8474, 479813.8408);
        Point2D point2D3 = new Point2D("A003", 2861398.5526, 509387.9347);
        Point2D point2D4 = new Point2D("A004", 2819325.6136, 524901.0211);
        Point2D point2D5 = new Point2D("B001", 2860046.5165, 524597.2659);

        List<Point2D> old = new ArrayList<>();
        old.add(point2D1);
        old.add(point2D2);
        old.add(point2D3);
        old.add(point2D4);
        old.add(point2D5);


        Point2D point2D11 = new Point2D("A001", 29459.2790, 31382.6500);
        Point2D point2D21 = new Point2D("A002", 34711.8360, -20285.9390);
        Point2D point2D31 = new Point2D("A003", 4271.0320, 9289.5760);
        Point2D point2D41 = new Point2D("A004", -37801.9780, 24804.2570);
        Point2D point2D51 = new Point2D("B001", 2919.4310, 24499.2250);
        List<Point2D> new1 = new ArrayList<>();
        new1.add(point2D11);
        new1.add(point2D21);
        new1.add(point2D31);
        new1.add(point2D41);
        new1.add(point2D51);

        FourParamTransfer transfer = new FourParamTransfer(old, new1);
        transfer.computTransferParam();
    }
}
