package com.louisz.zflow.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.entity.ReturnResult;
import com.louisz.zflow.handler.ProcessExecutionHandler;
import com.louisz.zflow.util.SequenceUtil;

/**
 * @author zhang
 * @description service for starting flows
 * @time 2018年2月2日
 */
@Component("startProcessService")
public class StartProcessService {
	private Logger logger = LoggerFactory.getLogger(StartProcessService.class);

	public ReturnResult doService(Map<String, String> variablesMap) throws Exception {
		String jobId = "PROCESS-EXECUTION-JOB-" + Integer.toString(SequenceUtil.getSequence());
		variablesMap.put(ZflowConstant.JOB_ID, jobId);

		logger.info("[jobId=" + jobId + "]Start service of executing process[processId="
				+ variablesMap.get(ZflowConstant.PROCESS_ID) + "],input parameters are" + variablesMap.toString()
				+ "...");
		ProcessExecutionHandler flowExecutionHandler = new ProcessExecutionHandler();
		ReturnResult result = null;
		try {
			result = flowExecutionHandler.handle(variablesMap, null);
		} catch (Exception e) {
			String msg = "[jobId=" + jobId + "]Exception happened while launching a flow of process[processId="
					+ variablesMap.get(ZflowConstant.PROCESS_ID) + "], this process execution is [Failed]!";
			logger.error(msg, e);
			throw e;
		}
		return result;
	}

}
