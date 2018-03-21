package com.louisz.zflow.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.louisz.zflow.constant.Result;
import com.louisz.zflow.entity.ReturnResult;

/**
 * test controller for calling EUREKA client
 * 
 * @author zhang
 * @description
 * @time 2018年3月14日
 */
@Controller
public class ClientTestController {
	private Logger logger = LoggerFactory.getLogger(ClientTestController.class);

	/**
	 * mapping for client calling test
	 * 
	 * @author zhang
	 * @time 2018年3月14日下午3:40:17
	 * @param variableMap
	 * @return
	 */
	@RequestMapping(value = "/clientTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnResult zflowTest(@RequestBody Map<String, String> variableMap) {
		String msg = "Client test is SUCCESS!";
		logger.info(msg + variableMap.toString());
		return new ReturnResult(Result.SUCCESS, msg);
	}
}
