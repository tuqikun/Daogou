package com.tutu.daogou.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by lsk on 2017/2/14.
 */
public class DateUtil extends tool.util.DateUtil {

	@SuppressWarnings("deprecation")
	public static Date dateAddMins(Date date, int minCnt) {
		Date d = new Date(date.getTime());
		d.setMinutes(d.getMinutes() + minCnt);
		return d;
	}

	/**
	 * 计算时间差,单位分
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int minuteBetween(Date date1, Date date2) {
		DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_001);
		Calendar cal = Calendar.getInstance();
		try {
			Date d1 = sdf.parse(DateUtil.dateStr4(date1));
			Date d2 = sdf.parse(DateUtil.dateStr4(date2));
			cal.setTime(d1);
			long time1 = cal.getTimeInMillis();
			cal.setTime(d2);
			long time2 = cal.getTimeInMillis();
			return Integer.parseInt(String.valueOf((time2 - time1) / 60000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算时间天数差
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int dayBetween(Date date1, Date date2) {
		DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_001);
		Calendar cal = Calendar.getInstance();
		try {
			Date d1 = sdf.parse(DateUtil.dateStr4(date1));
			Date d2 = sdf.parse(DateUtil.dateStr4(date2));
			cal.setTime(d1);
			long time1 = cal.getTimeInMillis();
			cal.setTime(d2);
			long time2 = cal.getTimeInMillis();
			return Integer.parseInt(String.valueOf((time2 - time1) / (60000 * 60 * 24)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[]  args){
		System.out.println(DateUtil.dayBetween(DateFormatUtil.toDatetime("2019-05-01 19:04:52.0"),new Date()));
	}

	/**
	 * 获取指定时间天的开始时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 0, 0, 0);
		return cal.getTime();
	}

	/**
	 * 获取指定时间天的结束时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 23, 59, 59);
		return cal.getTime();
	}

	/**
	 * String转化Date格式
	 *
	 * @param date
	 * @param type
	 * @return
	 */
	public static Date parse(String date, String type) {
		SimpleDateFormat formatter = new SimpleDateFormat(type);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(date, pos);
		return strtodate;

	}

