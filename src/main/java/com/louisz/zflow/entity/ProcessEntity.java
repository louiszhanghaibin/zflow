package com.louisz.zflow.entity;

import java.io.Serializable;

import com.louisz.zflow.prcxmlcfg.ProcessCfg;

/**
 * @author zhang
 * @description entity for PROCESS
 * @time 2018年2月2日
 */
public class ProcessEntity implements Serializable {

	private static final long serialVersionUID = 3866645078502421914L;

	/**
	 * 主键ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 状态，是否可用
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 最后更新時間
	 */
	private String updateTime;

	/**
	 * 流程定义xml
	 */
	private String content;

	/**
	 * parsed process XML bean
	 */
	private ProcessCfg process;

	public ProcessCfg getProcess() {
		return process;
	}

	public void setProcess(ProcessCfg process) {
		this.process = process;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
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

	public void setUpdateTime(String lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Process(id=").append(this.id);
		sb.append(",name=").append(this.name);
		sb.append(",state=").append(this.state);
		sb.append(",createTime=").append(this.createTime);
		sb.append(",updateTime=").append(this.updateTime);
		sb.append(",content=").append(this.content);
		return sb.toString();
	}

}
