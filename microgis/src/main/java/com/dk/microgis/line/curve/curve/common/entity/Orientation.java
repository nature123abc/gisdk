package com.dk.microgis.line.curve.curve.common.entity;

/**
 * 曲线偏向(左偏为=1：右偏为=-1)
 */
public enum Orientation {
    左(-1), 右(1);

    int value;


    Orientation(int value) {
        this.value = value;

    }

    public static Orientation valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {

            case 1:
                return 右;
            case -1:
                return 左;
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }
}
