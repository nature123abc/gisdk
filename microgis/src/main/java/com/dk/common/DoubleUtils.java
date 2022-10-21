package com.dk.common;

import com.dk.microgis.error.CommonException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by hq on 2018/1/23.
 */

public class DoubleUtils {


    public static final double MAX_VALUE = 99999999.9999;
    public static final double MIN_VALUE = 0.000000000001;
    public static final double MIN_DOUBLE_VALUE = 1.0 / Double.MAX_VALUE;
    public static final double MAX_DOUBLE_VALUE = Double.MAX_VALUE;
    public static final double MIN_DIS = 0.0001;

    public static void main(String[] args) {
        System.out.println(isZero(0.0));
    }

    public static boolean isZero(double v) {
        BigDecimal data1 = new BigDecimal(v);
        BigDecimal data2 = new BigDecimal(0.0);
        int result = data1.compareTo(data2);
        if (result == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断米级别类型的double是否相同
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean deMquals(double a, double b) {
        return Math.abs(a - b) < MIN_DIS;
    }

    /**
     * 获取小数点后几位
     *
     * @param InputVal double数据
     * @param n        需要保留的小数点位数
     * @return 一定位数的double数据
     */
    public static double getDemi(double InputVal, int n) {
        try {
            return formatDouble(InputVal, n);
        } catch (NumberFormatException e) {
            return InputVal;
        }
    }

    public static double formatDouble(double d, int count) {
        BigDecimal bg = new BigDecimal(d).setScale(count, RoundingMode.UP);
        return bg.doubleValue();
    }

    /**
     * 获取小数点后几位
     *
     * @param InputVal double数据
     * @param n        需要保留的小数点位数
     * @return 一定位数的double数据
     */
    public static String getStringDemi(double InputVal, int n) {
        return getStrDemi(InputVal, n);
    }

    /**
     * 获取小数点后几位
     *
     * @param InputVal String类型的double数据
     * @param n        需要保留的小数点位数
     * @return 一定位数的double数据
     */
    public static String getStrDemi(double InputVal, int n) {
        String OutString = "";
        String FormatString = "%.0" + String.valueOf(n) + "f";     //四舍五入
        OutString = String.format(FormatString, InputVal);
        return String.valueOf(Double.valueOf(OutString));
    }

    /**
     * 截取double类型数据前 count位数据
     *
     * @param count 小数点后几位
     * @param in    需要截取数据
     * @return
     */
    public static String getDoubleFront(double in, int count) {
        return String.format("%." + count + "f", in);
    }

    /**
     * 获取小数点后几位四舍五入
     *
     * @param InputVal String类型的double数据
     * @param n        需要保留的小数点位数
     * @return 一定位数的double数据
     */
    public static double getDoubleDigit(double InputVal, int n) {
        BigDecimal b = new BigDecimal(InputVal);
        return b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
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


    /**
     * 获取小数点后几位
     *
     * @param InputVal String类型的double数据
     * @param n        需要保留的小数点位数
     * @return 一定位数的double数据
     */
    public static String getStringDigit(double InputVal, int n) {
        return getDoubleDigit(InputVal, n) + "";
    }


    /**
     * 判断当前数值是否可用
     *
     * @param key
     * @return
     */
    public static boolean isIegalKey(Double key) {
        if (null == key || Double.isNaN(key) || Double.isInfinite(key)) {
            return false;
        }
        return true;
    }

    public static double avg(List<Double> dbs) {
        if (null == dbs || dbs.size() == 0) {
            throw new CommonException("平均要素为空");
        }
        double sum = 0.0;
        for (Double d : dbs) {
            sum += d;
        }
        return sum / dbs.size();
    }
}