	/**
	 * @param date
	 * @param beforeDays
	 *获取指定天数前多少天的集合
	 * @return
	 */
	public static  List<String> getDatePeriod(Date date, int beforeDays){
		List<String> datePeriodList = new ArrayList<String>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		for(int i=beforeDays-1;i>=0;i--){
			cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-i);
			datePeriodList.add(dateFormat.format(cal.getTime()));
		}
		return datePeriodList;
	}

	/**
	 * 得到指定日期之间的天数集合
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static List<Date> dateSplit(Date startDate, Date endDate)
			throws Exception {
		// if (!startDate.before(endDate))
		//     throw new Exception("开始时间应该在结束时间之后");
		Long spi = endDate.getTime() - startDate.getTime();
		Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

		List<Date> dateList = new ArrayList<Date>();
		dateList.add(endDate);
		for (int i = 1; i <= step; i++) {
			dateList.add(new Date(dateList.get(i - 1).getTime()
					- (24 * 60 * 60 * 1000)));// 比上一天减一
		}
		return dateList;
	}

	/**
	 * 得到指定日期之间的月数集合
	 *
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(sdf.parse(minDate));
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

		max.setTime(sdf.parse(maxDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sdf.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}

		return result;
	}

	/**
	 * 得到指定之前的前后几天
	 *
	 * @param day
	 * @param date
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getDateBefore(int day, Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);//把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Date dateAddDays(Date date, int days) {
		Date d = new Date(date.getTime());
		d.setDate(d.getDate() + days);
		return d;
	}

	/**
	 * 计算2个时间之间间隔的具体间隔天数
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @param connector 连接符 -
	 * @return startDate和endDate之间具体的日期range
	 */
	private static List<String> calculateDateRange(List<String> ret, LocalDate startDate, LocalDate endDate, String connector) {
		// startDate比endDate大直接返回
		if (startDate.isAfter(endDate)) {
			return ret;
			// startDate 和 endDate相等
		} else if (startDate.equals(endDate)) {
			ret.add(startDate.format(DateTimeFormatter.ofPattern("yyyy" + connector + "MM" + connector + "dd")));
			return ret;
			// 同年同一个月
		} else if (startDate.getYear() == endDate.getYear() && startDate.getMonthValue() == endDate.getMonthValue()) {
			int startYear = startDate.getYear();
			int startMonth = startDate.getMonthValue();
			int startMonthDay = startDate.getDayOfMonth();
			int startEndMonthDay = endDate.getDayOfMonth();
			StringBuilder builder = new StringBuilder();
			for (int day = startMonthDay; day <= startEndMonthDay; day++) {
				builder.append(startYear).append(connector).append(lessThan10Supplement0(startMonth)).append(connector).append(lessThan10Supplement0(day));
				ret.add(builder.toString());
				builder.setLength(0);
			}
			return ret;
		}
		// 存在跨月的情况
		int startYear = startDate.getYear();
		int startMonth = startDate.getMonthValue();
		int startMonthDay = startDate.getDayOfMonth();
		int startEndMonthDay = startDate.getMonth().length(isLeapYear(startDate.getYear()));
		StringBuilder builder = new StringBuilder();
		for (int day = startMonthDay; day <= startEndMonthDay; day++) {
			builder.append(startYear).append(connector).append(lessThan10Supplement0(startMonth)).append(connector).append(lessThan10Supplement0(day));
			ret.add(builder.toString());
			builder.setLength(0);
		}

		// 如果存在跨月份需要递归进行操作
		if (startDate.plusMonths(1).isBefore(endDate)) {
			LocalDate newStartDate = startDate.plusMonths(1);
			long minusDay = startMonthDay - 1L;
			newStartDate = newStartDate.minusDays(minusDay);
			calculateDateRange(ret, newStartDate, endDate, connector);
		} else {
			int endYear = endDate.getYear();
			int endStartDay = 1;
			int endEndDay = endDate.getDayOfMonth();
			int endMonth = endDate.getMonthValue();
			for (int day = endStartDay; day <= endEndDay; day++) {
				builder.append(endYear).append(connector).append(lessThan10Supplement0(endMonth)).append(connector).append(lessThan10Supplement0(day));
				ret.add(builder.toString());
				builder.setLength(0);
			}
		}

		return ret;
	}

	/**
	 * 计算2个时间之间间隔的具体间隔天数, 返回的时间格式化connector
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return startDate和endDate之间具体的日期range
	 */
	public static List<String> calculateDateRange(LocalDate startDate, LocalDate endDate, String connector) {
		List<String> ret = new ArrayList<>();
		return calculateDateRange(ret, startDate, endDate, connector);
	}

	/**
	 * 计算2个时间之间间隔的具体间隔天数,默认返回的时间格式化yyyy-MM-dd
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return startDate和endDate之间具体的日期range
	 */
	public static List<String> calculateDateRange(LocalDate startDate, LocalDate endDate) {
		return calculateDateRange(startDate, endDate, "-");
	}

	/**
	 * 小于10补0
	 *
	 * @param num 需要和10判断的树
	 * @return 进行补0操作
	 */
	public static String lessThan10Supplement0(int num) {
		return num < 10 ? "0" + num : num + "";
	}

	/**
	 * 判断是否是闰年
	 *
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
	}

	public static int hourBetween(Date date1, Date date2) {
		DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_001);
		Calendar cal = Calendar.getInstance();
		try {
			Date d1 = sdf.parse(DateUtil.dateStr4(date1));
			Date d2 = sdf.parse(DateUtil.dateStr4(date2));
			cal.setTime(d1);
			long time1 = cal.getTimeInMillis();
			cal.setTime(d2);
			long time2 = cal.getTimeInMillis();
			return Integer.parseInt(String.valueOf((time2 - time1) / (60000 * 60)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
