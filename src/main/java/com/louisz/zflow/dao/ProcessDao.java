package com.louisz.zflow.dao;

import com.louisz.zflow.entity.ProcessEntity;

/**
 * @author zhang
 * @description DAO for process table
 * @time 2018年2月2日
 */
public interface ProcessDao {
	/**
	 * find process by id from table PROCESS
	 * 
	 * @author zhang
	 * @time 2018年2月2日下午3:35:58
	 * @param id
	 * @return
	 */
	public ProcessEntity findProcessById(String id);

	/**
	 * insert process content into table PROCESS
	 * 
	 * @author zhang
	 * @time 2018年2月2日下午3:36:18
	 * @param process
	 */
	public void insertProcess(ProcessEntity process);

	/**
	 * delete process data from table PROCESS by id
	 * 
	 * @author zhang
	 * @time 2018年2月2日下午3:36:52
	 * @param id
	 */
	public void deleteProcessById(String id);
}
