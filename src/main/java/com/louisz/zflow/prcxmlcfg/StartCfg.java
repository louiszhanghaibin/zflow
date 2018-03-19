package com.louisz.zflow.prcxmlcfg;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author zhang
 * @description start节点
 * @time 2018年2月2日
 */
@XStreamAlias("start")
public class StartCfg {
	@XStreamAsAttribute
	private String name = "";

	private TransitionCfg transition;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TransitionCfg getTransition() {
		return transition;
	}

	public void setTransition(TransitionCfg transition) {
		this.transition = transition;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("START={").append("name=").append(this.name);
		if (null != this.transition) {
			sBuilder.append(", ").append(this.transition.toString());
		}
		sBuilder.append("}");

		return sBuilder.toString();
	}

}
