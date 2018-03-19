package com.louisz.zflow.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final String DATE_FORMAT_DEFAULT = "yyyyMMdd HH:mm:ss";

	/**
	 * 返回标准格式的当前时间 yyyyMMdd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
		Date date = new Date();
		return format.format(date);
	}

	/**
	 * 在当前日期基础上，增加或加少i天， 返回yyyyMMdd格式的日期
	 * 
	 * @param i
	 *            增加或减少的天数
	 * @return
	 */
	public static String getDate(Integer i) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, i);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(cal.getTime());
	}

	/**
	 * 在当前日期基础上，增加或加少i月， 返回yyyyMM格式的日期
	 * 
	 * @param i
	 *            增加或减少的月数
	 * @return
	 */
	public static String getMonth(Integer i) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, i);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		return format.format(cal.getTime());
	}

}
