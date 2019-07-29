package com.tutu.daogou.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class DateFormatUtil {

    private static final transient Log log = LogFactory.getLog(DateFormatUtil.class);

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATETIME_UNDERLINE_FORMAT = "yyyy-MM-dd_HH:mm:ss";

    public static final String DATETIME_NOSPLIT_FORMAT = "yyyyMMddHHmmss";

    public static final String DATETIME_FORMAT_MS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_CN = "yyyy年MM月dd日";

    public static final String DATE_FORMAT_BACKSLASH = "yyyy/MM/dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String RFC3339_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static ThreadLocal<DateFormat> datetimeFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATETIME_FORMAT);
        }
    };

    private static ThreadLocal<DateFormat> datatimeUnderlineFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATETIME_UNDERLINE_FORMAT);
        }
    };

    private static ThreadLocal<DateFormat> datatimeNoSplitFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATETIME_NOSPLIT_FORMAT);
        }
    };

    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT);
        }
    };

    private static ThreadLocal<DateFormat> dateFormatCN = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT_CN);
        }
    };

    private static ThreadLocal<DateFormat> timeFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(TIME_FORMAT);
        }
    };

    private static ThreadLocal<DateFormat> rfc3339DateFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(RFC3339_FORMAT);
        }
    };

    private static ThreadLocal<DateFormat> dateBackSlashFormat = new ThreadLocal<DateFormat>() {
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT_BACKSLASH);
        }
    };

    private static DateFormat getDatetimeFormat() {
        return (DateFormat) datetimeFormat.get();
    }

    private static DateFormat getDatetimeUnderlineFormat() {
        return (DateFormat) datatimeUnderlineFormat.get();
    }

    private static DateFormat getDatetimeNoSplitFormat() {
        return (DateFormat) datatimeNoSplitFormat.get();
    }

    private static DateFormat getDateFormat() {
        return (DateFormat) dateFormat.get();
    }

    private static DateFormat getDateFormatCN() {
        return (DateFormat) dateFormatCN.get();
    }

    private static DateFormat getTimeFormat() {
        return (DateFormat) timeFormat.get();
    }

    private static DateFormat getRfc3339DateFormat() {
        return (DateFormat) rfc3339DateFormat.get();
    }

    private static DateFormat getDateBackSlashFormat() {
        return (DateFormat) dateBackSlashFormat.get();
    }



    public static Date toDatetime(String string) {
        try {
            return getDatetimeFormat().parse(string);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(string, e);
            }
        }
        return null;
    }

    public static String toDatetimeString(Date date) {
        return getDatetimeFormat().format(date);
    }

    public static String toDatecnString(Date date) { return getDateFormatCN().format(date);}

    public static String toDatetimeUnderlineString(Date date) {
        return getDatetimeUnderlineFormat().format(date);
    }

    public static String toDatetimeNoSplitFormatString(Date date) {
        return getDatetimeNoSplitFormat().format(date);
    }

    public static String toDateBackSlashFormatString(Date date) {
        return getDateBackSlashFormat().format(date);
    }

    public static Date noSplitToDate(String noSplitString) {
        try {
            return getDatetimeNoSplitFormat().parse(noSplitString);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(noSplitString, e);
            }
        }
        return null;
    }

    public static Date toDate(String string) {
        try {
            return getDateFormat().parse(string);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(string, e);
            }
        }
        return null;
    }

    public static Date toDateBackSlash(String string) {
        try {
            return getDateBackSlashFormat().parse(string);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(string, e);
            }
        }
        return null;
    }

    public static Date toTime(String string) {
        try {
            return getTimeFormat().parse(string);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(string, e);
            }
        }
        return null;
    }

    public static String toTimeString(Date date) {
        return getTimeFormat().format(date);
    }

    public static boolean isExtDate(String string) {
        try {
            getRfc3339DateFormat().parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isDateTime(String string) {
        try {
            getDatetimeFormat().parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isDate(String string) {
        try {
            getDateFormat().parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date parseExtDate(String string) {
        try {
            return getRfc3339DateFormat().parse(string);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(string, e);
            }
        }
        return null;
    }

    public static String toDateString(Date date) {
        return getDateFormat().format(date);
    }

    // 转化UTC时间格式字符串(2007-10-23T17:15:44.000Z)
    public static Date parseUTCDate(String string) {
        string = string.replace("Z", " UTC");
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(string);
        } catch (ParseException e) {
            if (log.isErrorEnabled()) {
                log.error(string, e);
            }
        }
        return null;
    }

    /**
     * 获取当前服务器的时间，格式【yyyy-MM-dd HH:mm:ss SSS】
     * 
     * @return
     */
    public static String getNowDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String time = format.format(new Date());
        return time;
    }

    public static String getFormatDateTimeByExpress() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = "d" + format.format(new Date());
        return time;
    }

    /**
     * 获取当前服务器的时间，格式【yyyyMMddHHmmssSSS】
     * 
     * @return
     */
    public static String getNowDateTimeStamp() {
        return getDatetimeFormat().format(new Date());
    }

    /**
     * 获取当前时间戳
     * 
     * @return
     */
    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 
     * @param timeStamp
     * @param _format
     * @return
     */
    public static String getTimeStampToDate(long timeStamp, String _format) {
        Date date = new Timestamp(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat(_format);
        String now = format.format(date);
        return now;
    }

    /**
     * 时间戳转换成日期
     * 
     * @param timeStamp
     * @return
     */
    public static String getTimeStampToDate(String timeStamp) {
        if (!"".equals(timeStamp) && timeStamp != null && !"0".equals(timeStamp)) {
            String date = new Timestamp(Long.parseLong(timeStamp)).toString();
            date = date.split("\\x2E")[0];
            return date;
        } else {
            return "";
        }
    }

    /**
     * 时间戳转换成日期
     * 
     * @param timeStamp
     * @return
     */
    public static String getTimeStampToDate(long timeStamp) {
        String date = new Timestamp(timeStamp).toString();
        date = date.split("\\x2E")[0];
        return date;
    }

    /**
     * 时间戳转换成日期
     * 
     * @param timeStamp
     * @return
     */
    public static String getTimeStampToDate(Object ts) {
        if (ts == null)
            return null;
        return getTimeStampToDate(((Timestamp) ts).getTime());
    }

    /**
     * 日期（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）转换成时间戳
     * 
     * @param timeStamp
     * @return
     */
    public static long dateToTime(String date) {
        if (date == null || StringUtils.isEmpty(date))
            return 0l;
        long l = 0;
        Date d = new Date();
        try {
            String _date[] = date.split(" ");
            String temp[] = _date[0].split("/");
            if (temp.length > 1) {
                date = new StringBuffer(temp[2]).append("-").append(temp[1]).append("-").append(temp[0]).toString();
            }
            if (date.trim().length() <= 10) {
                date = date + " 00:00:00";
            }
            Format format = null;
            try {
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                try {
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                } catch (Exception ex) {
                    try {
                        format = new SimpleDateFormat("yyyy-MM-dd HH");
                    } catch (Exception ex1) {
                        format = new SimpleDateFormat("yyyy-MM-dd");
                    }
                }
            }

            try {
                d = (Date) format.parseObject(date);
            } catch (Exception ex) {

            }
            _date = null;
            temp = null;
            format = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        l = d.getTime();
        d = null;
        return l;
    }

    /**
     * 生成所有月份（从开始月到结束月）
     * 
     * @param startMonth
     * @param endMonth
     * @return
     */
    public static String[] monthToMonth(String startMonth, String endMonth) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        Date date1 = null; // 开始日期
        Date date2 = null; // 结束日期
        try {
            date1 = df.parse(startMonth);
            date2 = df.parse(endMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        // 定义集合存放月份
        List<String> list = new ArrayList<String>();
        list.add(startMonth);
        c1.setTime(date1);
        c2.setTime(date2);
        while (c1.compareTo(c2) < 0) {
            c1.add(Calendar.MONTH, 1);// 开始日期加一个月直到等于结束日期为止
            Date ss = c1.getTime();
            String str = df.format(ss);
            list.add(str);
        }
        // 存放入数组
        String[] str = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            str[i] = (String) list.get(i);
        }
        System.out.println(Arrays.toString(str));
        return str;

    }

    /**
     * 将一种格式时间转换成另一种时间格式的字符串
     * 
     * @param dateValue
     * @param beforeFormat
     * @param afterFormat
     * @return
     */
    public static String formatDate(String dateValue, String beforeFormat, String afterFormat) {
        Date date = null;
        SimpleDateFormat beforeFormatDate = new SimpleDateFormat(beforeFormat);
        SimpleDateFormat afterFormatDate = new SimpleDateFormat(afterFormat);
        try {
            date = beforeFormatDate.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return afterFormatDate.format(date);
    }

    /**
     * 取得系统时间与参数相差的之前的时间
     * 
     * @param date
     * @param beforeTime
     * @param i
     * @return
     */

    public static String getBeforeDateByYMdHms(Date date, String dateFormat, String beforeTime, int i) {
        Calendar cal = null;
        SimpleDateFormat format = null;
        if (dateFormat == null || dateFormat.equals("")) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            format = new SimpleDateFormat(dateFormat);
        }

        if (date != null) {
            format.format(date);
            cal = format.getCalendar();
        } else {
            cal = Calendar.getInstance();
        }

        cal = format.getCalendar();

        if ("YEAR".equals(beforeTime)) {
            cal.add(Calendar.YEAR, i);
        }
        if ("MONTH".equals(beforeTime)) {
            cal.add(Calendar.MONTH, i);
        }
        if ("DATE".equals(beforeTime)) {
            cal.add(Calendar.DATE, i);
        }
        if ("HOUR".equals(beforeTime)) {
            cal.add(Calendar.HOUR, i);
        }
        if ("MINUTE".equals(beforeTime)) {
            cal.add(Calendar.MINUTE, i);
        }

        return format.format(cal.getTime());
    }

    public static String addDays(String date, String dateFormat, int i){
        return DateFormatUtil.getBeforeDateByYMdHms(DateFormatUtil.getDateByString(date,dateFormat),dateFormat, "DATE", i);
    }
    public static String subDays(String date, String dateFormat, int i){
        return DateFormatUtil.getBeforeDateByYMdHms(DateFormatUtil.getDateByString(date,dateFormat),dateFormat, "DATE", i);
    }
    /**
     * 将字符串转成日期类型
     * 
     * @param dateString
     * @param strFormat
     * @return
     */
    public static Date getDateByString(String dateString, String strFormat) {
        Date date = null;
        SimpleDateFormat formatDate = new SimpleDateFormat(strFormat);
        try {
            date = formatDate.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据时间格式取得当前时间的字符串
     * 
     * @param strFormat
     * @return
     */
    public static String getTodayByFormat(Date date, String strFormat) {
        if (strFormat == null || strFormat.equals("")) {
            strFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(strFormat);
        String strDate = format.format(date);

        return strDate;

    }

    /** 日期格式:yyyyMMddHHmmss */
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    /** 日期格式:yyyy-MM-dd */
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    /** 日期格式:yyyy-MM-dd HH:mm:ss */
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String YYYY_MM_DD_HH_MM_SS_ORACLE = "yyyy-mm-dd hh24:mi:ss";
    /** 日期格式yyyy-MM-dd 23:59 */
    public static String YYYY_MM_DD_23_59 = "yyyy-MM-dd 23:59";
    /** 日期格式:yyyyMMdd */
    public static String YYYYMMDD = "yyyyMMdd";
    /** 日期格式:MMdd */
    public static String MM_DD = "MM-dd";

    /**
     * 获取当前系统时间
     * 
     * @return str
     */
    public static String getNowDate() {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return datetimeFormat.format(new Date());
    }

    /**
     * 获取当前系统时间
     * 
     * @return str
     */
    public static String getNowForPatter(String patter) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(patter);
        return datetimeFormat.format(new Date());
    }

    /**
     * 取当天23点59分59秒
     * 
     * @return Date
     */
    public static Date getTodayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 取当天零点零分零秒
     */
    public static Date getTodayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 取当月第一天
     */
    public static Date getMonthStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static void main(String[] args) {
        System.out.println(getTodayStart());
        System.out.println(getMonthStart());
    }

    /**
     * 取给定时间的零点零分零秒
     * 
     * @param date
     * @return
     */
    public static Date getDateStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * 获取系统时间前/后 days天
     * 
     * @param days
     * @param patter
     * @return
     */
    public static String getAfterDateStr(int days, String patter) {
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return sdf.format(calendar.getTime());
    }

    /***
     * 格式化指定日期
     * 
     * @param date
     * @param patter
     * @return
     */
    public static String formatDate(Date date, String patter) {
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        return sdf.format(date);
    }

    /**
     * * 字符串转换到时间格式 *
     * 
     * @param dateStr
     *            需要转换的字符串 *
     * @param formatStr
     *            需要格式的目标字符串 举例yyyy-MM-dd
     * @return Date 返回转换后的时间
     * @throws ParseException
     *             转换异常
     */
    public static Date stringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 指定日期计算多天前（后）的日期
     * 
     * @param days
     *            前后天数
     * @param patter
     *            日期格式
     * @param date
     *            日期
     * @return 跟patter一样的格式返回N天后的字符串日期
     */
    public static String countManyDayDateStr(int days, String date, String patter) {
        SimpleDateFormat sdf = new SimpleDateFormat(patter);
        Calendar calendar = Calendar.getInstance();
        try {
            Date newDate = stringToDate(date, patter);
            calendar.setTime(newDate);
            calendar.add(Calendar.DATE, days);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 计算n月前/后 的时间
     * 
     * @param month
     * @param format
     * @return
     */
    public static String addMonthDate(int month, String format) {
        return addMonthDate(month, format, null);
    }

    public static String addMonthDate(int month, String format, Date tagDate) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        if (tagDate != null) {
            cal.setTime(tagDate);
        }
        cal.add(Calendar.MONTH, month);// 对月份进行计算,减去12个月
        Date date = cal.getTime();
        return df.format(date);
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    
    /**
     * 
     * @param startDate  yyyy-MM-dd
     * @param endDate    yyyy-MM-dd
     * @return
     */
    public static Integer daysBetween(String startDate, String endDate){
    	try
    	{
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           Date sDate = sdf.parse(startDate);
           Date eDate = sdf.parse(endDate);
           Calendar cal = Calendar.getInstance();
           cal.setTime(sDate);
           long time1 = cal.getTimeInMillis();
           cal.setTime(eDate);
           long time2 = cal.getTimeInMillis();
           long between_days = (time2 - time1) / (1000 * 3600 * 24);

           return Integer.parseInt(String.valueOf(between_days));
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return null;
    }

    /**
     * 获取今天是星期几
     * 
     * @return
     */
    public static String getTodayOfWeek() {
        final String dayNames[] = { "w7", "w1", "w2", "w3", "w4", "w5", "w6" };
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;

        return dayNames[dayOfWeek];

    }
    
    /**
     * 获取传入的日期是星期几
     * 
     * @return
     */
    public static String getDateOfWeek(Date date) {
        final String dayNames[] = { "w7", "w1", "w2", "w3", "w4", "w5", "w6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;

        return dayNames[dayOfWeek];

    }

    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分";
    }

    // 得到当前时间所在的年份
    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    // 得到当前时间所在的年份
    public static int getYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    // 得到当前时间所在的月份
    public static int getMonth(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 拆分给定字符串构造本月月初 格式为：2009-08-01
     */
    public static String giveMonthFist(String strdate) {
        // 以“－”为分隔符拆分字符串
        String strArray[] = strdate.split("-");

        String tempyear = strArray[0]; // 得到字符串中的年

        String tempmonth = strArray[1]; // 得到字符串中的月
        // 拼接成月首字符串
        return tempyear + "-" + tempmonth + "-01";
    }

    /**
     * 拆分给定字符串构造本月月末 格式为：2009-08-01
     */
    public static String giveMonthLast(String strdate) {
        // 先得到下个月的同一天
        String addmonth = DateFormatUtil.addMonth(strdate);
        // 得到下个月的月初
        String monthfirst = DateFormatUtil.giveMonthFist(addmonth);
        // 下个月月初减一天为本月月末
        String subday = DateFormatUtil.subDay(monthfirst);
        return subday;
    }

    /**
     * 拆分给定字符串构造上个月月初 格式为：2009-08-01
     */
    public static String giveBeforeMonthFirst(String strdate) {
        // 调用得到上个月的函数
        String beforemonth = DateFormatUtil.subMonth(strdate);
        // 调用构造月初的函数
        return DateFormatUtil.giveMonthFist(beforemonth);
    }

    /**
     * 拆分给定字符串构造上个月月末 格式为：2009-08-01
     */
    public static String giveBeforeMonthLast(String strdate) {
        // 先调用函数得到本月月初
        String monthfirst = DateFormatUtil.giveMonthFist(strdate);
        // 调用当前日期减一天方法得到上个月月末
        return DateFormatUtil.subDay(monthfirst);
    }

    /**
     * 给定的日期减一天 格式为：2009-08-01
     */
    public static String subDay(String strdate) {
        Date date = new Date(); // 构造一个日期型中间变量
        String dateresult = null; // 返回的日期字符串
        // 创建格式化格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // 加减日期所用
        GregorianCalendar gc = new GregorianCalendar();
        try {
            date = df.parse(strdate); // 将字符串格式化为日期型
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gc.setTime(date); // 得到gc格式的时间
        gc.add(5, -1); // 2表示月的加减，年代表1依次类推(３周....5天。。)
        // 把运算完的时间从新赋进对象
        gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));
        // 在格式化回字符串时间
        dateresult = df.format(gc.getTime());
        return dateresult;
    }

    /**
     * 给定的日期加一个月 格式为：2009-08-01
     */
    public static String addMonth(String strdate) {
        return addMonth(strdate, 1);
    }

    /**
     * 给定的日期加monNum个月 格式为：2009-08-01
     *
     * @param strdate
     * @param monNum
     * @return
     */
    public static String addMonth(String strdate, int monNum) {
        Date date = new Date(); // 构造一个日期型中间变量
        String dateresult = null; // 返回的日期字符串
        // 创建格式化格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // 加减日期所用
        GregorianCalendar gc = new GregorianCalendar();
        try {
            date = df.parse(strdate); // 将字符串格式化为日期型
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gc.setTime(date); // 得到gc格式的时间
        gc.add(2, monNum); // 2表示月的加减，年代表1依次类推(周,天。。)
        // 把运算完的时间从新赋进对象
        gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));
        // 在格式化回字符串时间
        dateresult = df.format(gc.getTime());
        return dateresult;
    }

    /**
     * 给定的日期减一个月 格式为：2009-08-01
     */
    public static String subMonth(String strdate) {

        Date date = new Date(); // 构造一个日期型中间变量

        String dateresult = null; // 返回的日期字符串
        // 创建格式化格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // 加减日期所用
        GregorianCalendar gc = new GregorianCalendar();
        try {
            date = df.parse(strdate); // 将字符串格式化为日期型
        } catch (ParseException e) {
            e.printStackTrace();
        }
        gc.setTime(date); // 得到gc格式的时间
        gc.add(2, -1); // 2表示月的加减，年代表1依次类推(周,天。。)
        // 把运算完的时间从新赋进对象
        gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));
        // 在格式化回字符串时间
        dateresult = df.format(gc.getTime());
        return dateresult;
    }

    /**
     * 判断是否是当月的最后一天
     *
     * @param curDate
     * @return
     */
    public static Boolean IsLastDayInMonth(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DATE, 1); // 设置为该月第一天
        calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        Date otherDate = calendar.getTime();
        return otherDate.equals(curDate);
    }

    /**
     * 给定年月字串返回一个整型月份 格式为：2009-08-01
     */
    public static Integer returnday(String yrmoday) {
        // 以“－”为分隔符拆分字符串
        String strArray[] = yrmoday.split("-");
        String tempday = strArray[2]; // 得到字符串中的日
        return new Integer(tempday);
    }
    
    /**
     * 获取两个日期相差的月份数,注意多一天,则要增加一个月
     * @issueDay 放款日,如15,20
     * @startDate 逾期开始日
     * @endDate 逾期结束日
     */
    public static int getMonthDiff(int issueDay,Date startDate,Date endDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);
        int day1 = c.get(Calendar.DAY_OF_MONTH);

        c.setTime(startDate);
        int year2 = c.get(Calendar.YEAR);
        int month2 = c.get(Calendar.MONTH);
        //int day2 = c.get(Calendar.DAY_OF_MONTH);

        int result;
        if (year1 == year2) {
            result = month1 - month2;
        } else {
            result = 12 * (year1 - year2) + month1 - month2;
        }
        result = result + (((day1 - issueDay) > 0) ? 1 : 0);
        return result;
    }

}
