package com.tutu.daogou.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Slf4j
public class MathUtils {

    public static Double decimalTwo(double value){
        BigDecimal bc = new BigDecimal(Double.toString(value));
        value = bc.setScale(2, BigDecimal.ROUND_UP).doubleValue();
        return  value;
    }

    public static String decimalTwoToString(double value){
        BigDecimal bc = new BigDecimal(Double.toString(value));
        String strValue = bc.setScale(2, BigDecimal.ROUND_UP).toString();
        return  strValue;
    }

    public static Double doubleAdd(double value,double value1){
        BigDecimal b1=new BigDecimal(Double.toString(value));
        BigDecimal b2 = new BigDecimal(Double.toString(value1));
        Double e =b1.add(b2).doubleValue();
        return e;
    }

    /**
     * 两个数相减.
     * @param value
     * @param value1
     * @return
     */
    public static Double doubleSubtract(double value,double value1){
        BigDecimal b1=new BigDecimal(Double.toString(value));
        BigDecimal b2 = new BigDecimal(Double.toString(value1));
        Double e =b1.subtract(b2).doubleValue();
        return e;
    }

    /**
     * double 乘法
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }

    /**
     * double 除法
     * @param d1
     * @param d2
     * @return
     */
    public static double div(double d1,double d2){
        if (d2 == 0) {
            return 0.0;
        }
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        int scale = 5;
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * double 除法，结果保留scale位小数，其余舍弃，不进位
     * @param d1
     * @param d2
     * @param scale 保留小数点位数
     * @return
     */
    public static double divRoundDown(double d1,double d2,int scale){
        if (d2 == 0) {
            return 0.0;
        }
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide(bd2,scale,BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * double 除法，结果保留两位小数，结果不小于原数字
     * @param d1
     * @param d2
     * @return
     */
    public static double divCeiling(double d1,double d2){
        if (d2 == 0) {
            return 0.0;
        }
        double d3 = div(d1, d2);
        d3 = formatCeiling(d3);
        return d3;
    }

    /**
     * 数字保留，两位小数，结果不小于原数字.
     */
    public static Double formatCeiling(Double amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.CEILING);
        double formatAmount = Double.parseDouble(df.format(amount));
        return formatAmount;
    }
}
