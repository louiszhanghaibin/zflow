package com.louisz.zflow.prcxmlcfg;

import java.util.List;

import com.louisz.zflow.constant.ZflowConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author zhang
 * @description end节点
 * @time 2018年2月2日
 */
@XStreamAlias("end")
public class EndCfg implements NodeCfg {
	@XStreamAsAttribute
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public TransitionCfg getTransition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransitionCfg> getTransitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return ZflowConstant.NODE_TYPE_END;
	}

	@Override
	public RepeatCfg getRepeat() {
		// TODO Auto-generated method stub
		return null;
	}

}
