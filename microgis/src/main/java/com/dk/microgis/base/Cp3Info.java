package com.dk.microgis.base;



/**
 * @author hq
 * @date 2021-04-28 9:20
 * @desc
 */

public class Cp3Info extends Point4D {
    public Integer split;//分带
    public String originName;//
    public Double toCenterDis;//
    public Double toCenterAng;//


    public Cp3Info(Cp3Info cp3Info) {
        super(cp3Info);
        this.split = cp3Info.split;
        this.originName = cp3Info.originName;
        this.toCenterDis = cp3Info.toCenterDis;
        this.toCenterAng = cp3Info.toCenterAng;

    }

    public Cp3Info() {
        super();
    }

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Double getToCenterDis() {
        return toCenterDis;
    }

    public void setToCenterDis(Double toCenterDis) {
        this.toCenterDis = toCenterDis;
    }

    public Double getToCenterAng() {
        return toCenterAng;
    }

    public void setToCenterAng(Double toCenterAng) {
        this.toCenterAng = toCenterAng;
    }

}
