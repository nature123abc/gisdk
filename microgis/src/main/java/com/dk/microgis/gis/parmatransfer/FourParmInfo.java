package com.dk.microgis.gis.parmatransfer;



/**
 * @author hq
 * @date 2021-10-26 10:10
 * @desc
 */

public class FourParmInfo {

    public String name;

    public Double oldX;

    public Double oldY;

    public Double newX;

    public Double newY;

    public Double detlX;

    public Double detlY;

    public Double angle;//弧度

    public Double scale;

    public String remark;//


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOldX() {
        return oldX;
    }

    public void setOldX(Double oldX) {
        this.oldX = oldX;
    }

    public Double getOldY() {
        return oldY;
    }

    public void setOldY(Double oldY) {
        this.oldY = oldY;
    }

    public Double getNewX() {
        return newX;
    }

    public void setNewX(Double newX) {
        this.newX = newX;
    }

    public Double getNewY() {
        return newY;
    }

    public void setNewY(Double newY) {
        this.newY = newY;
    }

    public Double getDetlX() {
        return detlX;
    }

    public void setDetlX(Double detlX) {
        this.detlX = detlX;
    }

    public Double getDetlY() {
        return detlY;
    }

    public void setDetlY(Double detlY) {
        this.detlY = detlY;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
