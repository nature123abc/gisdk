package com.dk.microgis.line.chainbreaking;

import java.util.List;

/**
 * @author hq
 * @date 2021-04-12 13:12
 * @desc 断链信息
 */

public class BrokenChainInfo {
    Double sceneDk;//实际现场里程
    Double continuDk;//贯通里程
    Double brokenDk;//设置断链后里程 新的现场里程

    Double detlDk;
    Double sumDk;

    public BrokenChainInfo() {
    }

    public BrokenChainInfo(Double continuDk, Double sceneDk, Double brokenDk) {
        this.continuDk = continuDk;
        this.sceneDk = sceneDk;
        this.brokenDk = brokenDk;
    }


    /**
     * 计算断链差值和累计
     *
     * @param allChain
     */
    public static void computDetlSum(List<BrokenChainInfo> allChain) {
        for (BrokenChainInfo i : allChain) {
            double detl = i.getBrokenDk() - i.getSceneDk();
            double sum = i.getContinuDk() - i.getSceneDk();
            i.setDetlDk(detl);
            i.setSumDk(sum);
        }
    }

    public Double getSceneDk() {
        return sceneDk;
    }

    public void setSceneDk(Double sceneDk) {
        this.sceneDk = sceneDk;
    }

    public Double getContinuDk() {
        return continuDk;
    }

    public void setContinuDk(Double continuDk) {
        this.continuDk = continuDk;
    }

    public Double getBrokenDk() {
        return brokenDk;
    }

    public void setBrokenDk(Double brokenDk) {
        this.brokenDk = brokenDk;
    }

    public Double getDetlDk() {
        return detlDk;
    }

    public void setDetlDk(Double detlDk) {
        this.detlDk = detlDk;
    }

    public Double getSumDk() {
        return sumDk;
    }

    public void setSumDk(Double sumDk) {
        this.sumDk = sumDk;
    }
}
