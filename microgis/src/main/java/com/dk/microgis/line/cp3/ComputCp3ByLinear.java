package com.dk.microgis.line.cp3;



import com.dk.microgis.line.chainbreaking.BrokenChainInfo;
import com.dk.microgis.line.chainbreaking.BrokenComputDk;
import com.dk.microgis.line.curve.curve.CurveXy2Dk;
import com.dk.microgis.line.curve.curve.common.CurveMetaInfo;
import com.dk.microgis.line.curve.curve.common.TangentPoint;
import com.dk.microgis.base.Cp3Info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-04-28 9:17
 * @desc 通过线形计算CP3里程、方位角等数据
 */
public class ComputCp3ByLinear {
    List<Cp3Info> originCp3s;
    List<Cp3Info> computCp3s;//计算里程后的CP3

    List<CurveMetaInfo> multFiveStake;//五大桩
    List<BrokenChainInfo> brokenChainInfos;//断链表

    boolean computScence;

    public ComputCp3ByLinear(List<Cp3Info> originCp3s, List<CurveMetaInfo> multFiveStake, List<BrokenChainInfo> brokenChainInfos) {
        this.originCp3s = originCp3s;
        this.multFiveStake = multFiveStake;
        this.brokenChainInfos = brokenChainInfos;
        computCp3s = new ArrayList<>();
        computScence = null != brokenChainInfos && brokenChainInfos.size() > 0 ? true : false;
        init();
    }

    private void init() {
        CurveXy2Dk curveXy2Dk = new CurveXy2Dk(multFiveStake);

        BrokenComputDk computDk = null;
        if (computScence) {
            computDk = new BrokenComputDk(brokenChainInfos);
        }

        for (Cp3Info info : originCp3s) {

            Cp3Info cp3 = new Cp3Info(info);


            TangentPoint point = null;
            try {
                point = curveXy2Dk.computeDkByXY(info,30000.0);

                cp3.continuDk = point.continuDk;
                cp3.setToCenterDis(point.getDetlDis());
                cp3.setToCenterAng(point.getTangentAzimuth());
                cp3.setRemark(point.getRemark());

                if (computScence) {
                    double seDk = computDk.continu2Scence(point.continuDk);
                    cp3.sceneDk = seDk;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            computCp3s.add(cp3);
        }

    }

    public List<Cp3Info> getComputCp3s() {
        return computCp3s;
    }
}
