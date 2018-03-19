package com.louisz.zflow.util;

import java.util.List;

import com.louisz.zflow.prcxmlcfg.TaskCfg;

/**
 * @author zhang
 * @description link utility for a running process
 * @time 2018年2月7日
 */
public class NodeLinkUtil {
	private List<TaskCfg> tasks;

	private NodeLinkUtil nextNode;

	public List<TaskCfg> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskCfg> tasks) {
		this.tasks = tasks;
	}

	public NodeLinkUtil getNextNode() {
		return nextNode;
	}

	public void setNextNode(NodeLinkUtil nextNode) {
		this.nextNode = nextNode;
	}

}
