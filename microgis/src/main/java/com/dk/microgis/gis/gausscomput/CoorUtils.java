package com.dk.microgis.gis.gausscomput;


import com.dk.microgis.base.Point2D;
import com.dk.microgis.math.Angle;

/**
 * @作者: qq
 * @日期： 2018/12/10 23:07
 * @备注：
 */
public class CoorUtils {


    /**
     * 高斯反算
     *
     * @param coorType       坐标参考 {@link CoordinateSystem}
     * @param centraMeridian 中央子午线 {度,如果不是度，请在外面进行转换}
     * @param x              X坐标
     * @param y              Y坐标，注意Y坐标是添加500km之后的值
     * @return 经纬度（度）
     */
    public static double[] computCoor(CoordinateSystem coorType, double centraMeridian, double x, double y) {
        double dx = -1;
        double dy = -1;
        double centerAngle = 0;
        centerAngle = Angle.D2RAD(centraMeridian);
        dx = Double.valueOf(x);
        dy = Double.valueOf(y) - 500000;
        Point2D point2D = new Point2D(dx, dy);
        double[] bl = GaussProjection.XYToBL(coorType.value(), point2D);
        double detB;
        double detL;
        detB = Angle.RAD2D(centerAngle + bl[0]);//实际经度
        detL = Angle.RAD2D(bl[1]);
        return new double[]{detB, detL};
    }

    /**
     * 高斯正算
     *
     * @param coorType
     * @param centraMeridian 度
     * @param lng
     * @param lat
     * @return
     */
    public static Point2D bl2xy(CoordinateSystem coorType, double centraMeridian, double lng, double lat) {
        Point2D point2D = GaussProjection.bl2Xy(coorType.value(), lat, lng, centraMeridian);
        point2D.setY(point2D.y + 500000);
        return point2D;
    }
}
