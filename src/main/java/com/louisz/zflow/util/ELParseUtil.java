package com.louisz.zflow.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * utility for parsing EL
 * 
 * @author zhang
 * @description
 * @time 2018年2月26日
 */
public class ELParseUtil {
	private final static String ELPattern = "#\\{.*?\\}";
	private static handleChain hchain;

	private ELParseUtil() {
		hchain = new StaticFunctionHandler();
	}

	// 必须创建对象
	static {
		new ELParseUtil();
	}

	/**
	 * 處理表達式 的抽象類
	 * 
	 * @author zhoushuang
	 * 
	 */
	abstract class handleChain {
		handleChain superior = null;

		void setSuperior(handleChain hchain) {
			this.superior = hchain;
		}

		handleChain getSuperior() {
			return superior;
		}

		Object doChain(Object obj, String el) {
			Object ret = handle(obj, el);
			if (ret == null && this.getSuperior() != null)
				return this.getSuperior().doChain(obj, el);
			return ret;
		}

		abstract Object handle(Object obj, String el);
	}

	/**
	 * 解析类的静态函数调用, 例如解析 cmsz.autoflow.engine.helper.DateHelper.getDate(2)，
	 * cmsz.autoflow.engine.helper.DateHelper为类名，getDate为类的静态方法。
	 * 
	 * @author zhoushuang
	 * 
	 */
	class StaticFunctionHandler extends handleChain {

		private final Logger logger = LoggerFactory.getLogger(StaticFunctionHandler.class);

		@Override
		Object handle(Object obj, String elParameter) {
			String el = elParameter.trim();
			if (obj != null || el.startsWith("."))
				return null;
			String func = el.substring(0, el.indexOf("(")).trim();
			String paras = el.substring(el.indexOf("(") + 1, el.indexOf(")")).trim();
			String classname = func.substring(0, el.lastIndexOf(".")).trim();
			String sfunc = func.substring(func.lastIndexOf(".") + 1).trim();

			Object[] objargs = null;
			if (StringUtil.isNotEmpty(paras)) {
				String[] args = paras.split(",");
				objargs = new Object[args.length];
				for (int i = 0; i < args.length; i++) {
					args[i] = args[i].trim();
					if (args[i].matches("\"\\w+\""))
						objargs[i] = new String(args[i].substring(1, args[i].length() - 1));
					else if (args[i].matches("\\-?\\d+"))
						objargs[i] = Integer.parseInt(args[i]);
					else
						return null;
				}

			}
			try {
				return invokeStaticMethod(classname, sfunc, objargs);
			} catch (Exception e) {
				logger.warn("Exception happend while invoke static method!", e);
				return null;
			}
		}
	}

	/**
	 * 重新組裝字符串, 并解析 #｛静态函数调用｝
	 * 
	 * @param el
	 *            需要解析的字符串 例如
	 *            "settleDate=#{cmsz.autoflow.engine.helper.DateHelper.getDate(-1)}"
	 *            ，当前日期是2015/04/10,则返回 settleDate="20150409" (包括=之前的都是)
	 * @return String
	 */
	public static String reassemble(String el) {
		String targ = new String(el);
		Pattern pat = Pattern.compile(ELPattern);
		Matcher mat = pat.matcher(el);
		while (mat.find()) {
			String mstr = mat.group();
			String elstr = mstr.replaceAll("[\\#\\{\\}]", "");
			Object obj = hchain.doChain(null, elstr);
			if (obj == null) {
				targ = targ.replace(mstr, "null");
			} else if (obj instanceof String) {
				String val = "\"" + (String) obj + "\"";
				targ = targ.replace(mstr, val);
			} else
				targ = targ.replace(mstr, obj.toString());
		}
		return targ;
	}

	/**
	 * 调用类的静态方法
	 * 
	 * @param className
	 *            类名
	 * @param methodName
	 *            方法名
	 * @param args
	 *            参数
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeStaticMethod(String className, String methodName, Object[] args)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class ownerClass = Class.forName(className);
		Class[] argsClass = null;
		if (args != null) {
			argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
			}
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(null, args);
	}

}
