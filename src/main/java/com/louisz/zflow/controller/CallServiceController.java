package com.louisz.zflow.controller;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.service.DeployProcessService;
import com.louisz.zflow.service.StartProcessService;
import com.louisz.zflow.util.JsonUtil;

/**
 * controller for launching process service
 * 
 * @author zhang
 * @description
 * @time 2018年2月26日
 */
@Controller
public class CallServiceController {
	@Resource
	StartProcessService service;

	Logger logger = LoggerFactory.getLogger(CallServiceController.class);

	/**
	 * transfer for index page
	 * 
	 * @author zhang
	 * @time 2018年3月1日上午10:56:59
	 * @return
	 */
	@RequestMapping(value = "/zflow", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	/**
	 * method for starting a new flow form a process
	 * 
	 * @author zhang
	 * @time 2018年2月27日上午10:19:32
	 * @param variablesMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/startProcess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String startProcess(@RequestBody Map<String, String> variablesMap) throws Exception {

		String result = "Received a request for launching a flow of process[processId="
				+ variablesMap.get(ZflowConstant.PROCESS_ID) + "], launching a new flow now...";
		logger.info(result);

		logger.info("validating the legality of the input parameters from request for launching process[processId="
				+ variablesMap.get(ZflowConstant.PROCESS_ID) + "]...");
		if (!validateLegalityOfVariables(variablesMap)) {
			result = "Some parameters did not pass the validation of legality before starting a flow of process[processId="
					+ variablesMap.get(ZflowConstant.PROCESS_ID)
					+ "], please make sure there is no illegal parameter along with the request, such as special characters, SQL words and etc.";
			logger.error(result);
			return JsonUtil.toJson("ERROR\n" + result);
		}
		logger.info("validation passed for launching process[processId=" + variablesMap.get(ZflowConstant.PROCESS_ID)
				+ "]!");

		String state = "";
		try {
			state = service.doService(variablesMap);
		} catch (Exception e) {
			result = "Exception happened while launching a flow of process[processId="
					+ variablesMap.get(ZflowConstant.PROCESS_ID) + "], this process execution is [Failed]!";
			return JsonUtil.toJson("ERROR\n" + result + "\n{" + e.getMessage() + "}");
		}

		result = state + "\nResult has already come out for execution service of process[processId="
				+ variablesMap.get(ZflowConstant.PROCESS_ID)
				+ "], please check those relevant log files or data tables for more information.";

		return JsonUtil.toJson(result);
	}

	@RequestMapping(value = "/deployProcess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deployProcess(@RequestBody Map<String, String> variablesMap) {
		String type = variablesMap.get(ZflowConstant.PROCESS_DEPLOY_TYPE);
		String result = "";
		String pathPattern = "";
		if (ZflowConstant.PROCESS_DEPLOY_TYPE_PATH.equals(type)) {
			String path = variablesMap.get(ZflowConstant.PROCESS_FILE_PATH);
			logger.info("Received a request for deploying a process in path[" + path + "]...");

			if (!validateLegalityOfVariables(variablesMap)) {
				result = "Some parameters did not pass the validation of legality before starting a flow of process[processId="
						+ variablesMap.get(ZflowConstant.PROCESS_ID)
						+ "], please make sure there is no illegal parameter along with the request, such as special characters, SQL words and etc.";
				logger.error(result);
				return JsonUtil.toJson("ERROR\n" + result);
			}

			File file = new File(path);
			if (!file.exists()) {
				result = "File dose not exist in path[" + path + "]!";
				logger.warn(result);
				return JsonUtil.toJson("ERROR\n" + result);
			}
			pathPattern = "[filePath=" + path + "]";
		} else {
			logger.info("Received a request for deploying a process of the input content...");
		}

		String state = "";
		try {
			DeployProcessService deployProcessService = new DeployProcessService();
			state = deployProcessService.doService(variablesMap);
		} catch (Exception e) {
			result = "Exception happened while deploying process" + pathPattern
					+ ", this process deployment is [Failed]!";
			return JsonUtil.toJson("ERROR\n" + result + "\n{" + e.getMessage() + "}");
		}

		result = state + "\nResult has already come out for deployment servive of process" + pathPattern
				+ ", please check relevant log files or data tables for more information.";

		return JsonUtil.toJson(result);
	}

	/**
	 * validate the legality of the input parameters
	 * 
	 * @author zhang
	 * @time 2018年2月26日下午3:16:49
	 * @param variablesMap
	 * @return
	 */
	private boolean validateLegalityOfVariables(Map<String, String> variablesMap) {
		Boolean b = true;
		for (String key : variablesMap.keySet()) {
			b = variablesMap.get(key).matches(ZflowConstant.REGEX_FOR_LEGAL_LETTER);
			b = b == false ? false : (!variablesMap.get(key).matches(ZflowConstant.REGEX_FOR_SQL));
		}

		return b;
	}

}
