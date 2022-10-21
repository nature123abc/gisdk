package com.dk.microgis.line.curve.cruve1;


import com.dk.microgis.line.curve.cruve1.comm.Jd2RoadElement;
import com.dk.microgis.line.curve.cruve1.entity.CommonJdPoint;
import com.dk.microgis.math.Angle;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hq
 * @date 2021-10-29 16:55
 * @desc 多个分带的交点计算到曲线元
 */
public class MultJd2RoadElement {

    List<CommonJdPoint> commonJdPoints;//多个分带数据
    public List<List<CommonJdPoint>> multSpitOrgJD;//多个分带交点数据，
    public List<CommonJdPoint> multSpit2OneResultJD;//多个分带交点合并


    public MultJd2RoadElement(List<CommonJdPoint> commonJdPoints) {
        this.commonJdPoints = commonJdPoints;
        multSpitOrgJD = new ArrayList<>();
        multSpit2OneResultJD = new ArrayList<>();
        init1();

        for (int i = 0; i < multSpitOrgJD.size(); i++) {
            List<CommonJdPoint> js = multSpitOrgJD.get(i);

            //计算一个分段的交点数据
            Jd2RoadElement jd2RoadElement = new Jd2RoadElement(js);

            List<CommonJdPoint> oneSpitJD = jd2RoadElement.commonJdPoints;

            multSpitOrgJD.set(i, oneSpitJD);//保证计算后数据最新
            setSpit(oneSpitJD, js.get(0).split);
            multSpit2OneResultJD.addAll(oneSpitJD);//合并所有分带数据
        }

    }

    private void setSpit(List<CommonJdPoint> oneSpitJD, Integer split) {
        for (CommonJdPoint j : oneSpitJD) {
            if (null != j.roadElement) {
                if (null != j.roadElement.a) {
                    j.roadElement.a = Angle.RAD2DMS(j.roadElement.a);

                }
            }
            j.split = split;
        }
    }

    private void init1() {

        List<CommonJdPoint> oneSpit = new ArrayList<>();
        Set<Integer> spit = new HashSet<>();

        for (int i = 0; i < commonJdPoints.size(); i++) {
            CommonJdPoint jd = commonJdPoints.get(i);
            if (oneSpit.size() == 0) {
                oneSpit.add(jd);
                spit.add(jd.split);
                continue;
            }
            if (!spit.contains(jd.split)) {//新的一个
                multSpitOrgJD.add(oneSpit);
                oneSpit = new ArrayList<>();
            }
            spit.add(jd.split);
            oneSpit.add(jd);

            if (i == commonJdPoints.size() - 1) {
                multSpitOrgJD.add(oneSpit);
            }
        }
    }
}
