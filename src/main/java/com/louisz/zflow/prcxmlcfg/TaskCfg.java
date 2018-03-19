package com.louisz.zflow.prcxmlcfg;

import java.util.ArrayList;
import java.util.List;

import com.louisz.zflow.constant.ZflowConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * task节点
 * 
 * @author zhang
 * @description
 * @time 2018年2月2日
 */
@XStreamAlias("task")
public class TaskCfg implements NodeCfg {
	@XStreamAsAttribute
	private String name = "";

	@XStreamAsAttribute
	private String refUri = "";

	private TransitionCfg transition;

	@XStreamImplicit
	private List<FieldCfg> fields = new ArrayList<>();

	private RepeatCfg repeat;

	@Override
	public RepeatCfg getRepeat() {
		return repeat;
	}

	public void setReapeat(RepeatCfg repeat) {
		this.repeat = repeat;
	}

	public TransitionCfg getTransition() {
		return transition;
	}

	public void setTransition(TransitionCfg transition) {
		this.transition = transition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRefUri() {
		return refUri;
	}

	public void setRefUri(String refUri) {
		this.refUri = refUri;
	}

	public List<FieldCfg> getFields() {
		return fields;
	}

	public void setFields(List<FieldCfg> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("{name=" + this.name + ", refUri=" + this.refUri);
		sBuilder.append(", ").append(this.transition.toString());
		if (null != this.repeat) {
			sBuilder.append(", ").append(this.repeat.toString()).append(", ");
		}
		if (null != this.fields && !this.fields.isEmpty()) {
			this.fields.forEach(f -> sBuilder.append(f.toString() + ", "));
		}
		sBuilder.append("}");
		return sBuilder.toString();
	}

	@Override
	public List<TransitionCfg> getTransitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return ZflowConstant.NODE_TYPE_TASK;
	}

}
