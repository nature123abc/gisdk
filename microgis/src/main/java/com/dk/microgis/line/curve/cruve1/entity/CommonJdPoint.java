package com.dk.microgis.line.curve.cruve1.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-10-29 17:00
 * @desc 完整曲线要素信息都在
 */
public class CommonJdPoint extends BaseJdPoint {
    public RoadElement roadElement;//完整曲线要素信息都在
    public Integer split;//带号


    public CommonJdPoint() {
    }

    public CommonJdPoint(BaseJdPoint baseJdPoint, RoadElement roadElement) {
        super(baseJdPoint);
        this.roadElement = roadElement;
    }

    public RoadElement getRoadElement() {
        return roadElement;
    }

    public void setRoadElement(RoadElement roadElement) {
        this.roadElement = roadElement;
    }

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    static public List<RoadElement> convert2RoadElement(List<CommonJdPoint> coms) {
        List<RoadElement> ret = new ArrayList<>();
        for (CommonJdPoint j : coms) {
            ret.add(convert2RoadElement(j));
        }
        return ret;
    }

    static private RoadElement convert2RoadElement(CommonJdPoint j) {
        return j.roadElement;
    }
}
