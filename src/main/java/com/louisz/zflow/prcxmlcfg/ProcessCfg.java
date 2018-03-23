package com.louisz.zflow.prcxmlcfg;

import java.util.ArrayList;
import java.util.List;

import com.louisz.zflow.constant.ZflowConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author zhang
 * @description process节点
 * @time 2018年2月2日
 */
@XStreamAlias("process")
public class ProcessCfg implements NodeCfg {
	@XStreamAsAttribute
	private String id = "";
	@XStreamAsAttribute
	private String name = "";
	@XStreamImplicit
	private List<FieldCfg> fields = new ArrayList<>();
	@XStreamImplicit
	private List<TaskCfg> tasks = new ArrayList<>();
	@XStreamImplicit
	private List<ProcessCfg> processes = new ArrayList<>();
	@XStreamImplicit
	private List<ForkCfg> forks;
	@XStreamImplicit
	private List<JoinCfg> joins;
	@XStreamImplicit
	private List<ConditionCfg> conditions;

	@Override
	public List<ConditionCfg> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionCfg> conditions) {
		this.conditions = conditions;
	}

	private RepeatCfg repeat;

	public List<ForkCfg> getForks() {
		return forks;
	}

	public void setForks(List<ForkCfg> forks) {
		this.forks = forks;
	}

	public List<JoinCfg> getJoins() {
		return joins;
	}

	public void setJoins(List<JoinCfg> joins) {
		this.joins = joins;
	}

	@Override
	public RepeatCfg getRepeat() {
		return repeat;
	}

	public void setRepeat(RepeatCfg repeat) {
		this.repeat = repeat;
	}

	private TransitionCfg transition;

	private StartCfg start;

	private EndCfg end;

	public EndCfg getEnd() {
		return end;
	}

	public void setEnd(EndCfg end) {
		this.end = end;
	}

	public StartCfg getStart() {
		return start;
	}

	public void setStart(StartCfg start) {
		this.start = start;
	}

	public List<ProcessCfg> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessCfg> processes) {
		this.processes = processes;
	}

	public List<TaskCfg> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskCfg> tasks) {
		this.tasks = tasks;
	}

	public List<FieldCfg> getFields() {
		return fields;
	}

	public void setFields(List<FieldCfg> fields) {
		this.fields = fields;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
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
		return ZflowConstant.NODE_TYPE_PROCESS;
	}

}
