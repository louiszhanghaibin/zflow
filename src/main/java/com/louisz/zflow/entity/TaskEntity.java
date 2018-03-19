package com.louisz.zflow.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.louisz.zflow.prcxmlcfg.TaskCfg;
import com.louisz.zflow.util.JsonUtil;
import com.louisz.zflow.util.StringUtil;

public class TaskEntity implements Serializable {
	private static final long serialVersionUID = -57576037706626126L;

	/**
	 * 主键 id
	 */

	private String id;

	/**
	 * 流程Id
	 */
	private String flowId;

	/**
	 * 流程实例名字
	 */
	private String flowName;

	/**
	 * 流程定义Id
	 */
	private String processId;

	/**
	 * 流程定义名
	 */
	private String processName;

	/**
	 * 任务名字
	 */
	private String name;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 完成时间
	 */
	private String finishTime;

	/**
	 * 任务模型对象
	 */
	private TaskCfg taskCfg;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 任务变量
	 */
	private String variables;

	/**
	 * response massage from the executed task
	 */
	private String message;

	/**
	 * node IP
	 */
	private String nodeIp;

	/**
	 * map for parameters
	 */
	private Map<String, String> variablesMap = new HashMap<>();

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public Map<String, String> getVariablesMap() {
		return variablesMap;
	}

	public void setVariablesMap(Map<String, String> variablesMap) {
		this.variablesMap = variablesMap;
	}

	public TaskCfg getTaskCfg() {
		return taskCfg;
	}

	public void setTaskCfg(TaskCfg taskCfg) {
		this.taskCfg = taskCfg;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String taskName) {
		this.name = taskName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getVariables() {
		return variables;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getVariableMap() {
		return this.variables == null ? new HashMap<String, Object>() : JsonUtil.fromJson(this.variables, Map.class);
	}

	public void setVariables(String variables) {
		this.variables = variables;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Task(id=").append(this.id);
		sb.append(",taskName=").append(this.name);
		sb.append(",flowId=").append(this.flowId);
		sb.append(",flowName=").append(this.flowName);
		sb.append(",processId=").append(this.processId);
		sb.append(",processName=").append(this.processName);
		sb.append(",state=").append(this.state);
		sb.append(",variables=").append(this.variables);
		sb.append(",taskCfg=").append(this.taskCfg.toString());
		sb.append(",createTime=").append(this.createTime);
		sb.append(",updateTime=").append(this.updateTime);
		sb.append(",finishTime=").append(this.finishTime);
		sb.append(")");
		return sb.toString();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	private transient Map<String, Object> updateVariables = null;

	public void updateVariables(Map<String, Object> args) {
		if (args == null)
			return;
		if (this.updateVariables == null) {
			this.updateVariables = new HashMap<String, Object>();
		}
		this.updateVariables.putAll(args);
		// 需要将流程参数map进行还原更新
		// args.remove(Constant.ARGS_COMMON);
		if (StringUtil.isNotEmpty(this.variables)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> vars = JsonUtil.fromJson(this.variables, Map.class);
			vars.putAll(args);
			this.variables = JsonUtil.toJson(vars);
		} else {
			this.variables = JsonUtil.toJson(args);
		}
	}

	public void setUpdateVariables(Map<String, Object> updateVariables) {
		this.updateVariables = updateVariables;
	}

	public Map<String, Object> getUpdateVariables() {
		return this.updateVariables;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
