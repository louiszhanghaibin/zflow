package com.louisz.zflow.dao;

import java.util.List;
import java.util.Map;

import com.louisz.zflow.entity.FlowEntity;

/**
 * @author zhang
 * @description DAO for running flows
 * @time 2018年2月5日
 */
public interface FlowDao {

	public void insertFlow(FlowEntity flow);

	public FlowEntity selectFlowById(String flowId);

	public void updateFlowState(FlowEntity flow);

	public List<FlowEntity> getFlowSelective(Map<String, Object> paraMap);

}
