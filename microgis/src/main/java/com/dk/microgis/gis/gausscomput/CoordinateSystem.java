package com.dk.microgis.gis.gausscomput;

/**
 * @作者: qq
 * @日期： 2018/12/8 14:12
 * @备注： 1 代表WGS-84椭球；2代表北京54坐标 椭球；3代表西安80坐标椭球；4 代表国家2000坐标椭球；
 */
public enum CoordinateSystem {
    WGS84(1), 北京54(2), 西安80(3), 国家2000(4);    //    调用构造函数来构造枚举项

    private int value = -1;

    private CoordinateSystem(int value) {    //    必须是private的，否则编译错误
        this.value = value;
    }

    public static CoordinateSystem valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 1:
                return WGS84;
            case 2:
                return 北京54;
            case 3:
                return 西安80;
            case 4:
                return 国家2000;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }

    static public int Parse(String morCode) {
        if (morCode.toUpperCase().contains("WGS") || morCode.toUpperCase().contains("84")) {
            return CoordinateSystem.WGS84.value();
        } else if (morCode.contains("北京") || morCode.contains("54")) {
            return CoordinateSystem.北京54.value();
        } else if (morCode.contains("西安") || morCode.contains("80")) {
            return CoordinateSystem.西安80.value();
        } else if (morCode.contains("2000") || morCode.contains("国家")) {
            return CoordinateSystem.国家2000.value();
        }
        return 0;
    }

    public static String Parse(int errorCode) {
        switch (errorCode) {
            case 1:
                return CoordinateSystem.WGS84.toString();
            case 2:
                return CoordinateSystem.北京54.toString();
            case 3:
                return CoordinateSystem.西安80.toString();
            case 4:
                return CoordinateSystem.国家2000.toString();
            default:
                return "";
        }
    }

}
