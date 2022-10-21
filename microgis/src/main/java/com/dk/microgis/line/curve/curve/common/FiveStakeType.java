package com.dk.microgis.line.curve.curve.common;

/**
 * @author hq
 * @date 2021-04-16 22:22
 * @desc
 */
public enum FiveStakeType {
    QD(0),//起点
    ZH(1),//直缓
    HY(2), //缓圆
    QZ(3), //曲中
    YH(4),//圆缓
    HZ(5),//缓直
    ZY(6),//直圆
    YZ(7);//圆直


    int value;

    FiveStakeType(int value) {
        this.value = value;
    }

}
