package com.dk.microgis.line.curve.curve.common.entity;

public enum PointType {
    起点(0, "QD"), 交点(1, "JD"), 终点(2, "ZD");

    int value;
    String name;

    PointType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static PointType valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {

            case 0:
                return 起点;
            case 1:
                return 交点;
            case 2:
                return 终点;
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
