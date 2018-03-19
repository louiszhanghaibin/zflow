package com.louisz.zflow.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.handler.ProcessDeloyHandler;
import com.louisz.zflow.util.SequenceUtil;

public class DeployProcessService {
	Logger logger = LoggerFactory.getLogger(DeployProcessService.class);

	public String doService(Map<String, String> variablesMap) throws Exception {
		String result = "";
		String jobId = "PROCESS-DEPLOYMENT-JOB-" + Integer.toString(SequenceUtil.getSequence());
		variablesMap.put(ZflowConstant.JOB_ID, jobId);

		logger.info("[jobId=" + jobId + "]Start to deploy a process, input parameters are" + variablesMap.toString()
				+ "...");

		try {
			ProcessDeloyHandler handler = new ProcessDeloyHandler();
			result = (String) handler.handle(variablesMap, null);
		} catch (Exception e) {
			String msg = "[jobId=" + jobId
					+ "]Exception happened while deploying a process, process deployment service is [Failed]!";
			logger.error(msg, e);
			throw e;
		}

		return result;
	}

}
