package com.louisz.zflow.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.NumberTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.louisz.zflow.constant.ZflowConstant;

/**
 * 校验表达式管理类
 * 
 * @author zhanghb
 * @description
 * @time 2018年1月17日
 */
@Component
@Scope("prototype")
public class ExpressionEvaluaterUtil {

	/**
	 * 缓存Map
	 */
	private Map<String, Expression> expMap = new HashMap<>();

	private VelocityEngine ve = new VelocityEngine();

	private static Logger logger = LoggerFactory.getLogger(ExpressionEvaluaterUtil.class);

	public ExpressionEvaluaterUtil() {
		this.veInit();
	}

	/**
	 * 规则表达式校验
	 * 
	 * @author zhanghb
	 * @time 2018年1月5日下午5:41:16
	 * @param expressionOp
	 * @return
	 * @throws Exception
	 */
	public boolean checkExpression(String expString, Map<String, String> variableMap) throws Exception {
		String idPattern = "[jobId=" + variableMap.get(ZflowConstant.JOB_ID) + ", flowId="
				+ variableMap.get(ZflowConstant.FLOW_ID) + ", processId=" + variableMap.get(ZflowConstant.PROCESS_ID)
				+ "]";

		String oriExpString = "";
		try {
			oriExpString = getOriExpString(expString, variableMap);
		} catch (Exception e) {
			String mString = idPattern + "Exception happend while parsing expression[" + expString + "] via VELOCITY!";
			logger.error(mString, e);
			throw e;
		}

		JexlContext jContext = new MapContext();
		boolean b = false;
		jContext.set("b", b);
		for (String key : variableMap.keySet()) {
			jContext.set(key, variableMap.get(key));
		}

		try {
			Expression exp = getExpression(oriExpString);
			exp.evaluate(jContext);
			b = (boolean) jContext.get("b");
		} catch (Exception e) {
			String mString = idPattern + "Exception happend while parsing expression[" + oriExpString + "] via JEXL2!";
			logger.error(mString, e);
			throw e;
		}

		return b;
	}

	/**
	 * 获取表达式，以创建的直接获得
	 * 
	 * @author zhanghb
	 * @time 2018年1月5日下午5:42:01
	 * @param op
	 * @param oriExpString
	 * @return
	 * @throws Exception
	 */
	private Expression getExpression(String oriExp) throws Exception {
		String key = oriExp;
		if (expMap.containsKey(key)) {
			return expMap.get(key);
		}

		JexlEngine jexlEngine = new JexlEngine();
		String expString = "b=(" + oriExp + ")";
		Expression exp = jexlEngine.createExpression(expString);
		expMap.put(key, exp);

		return exp;
	}

	/**
	 * 通过velocity渲染表达式原始语句
	 * 
	 * @author zhanghb
	 * @time 2018年1月5日下午5:43:04
	 * @param expressionTpl
	 * @return
	 * @throws Exception
	 */
	private String getOriExpString(String expressionTpl, Map<String, String> variableMap) throws Exception {
		VelocityContext context = new VelocityContext();
		// put the arguments in runtime into velocity context, then these arguments can
		// be called like $!XXX while velocity evaluating
		for (String key : variableMap.keySet()) {
			context.put(key, variableMap.get(key));
		}

		context.put("numberTool", new NumberTool());
		// 把参数map放入velo上下文进行渲染，在velo模板中可以直接取参数map中的值，用法：$!variableMap.get("XXXX")
		context.put("variableMap", variableMap);

		String expString = "";

		// 进行velocity模板渲染，获得渲染后的结果
		StringWriter sWriter = new StringWriter();
		ve.evaluate(context, sWriter, "", expressionTpl);
		expString = sWriter.toString();

		return expString;
	}

	/**
	 * 初始化velocity模板
	 * 
	 * @throws VelocityException
	 */
	private void veInit() throws VelocityException {
		VelocityEngine vEngine = new VelocityEngine();
		vEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		vEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		vEngine.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		vEngine.setProperty("runtime.log.logsystem.log4j.category", "velocity");
		vEngine.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		vEngine.setProperty("runtime.log.error.stacktrace", false);
		vEngine.setProperty("runtime.log.warn.stacktrace", false);
		vEngine.setProperty("runtime.log.info.stacktrace", false);
		vEngine.init();

		this.ve = vEngine;
	}

}
