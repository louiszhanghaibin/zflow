package com.louisz.zflow.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	/**
	 * jackson的ObjectMapper对象
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 将对象转换为json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		if (object == null)
			return "";
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			log.warn("write to json string error:" + object, e);
			return "";
		}
	}

	/**
	 * 根据指定类型解析json字符串，并返回该类型的对象
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtil.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			log.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	public static boolean validate(String jsonString) {
		if (StringUtil.isEmpty(jsonString))
			return false;
		try {
			mapper.readValue(jsonString, Map.class);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
