package com.dk.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ProjectName: IOTS
 * @Desc:
 * @Author: hq
 * @Date: 2022/9/9
 */
public class FormatData {
    public static String formatDk(Double dk, int digit) {
        if (null == dk) return "--";
        int intDk = dk.intValue();
        double doubleDk = dk % 1.0;
        int qian = (intDk / 1000);
        double xiaoshu = (intDk % 1000) + doubleDk;
        return "K" + qian + "+" + formatStringDigit(xiaoshu, digit);
    }

    public static String formatSign(Double data, int digit) {
        if (null == data) return "--";
        String sign = data > 0.0 ? "+" : "";
        return sign + formatStringDigit(data, digit);
    }

    public static String formatStr(String data) {
        if (null == data) return "--";
        return data;
    }

    public static String formatStrInt(Integer data) {
        if (null == data) return "--";
        return "" + data;
    }

    public static String formatNullStr(String data) {
        if (null == data) return "";
        return data;
    }

    public static String obtDouble(Double value, int count) {
        if (null == value) {
            return "--";
        }
        return formatStringDigit(value, count);
    }

    public static String obtNullDouble(Double value, int count) {
        if (null == value) {
            return "";
        }
        return formatStringDigit(value, count);
    }

    public static String formatStringDigit(Double value, int count) {
        try {
            // double v = DoubleUtils.getDoubleDigit(value, count);
            return "" + formatDoubleDigit(value, count);
        } catch (Exception e) {
            e.printStackTrace();
            return "" + value;
        }
    }

    /**
     * 四舍五入格式化数据
     *
     * @param InputVal
     * @param n
     * @return
     */
    public static BigDecimal formatDoubleDigit(Double InputVal, int n) {
        BigDecimal b = new BigDecimal(InputVal);
        b.setScale(n, RoundingMode.HALF_UP);
        return b;
    }
}
