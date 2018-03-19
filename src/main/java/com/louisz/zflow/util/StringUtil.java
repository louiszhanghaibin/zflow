package com.louisz.zflow.util;

import java.util.UUID;

public class StringUtil {
	private static final String numericRegx = "\\d{1,}";

	/**
	 * 获取uuid类型的字符串
	 * 
	 * @return uuid字符串
	 */
	public static String getPrimaryKey() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return 是否为空标识
	 */
	public static boolean isEmpty(String str) {
		return (null == str || 0 == str.length());
	}

	/**
	 * 判断字符串是否为非空
	 * 
	 * @param str
	 *            字符串
	 * @return 是否为非空标识
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * validate if the string is numeric or not
	 * 
	 * @author zhang
	 * @time 2018年3月14日下午2:19:26
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		return str.matches(numericRegx);
	}
}
