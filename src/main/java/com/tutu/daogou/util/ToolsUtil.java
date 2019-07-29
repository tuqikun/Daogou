package com.tutu.daogou.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用工具类.
 * Created by Administrator on 2018/12/3.
 */
public class ToolsUtil {
    /**
     * 数字保留，两位小数，结果不小于原数字.
     */
    public static Double formatCeilling(Double amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.CEILING);
        double formatAmount = Double.parseDouble(df.format(amount));
        return formatAmount;
    }

    /**
     * 数字保留，两位小数，结果不大于原数字
     */
    public static Double formatDown(Double amount) {
        BigDecimal bg = new BigDecimal(amount);
        double formatAmount = bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        return formatAmount;
    }

    /**
     * 计算两个日期相差多少天.
     * @param startDay
     * @param endDate
     * @return
     */
    public static Integer getDaysByDifferenceDate(Date startDay, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeSec = calendar.getTimeInMillis();

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 0);
        calendarEnd.set(Calendar.MINUTE, 0);
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);
        long endTimeSec = calendarEnd.getTimeInMillis();
        return (int)((endTimeSec - startTimeSec) / (1000 * 60 * 60 * 24));
    }

    public static Date getStartDateByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        return startDate;
    }

    /**
     * 获取昨天日期
     * @param date
     * @return
     */
    public static Date getPreDateByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        Date startDate = calendar.getTime();
        return startDate;
    }

    /**
     * 获取明天日期
     * @param date
     * @return
     */
    public static Date getNextDateByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        Date startDate = calendar.getTime();
        return startDate;
    }

    /**
     * 获取num天日期
     * @param date
     * @return
     */
    public static Date getNewDateByDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, num);
        Date newDate = calendar.getTime();
        return newDate;
    }

    public static String getDateToString(Date date, String pattern) {
//        "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static Date getStringToDate(String dateStr, String pattern) {
//        "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取下个月的最小日期.
     * @param date
     * @return
     */
    public static Date getNextMonthMin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        Date nextMonthDate = calendar.getTime();
        return nextMonthDate;
    }

    /**
     * 获取上个月的最小日期.
     * @param date
     * @return
     */
    public static Date getPreMonthMin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        Date nextMonthDate = calendar.getTime();
        return nextMonthDate;
    }

    /**
     * 获取当月的最小日期.
     * @param date
     * @return
     */
    public static Date getCurrentMonthMin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date nextMonthDate = calendar.getTime();
        return nextMonthDate;
    }

    // 获得某天最大时间 2017-10-15 23:59:59
    public static Date getEndOfDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = df.format(date);
        dateStr = dateStr + " 23:59:59";
        SimpleDateFormat dfMax = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateMax = null;
        try {
            dateMax = dfMax.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateMax;
    }

    /**
     * 根据repayDate获取出账日.
     * @param repayDate
     * @return
     */
    public static Date getDateOfAccount(Date repayDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(repayDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.add(Calendar.DAY_OF_MONTH, -5);
//        calendar.add(Calendar.MONTH, -5);
        Date nextMonthDate = calendar.getTime();

        String temp = getDateToString(nextMonthDate, "yyyy-M-d");
        System.out.println(temp);

        /*BigDecimal bg = null;
        Double amount = 2271.2800001;

        bg = new BigDecimal(amount);
        System.out.println("amount = " + bg.setScale(2, BigDecimal.ROUND_UP).doubleValue());
//        bg = new BigDecimal(-1.0101);
//        System.out.println("-1.011 = " + bg.setScale(2, BigDecimal.ROUND_CEILING).doubleValue());
        bg = new BigDecimal(amount);
        System.out.println("amount = " + bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
//        bg = new BigDecimal(-1.016);
//        System.out.println("-1.016 = " + bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        double num = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
        System.out.println("num = " + num);
        System.out.println(String.format("%.2f", amount));

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.CEILING);
        System.out.println("DecimalFormat :: " + df.format(amount));*/
    }
}
