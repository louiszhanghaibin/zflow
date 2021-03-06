package com.louisz.zflow.handler;

import java.util.Map;

import com.louisz.zflow.prcxmlcfg.NodeCfg;

/**
 * @author zhang
 * @description interface for all handlers
 * @time 2018年2月5日
 */
public interface Handler {

	public Object handle(Map<String, String> variablesMap, NodeCfg node) throws Exception;
}
