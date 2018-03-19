package com.louisz.zflow.entity;

import java.io.Serializable;

public class PlanEntity  implements Serializable {

	private static final long serialVersionUID = 8797182297845563667L;
	
	private String  id;
	private String 	processId;
	private String  flowName;
	private String 	cron;
	private String  variables;
	private int state;
	private String createTime;
	private String updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getVariables() {
		return variables;
	}
	public void setVariables(String variables) {
		this.variables = variables;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("plan(").append("id=").append(this.id);
		sb.append(", processId=").append(this.processId);
		sb.append(",flowName=").append(this.flowName);
		sb.append(",cron=").append(this.cron);
		sb.append(",state=").append(this.state);
		sb.append(",createTime=").append(this.createTime);
		sb.append(",updateTime=").append(this.updateTime);
		sb.append(")");
		return sb.toString();
	}
	
	

}
