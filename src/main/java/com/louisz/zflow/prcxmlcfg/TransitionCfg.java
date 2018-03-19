package com.louisz.zflow.prcxmlcfg;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * trasition 节点
 * 
 * @author zhang
 * @description
 * @time 2018年2月2日
 */
@XStreamAlias("transition")
public class TransitionCfg {
	@XStreamAsAttribute
	private String to = "";

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		String result = "transition={to=" + this.to + "}";
		return result;
	}

}
