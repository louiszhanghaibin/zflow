package com.louisz.zflow.dao;

import com.louisz.zflow.entity.TaskEntity;

/**
 * @author zhang
 * @description DAO for task table
 * @time 2018年2月5日
 */
public interface TaskDao {

	public void insertTask(TaskEntity task);

	public TaskEntity selectTaskById(String Id);

	public TaskEntity selectTaskByFlowIdAndTaskName(TaskEntity task);

	public void deleteTasksByFlowId(String flowId);

	public void updateTask(TaskEntity task);

}
