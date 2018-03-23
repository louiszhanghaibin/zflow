package com.louisz.zflow.prcxmlcfg;

import java.util.ArrayList;
import java.util.List;

import com.louisz.zflow.constant.ZflowConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author zhang
 * @description fork节点
 * @time 2018年2月2日
 */
@XStreamAlias("fork")
public class ForkCfg implements NodeCfg {
	@XStreamAsAttribute
	private String name = "";
	@XStreamImplicit
	private List<TransitionCfg> transitions = new ArrayList<>();
	@XStreamImplicit
	private List<ConditionCfg> conditions;

	@Override
	public List<ConditionCfg> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionCfg> conditions) {
		this.conditions = conditions;
	}

	public List<TransitionCfg> getTransitions() {
		return transitions;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTransitions(List<TransitionCfg> transitions) {
		this.transitions = transitions;
	}

	@Override
	public TransitionCfg getTransition() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("fork={name=").append(this.name);
		if (null != this.transitions && !this.transitions.isEmpty()) {
			this.transitions.forEach(transition -> {
				sBuilder.append(", ").append(transition.toString());
			});
		}
		sBuilder.append("}");
		return sBuilder.toString();
	}

	@Override
	public String getType() {
		return ZflowConstant.NODE_TYPE_FORK;
	}

	@Override
	public RepeatCfg getRepeat() {
		return null;
	}

}
