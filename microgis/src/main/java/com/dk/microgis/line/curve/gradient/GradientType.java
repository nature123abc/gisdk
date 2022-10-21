package com.dk.microgis.line.curve.gradient;

public enum GradientType {
    凸形(1), 凹形(-1);

    int value;

    GradientType(int value) {
        this.value = value;

    }

    public static GradientType valueOf(int value) {    //
        switch (value) {

            case -1:
                return 凹形;
            case 1:
                return 凸形;

            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }
}
