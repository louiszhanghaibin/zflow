package com.louisz.zflow.dao;

import java.util.List;

import com.louisz.zflow.entity.PlanEntity;

/**
 * DAO for table schedule
 * 
 * @author zhang
 * @description
 * @time 2018年2月27日
 */
public interface ScheduleDao {
	List<PlanEntity> selectAll();
}
