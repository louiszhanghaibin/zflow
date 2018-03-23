package com.louisz.zflow.prcxmlcfg;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * entity for condition node
 * 
 * @author zhang
 * @description
 * @time 2018年3月23日
 */
@XStreamAlias("condition")
public class ConditionCfg {
	@XStreamAsAttribute
	private String refClass = "";
	@XStreamAsAttribute
	private String expression = "";
	@XStreamAsAttribute
	private String quit;

	public String getQuit() {
		return quit;
	}

	public void setQuit(String quit) {
		this.quit = quit;
	}

	public String getRefClass() {
		return refClass;
	}

	public void setRefClass(String refClass) {
		this.refClass = refClass;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
