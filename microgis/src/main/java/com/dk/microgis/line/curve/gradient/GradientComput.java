package com.dk.microgis.line.curve.gradient;


import com.dk.microgis.line.chainbreaking.BrokenChainInfo;
import com.dk.microgis.line.chainbreaking.BrokenComputDk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-04-28 18:17
 * @desc S竖曲线计算到曲线元
 */
public class GradientComput {
    List<BaseGradient> grads;
    List<CommonGradient> commonGradients;//
    List<BrokenChainInfo> brokenChainInfos;//断链表

    public GradientComput(List<BaseGradient> grads, List<BrokenChainInfo> brokenChainInfos) {
        this.grads = grads;
        this.brokenChainInfos = brokenChainInfos;
        commonGradients = new ArrayList<>();
        init();
    }

    public GradientComput(List<BaseGradient> grads) {
        this(grads, null);
    }

    private void init() {
        //读取坡度竖曲线数据
        int size = grads.size();
        if (size < 2) {
            return;
        }

        commonGradients.add(new CommonGradient(grads.get(0)));
        for (int i = 1; i < size; i++) {
            BaseGradient first = commonGradients.get(i - 1);
            BaseGradient second = grads.get(i);

            CommonGradient com = new CommonGradient(second);
            //根据坡长计算贯通里程
            if (null == com.continuDk) {
                if (null != first.continuDk && null != second.slopeLen) {
                    com.continuDk = first.continuDk + second.slopeLen;
                }
            }

            if (null == com.slopeLen) {
                if (null != first.continuDk && null != second.continuDk) { //根据里程计算坡长
                    com.slopeLen = second.continuDk - first.continuDk;
                }
            }

            double k = (second.pointH - first.pointH) / com.slopeLen;//根据坡长和高程变化值计算变坡率

            com.slopeAng = k;//坡度值
            commonGradients.add(com);
        }
        for (int i = 1; i < size - 1; i++) {

            CommonGradient first = commonGradients.get(i);
            CommonGradient second = commonGradients.get(i + 1);


            // double afa = Math.atan(second.slopeAng) - Math.atan(first.slopeAng);//高精度
            double afa = second.slopeAng - first.slopeAng;//低精度
            first.afa = afa;
            first.gradientLen = first.radius * Math.abs(afa);    //曲线长
            first.tangentLen = first.radius * Math.tan(afa / 2.0);//切线长度
            first.startDk = first.continuDk - first.gradientLen / 2.0;//竖曲线起点里程
            first.endDk = first.continuDk + first.gradientLen / 2.0;//竖曲线终点里程
            first.extDis = first.tangentLen * first.tangentLen / 2.0 / first.radius;//竖曲线的外矢距

            if (afa > 0.0) {
                first.graCurvePX = GradientType.凹形;
            } else if (afa < 0.0) {
                first.graCurvePX = GradientType.凸形;
            }
        }
        //计算里程
        if (null != brokenChainInfos) {
            BrokenComputDk computDk = new BrokenComputDk(brokenChainInfos);
            for (int i = 0; i < commonGradients.size(); i++) {
                CommonGradient tmep = commonGradients.get(i);
                double secnceDk = computDk.continu2Scence(tmep.continuDk);
                setPointDk(tmep, secnceDk);
            }
        } else {
            for (int i = 0; i < commonGradients.size(); i++) {
                CommonGradient tmep = commonGradients.get(i);
                double secnceDk = tmep.continuDk;
                setPointDk(tmep, secnceDk);
            }
        }
    }

    private void setPointDk(CommonGradient tmep, double secnceDk) {
        tmep.setScenceDk(secnceDk);
        if (null != tmep.slopeAng) {
            tmep.slopeAng = tmep.slopeAng * 1000;
        }
    }

    public List<CommonGradient> getCommonGradients() {
        return commonGradients;
    }
}
