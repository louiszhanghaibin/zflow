package com.louisz.zflow.handler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.dao.ProcessDao;
import com.louisz.zflow.entity.ProcessEntity;
import com.louisz.zflow.prcxmlcfg.NodeCfg;
import com.louisz.zflow.prcxmlcfg.ProcessCfg;
import com.louisz.zflow.util.DateUtil;
import com.louisz.zflow.util.ProcessXmlParseUtil;
import com.louisz.zflow.util.SpringUtil;

/**
 * class for handling process deployment
 * 
 * @author zhang
 * @description
 * @time 2018年3月7日
 */
public class ProcessDeloyHandler implements Handler {
	Logger logger = LoggerFactory.getLogger(ProcessDeloyHandler.class);

	@Override
	public Object handle(Map<String, String> variablesMap, NodeCfg node) throws Exception {
		String jobIdPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + "]";

		logger.info(jobIdPattern + "Handling process deployment...");

		// form a new process entity for MYBATIS to insert a record of the process
		ProcessEntity processEntity;
		try {
			String content = getProcessContent(variablesMap);
			processEntity = getProcessEntity(content);
		} catch (Exception e) {
			throw e;
		}

		// insert the record of the process into data base
		String processId = processEntity.getId();
		try {
			ProcessDao processDao = SpringUtil.getBean(ProcessDao.class);
			if (null != processDao.findProcessById(processId)) {
				String result = "The process ID[" + processId
						+ "] is already in use, please check if the process is deployed or the processId of the pending process is repeated ";
				logger.warn(result);
				return result;
			}
			processDao.insertProcess(processEntity);
		} catch (Exception e) {
			String msg = jobIdPattern + "Exception happened while storing process[processId=" + processId
					+ "] into data base,process deployment [Failed]!";
			logger.error(msg, e);
			throw e;
		}

		logger.info("[" + ZflowConstant.STATE_SUCCESS + "] for deploying process[processId=" + processId
				+ "] into data base!");
		return ZflowConstant.STATE_SUCCESS;
	}

	/**
	 * get a new process entity from a process file
	 * 
	 * @author zhang
	 * @time 2018年3月7日下午4:33:32
	 * @param content
	 * @return
	 * @throws Exception
	 */
	private ProcessEntity getProcessEntity(String content) throws Exception {
		ProcessEntity processEntity = null;
		ProcessCfg processCfg = new ProcessCfg();
		try {
			processCfg = ProcessXmlParseUtil.getProcess(content);
			processEntity = new ProcessEntity();
			processEntity.setContent(content);
			processEntity.setId(processCfg.getId());
			processEntity.setName(processCfg.getName());
			processEntity.setState(ZflowConstant.STATE_AVAIABLE);
			processEntity.setCreateTime(DateUtil.getTime());
			processEntity.setUpdateTime(DateUtil.getTime());
			processEntity.setProcess(processCfg);
		} catch (Exception e) {
			throw e;
		}

		return processEntity;
	}

	/**
	 * get the process content
	 * 
	 * @author zhang
	 * @time 2018年3月13日上午11:15:04
	 * @param variablesMap
	 * @return
	 * @throws Exception
	 */
	private String getProcessContent(Map<String, String> variablesMap) throws Exception {
		String jobIdPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + "]";
		String type = variablesMap.get(ZflowConstant.PROCESS_DEPLOY_TYPE);
		String pathPattern = "";
		String content = null;
		// if the process content comes from a process file
		if (ZflowConstant.PROCESS_DEPLOY_TYPE_PATH.equals(type)) {
			String path = variablesMap.get(ZflowConstant.PROCESS_FILE_PATH);
			pathPattern = " File[" + path + "] ";
			try {
				InputStream inputStream = new FileInputStream(path);
				content = IOUtils.toString(inputStream);
				inputStream.close();
			} catch (Exception e) {
				String msg = jobIdPattern + "Excption happened while reading" + pathPattern
						+ ", deploying process [Failed]!";
				logger.error(msg, e);
				throw e;
			}
		} else {
			// else the content must is transfered by request
			content = variablesMap.get(ZflowConstant.PROCESS_FILE_CONTENT);
		}
		if (null == content || 0 == content.length()) {
			String result = jobIdPattern + "Process" + pathPattern
					+ "content is empty,there must be valid content for deploying a process, this process deployment is [Failed]!";
			logger.warn(result);
			throw new NullArgumentException(result);
		}

		return getTreatedContent(content);
	}

	/**
	 * get process content string trim and remove those useless characters in the
	 * string
	 * 
	 * @author zhang
	 * @time 2018年3月8日下午5:13:23
	 * @param content
	 * @return
	 * @throws Exception
	 */
	private String getTreatedContent(String content) throws Exception {
		String result = "";
		result = content.trim();
		result.replaceAll("\r", "");

		return result;
	}

}
