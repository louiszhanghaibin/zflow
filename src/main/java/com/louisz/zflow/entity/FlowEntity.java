package com.louisz.zflow.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import com.louisz.zflow.util.JsonUtil;
import com.louisz.zflow.util.StringUtil;

public class FlowEntity implements Serializable {

	private static final long serialVersionUID = 8234029381213894069L;

	/**
	 * 主鍵 ID
	 */
	private String id;

	/**
	 * 流程实例名
	 */

	private String name;

	/**
	 * 流程定義 ID
	 */

	private String processId;

	/**
	 * 流程定义名
	 */
	private String processName;

	/**
	 * 最后更新時間
	 */

	private String updateTime;

	/**
	 * 结束时间
	 */
	private String finishTime;

	/**
	 * 流程示例 創建時間
	 */
	private String createTime;

	/**
	 * 流程实例变量
	 */
	private String variables;

	/**
	 * father flow
	 */
	private String father = "NULL";

	/**
	 * map of parameters for the flow
	 */
	private Map<String, String> variablesMap;

	/**
	 * 状态
	 */
	private String state;

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public Map<String, String> getVariablesMap() {
		return variablesMap;
	}

	public void setVariablesMap(Map<String, String> variablesMap) {
		this.variablesMap = variablesMap;
	}

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVariables() {
		return variables;
	}

	public void setVariables(String variables) {
		this.variables = variables;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getVariableMap() {
		if (StringUtil.isNotEmpty(variables))
			return (Map<String, Object>) JsonUtil.fromJson(variables, Map.class);
		else
			return Collections.emptyMap();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Flow={id=").append(this.id);
		sb.append(",name=").append(this.name);
		sb.append(",processId=").append(this.processId);
		sb.append(",processName=").append(this.processName);
		sb.append(",createTime=").append(this.createTime);
		sb.append(", updateTime=").append(this.updateTime);
		sb.append(",finishTime=").append(this.finishTime);
		sb.append(", state=").append(this.state);
		sb.append(",variables=").append(this.variables);
		sb.append("}");
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}
}
