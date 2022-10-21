package com.dk.microgis.line.curve.curve.common;


import com.dk.microgis.base.Point3D;
import com.dk.microgis.base.Point4D;

/**
 * @author hq
 * @date 2021-04-16 17:45
 * @desc 切线点
 */

public class TangentPoint extends Point4D {
    public TangentPoint(Point3D point3D) {
        super(point3D);
    }

    public TangentPoint() {
    }

    public String cp3OriginalName;//原来编号
    public Double tangentAzimuth;//沿线路方向切线方位角
    public Double detlDis;//点间距,该点到线路中心距离，有正负之分 ，左- ，右﹢
    public Double p2CenterAzimuth;//当前点至线路中心方位角

    public String getCp3OriginalName() {
        return cp3OriginalName;
    }

    public void setCp3OriginalName(String cp3OriginalName) {
        this.cp3OriginalName = cp3OriginalName;
    }

    public Double getTangentAzimuth() {
        return tangentAzimuth;
    }

    public void setTangentAzimuth(Double tangentAzimuth) {
        this.tangentAzimuth = tangentAzimuth;
    }

    public Double getDetlDis() {
        return detlDis;
    }

    public void setDetlDis(Double detlDis) {
        this.detlDis = detlDis;
    }

    public Double getP2CenterAzimuth() {
        return p2CenterAzimuth;
    }

    public void setP2CenterAzimuth(Double p2CenterAzimuth) {
        this.p2CenterAzimuth = p2CenterAzimuth;
    }
}
