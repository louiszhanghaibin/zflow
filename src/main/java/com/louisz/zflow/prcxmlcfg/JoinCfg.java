package com.louisz.zflow.prcxmlcfg;

import java.util.List;

import com.louisz.zflow.constant.ZflowConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author zhang
 * @description join节点
 * @time 2018年2月2日
 */
@XStreamAlias("join")
public class JoinCfg implements NodeCfg {
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
	public List<TransitionCfg> getTransitions() {
		return null;
	}

	@Override
	public String getType() {
		return ZflowConstant.NODE_TYPE_JOIN;
	}

	@Override
	public RepeatCfg getRepeat() {
		return null;
	}

	@Override
	public List<ConditionCfg> getConditions() {
		return null;
	}

}
