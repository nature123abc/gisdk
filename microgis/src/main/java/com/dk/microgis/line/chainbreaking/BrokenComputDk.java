package com.dk.microgis.line.chainbreaking;

import com.dk.microgis.error.CommonException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-04-28 16:57
 * @desc 根据现场里程计算贯通里程，根据贯通里程计算现场里程
 */
public class BrokenComputDk {
    List<BrokenChainInfo> brokenChainInfos;

    public BrokenComputDk(List<BrokenChainInfo> brokenChainInfos) {
        this.brokenChainInfos = brokenChainInfos;
        init();
    }

    private void init() {
        BrokenChainInfo.computDetlSum(brokenChainInfos);
    }

    /**
     * 贯通里程计算现场里程
     *
     * @param continuDk
     * @return
     */
    public double continu2Scence(double continuDk) {
        for (int i = 1; i < brokenChainInfos.size(); i++) {
            BrokenChainInfo first = brokenChainInfos.get(i - 1);
            BrokenChainInfo second = brokenChainInfos.get(i);
            if (continuDk <= second.getContinuDk() && continuDk >= first.getContinuDk()) {

                double detlDk = continuDk - first.continuDk;
                return first.getBrokenDk() + detlDk;
            }
        }
        if (null != brokenChainInfos && brokenChainInfos.size() > 0) {
            if (continuDk < brokenChainInfos.get(0).getContinuDk()) {
                return continuDk;
            }
            BrokenChainInfo last = brokenChainInfos.get(brokenChainInfos.size() - 1);
            if (continuDk > last.getContinuDk()) {
                double detlDk = continuDk - last.continuDk;
                return last.getBrokenDk() + detlDk;
            }
        }
        return continuDk;
    }

    /**
     * @param type     大于0表示是断链之后，
     * @param scenceDk
     * @return
     */
    public double scence2Continu(int type, double scenceDk) {
        List<BrokenChainInfo> chainInfos = new ArrayList<>();
        for (int i = 1; i < brokenChainInfos.size(); i++) {
            BrokenChainInfo first = brokenChainInfos.get(i - 1);
            BrokenChainInfo second = brokenChainInfos.get(i);
            if (scenceDk <= second.getSceneDk() && scenceDk >= first.getBrokenDk()) {
                chainInfos.add(first);
                chainInfos.add(second);
            }
        }
        if (chainInfos.size() < 1) {
            throw new CommonException("当前里程不在断链内，请查证后再试");
        }
        BrokenChainInfo first = chainInfos.get(0);//第一个点
        if (chainInfos.size() == 4) {
            if (type > 0) {
                first = chainInfos.get(3);
            }
        }
        double detl = scenceDk - first.getBrokenDk();
        return first.getContinuDk() + detl;
    }


}
