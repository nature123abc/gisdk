package com.dk.microgis.line.curve.cruve1;

import com.dk.microgis.base.Cp3Info;
import com.dk.microgis.error.CommonException;
import com.dk.microgis.line.chainbreaking.BrokenChainInfo;
import com.dk.microgis.line.chainbreaking.BrokenComputDk;
import com.dk.microgis.line.curve.cruve1.comm.RoadEle2XyDk;
import com.dk.microgis.line.curve.cruve1.entity.CommonJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.LinePoint;
import com.dk.microgis.line.curve.gradient.BaseGradient;
import com.dk.microgis.line.curve.gradient.CommonGradient;
import com.dk.microgis.line.curve.gradient.GradientComput;


import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-11-09 13:08
 * @desc
 */
public class ComputCurveGradientBrokenCp3 {
    List<CommonJdPoint> curveDesignInfos;//曲线交点信息
    List<BaseGradient> baseGradients;//竖曲线信息


    public List<BrokenChainInfo> breakChainInfos;//断链
    public List<Cp3Info> cp3Infos;//线路cp3

    public List<CommonGradient> gradientResult;
    public List<CommonJdPoint> multSpit2OneResultJD;//多个分带交点合并

    public ComputCurveGradientBrokenCp3(List<CommonJdPoint> curveDesignInfos, List<BaseGradient> baseGradients, List<BrokenChainInfo> breakChainInfos, List<Cp3Info> cp3Infos) {
        this.curveDesignInfos = null == curveDesignInfos ? new ArrayList<>() : curveDesignInfos;
        this.baseGradients = null == baseGradients ? new ArrayList<>() : baseGradients;
        this.breakChainInfos = null == breakChainInfos ? new ArrayList<>() : breakChainInfos;
        this.cp3Infos = null == cp3Infos ? new ArrayList<>() : cp3Infos;
        comput();
    }

    private void comput() {
        //1、五大桩
        MultJd2RoadElement jd2RoadElement = new MultJd2RoadElement(curveDesignInfos);
        List<List<CommonJdPoint>> multSpitOrgJD = jd2RoadElement.multSpitOrgJD;//多个分带的交点信息
        multSpit2OneResultJD = jd2RoadElement.multSpit2OneResultJD;
        //2、断链
        BrokenChainInfo.computDetlSum(breakChainInfos);
        //3、竖曲线
        GradientComput comput = new GradientComput(baseGradients, breakChainInfos);
        gradientResult = comput.getCommonGradients();//竖曲线计算结果

        //4、CP3
        computCp3(multSpitOrgJD, breakChainInfos);

    }

    private void computCp3(List<List<CommonJdPoint>> multSpitOrgJD, List<BrokenChainInfo> breakChainInfos) {
        for (int i = 0; i < cp3Infos.size(); i++) {
            Cp3Info cp = cp3Infos.get(i);
            if (null == cp.split) {
                throw new CommonException("Cp3点分带不能为空");
            }
            List<CommonJdPoint> currentSpit = findSpitJd(cp, multSpitOrgJD);
            computOneCp3(cp, currentSpit, breakChainInfos);
        }

    }

    private void computOneCp3(Cp3Info cp, List<CommonJdPoint> currentSpit, List<BrokenChainInfo> breakChainInfos) {
        RoadEle2XyDk roadEle = new RoadEle2XyDk(CommonJdPoint.convert2RoadElement(currentSpit));
        LinePoint lp = roadEle.xy2Dk(cp);
        cp.toCenterAng = lp.azimuth;
        cp.toCenterDis = lp.offDis;
        cp.continuDk = lp.continuDk;
        cp.remark = lp.remark;

        BrokenComputDk computDk = new BrokenComputDk(breakChainInfos);
        double seDk = computDk.continu2Scence(cp.continuDk);
        cp.sceneDk = seDk;
    }

    private List<CommonJdPoint> findSpitJd(Cp3Info cp, List<List<CommonJdPoint>> multSpitOrgJD) {
        for (List<CommonJdPoint> oneSpit : multSpitOrgJD) {
            if (oneSpit.get(0).split.equals(cp.split)) {
                return oneSpit;
            }
        }
        return null;
    }
}
