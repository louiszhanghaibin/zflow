package com.louisz.zflow.handler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.dao.TaskDao;
import com.louisz.zflow.entity.ReturnResult;
import com.louisz.zflow.entity.TaskEntity;
import com.louisz.zflow.prcxmlcfg.NodeCfg;
import com.louisz.zflow.prcxmlcfg.TaskCfg;
import com.louisz.zflow.util.DateUtil;
import com.louisz.zflow.util.IpPortUtil;
import com.louisz.zflow.util.JsonUtil;
import com.louisz.zflow.util.SequenceUtil;
import com.louisz.zflow.util.SpringUtil;
import com.louisz.zflow.util.StringUtil;

/**
 * @author zhang
 * @description handler for task execution
 * @time 2018年2月6日
 */
public class TaskExecutionHandler extends AbstractHandler implements Handler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * main handling method
	 */
	@Override
	public Object handle(Map<String, String> variablesMap, NodeCfg node) throws Exception {
		Map<String, String> vMap = new HashMap<>();
		vMap.putAll(variablesMap);

		String processIdPattern = "[processId=" + variablesMap.get(ZflowConstant.PROCESS_ID) + "]";
		String taskNamePattern = "[name=" + node.getName() + "]";
		String jobIdPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + "]";

		logger.info(
				jobIdPattern + "Handling task" + taskNamePattern + " execution of process" + processIdPattern + "...");

		String result = "";
		if (isLooporRepeat(vMap, node)) {
			result = doRepeatHandle(vMap, (TaskCfg) node);
		} else {
			result = doHandle(vMap, (TaskCfg) node);
		}

		return result;
	}

	/**
	 * method for repeated task execution
	 * 
	 * @author zhang
	 * @time 2018年3月14日下午4:26:30
	 * @param variablesMap
	 * @param taskCfg
	 * @return
	 * @throws Exception
	 */
	private String doRepeatHandle(Map<String, String> variablesMap, TaskCfg taskCfg) throws Exception {
		String taskNamePattern = "[name=" + taskCfg.getName() + "]";
		String jobIdPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + "]";

		List<Map<String, String>> vMaps = getRepetitionMapList(variablesMap, taskCfg);

		List<String> results = new LinkedList<>();
		int size = vMaps.size();
		int count = 0;
		String result = "";
		for (Map<String, String> map : vMaps) {
			count++;
			String res = this.doHandle(map, taskCfg);
			results.add(res);
			if (isQuit(result, taskCfg, map)) {
				return res;
			}
			int interval = Integer.parseInt(map.get(ZflowConstant.REPEAT_TIME_INTERVAL));
			Thread.sleep(interval);
		}
		if (1 >= size) {
			return results.get(0);
		}

		String msg = jobIdPattern + "Task" + taskNamePattern + " has been executed for " + count
				+ " times, result for these executions are:[";
		for (String res : results) {
			msg += res + ";";
			if (ZflowConstant.STATE_SUCCESS.equals(res)) {
				result = res;
			}
		}
		msg += "].As there is no configuration for executions' quiting policy, the final result will be estimated by default"
				+ "(FINISHED if there is at least one execution succeeded)!So, the final result is [" + result + "]!";
		logger.info(msg);

		return result;
	}

	/**
	 * method for handling task execution
	 * 
	 * @author zhang
	 * @time 2018年3月14日上午10:10:00
	 * @param vMap
	 * @param node
	 * @return
	 * @throws Exception
	 */
	private String doHandle(Map<String, String> vMap, TaskCfg taskCfg) throws Exception {
		String processIdPattern = "[processId=" + vMap.get(ZflowConstant.PROCESS_ID) + "]";
		String taskNamePattern = "[name=" + taskCfg.getName() + "]";
		String jobIdPattern = "[jobId=" + vMap.get(ZflowConstant.JOB_ID) + "]";
		String flowIdPattern = "[flowId=" + vMap.get(ZflowConstant.FLOW_ID) + "]";

		logger.info(jobIdPattern + flowIdPattern + "Start executing TASK[" + taskCfg.toString() + "] in process"
				+ processIdPattern + "...");
		TaskEntity task = initTaskParams(vMap);
		TaskDao taskDao = (TaskDao) SpringUtil.getBean("taskDao");
		taskDao.insertTask(task);

		// send post request to service nodes
		RestTemplate restTemplate = (RestTemplate) SpringUtil.getBean(RestTemplate.class);
		String uri = vMap.get(ZflowConstant.TASK_REFURI);
		ReturnResult result;
		ResponseEntity<ReturnResult> responseEntity;
		try {
			responseEntity = restTemplate.postForEntity(uri, vMap, ReturnResult.class);
			result = responseEntity.getBody();
		} catch (Exception e) {
			String message = "Exception happened while request URL[" + uri + "]!";
			logger.error(message, e);
			updateTaskStateMessage(ZflowConstant.TASK_STATE_ERROR, task, message);
			return ZflowConstant.TASK_STATE_FAILED;
		}

		// get IP and PROT information from response entity
		String ipPort = result.getIp();
		if (StringUtil.isEmpty(ipPort) || "0.0.0.0".equals(ipPort)) {
			ipPort = IpPortUtil.getRemoteIpPortString(responseEntity.getHeaders());
		}
		task.setNodeIp(ipPort);

		// set state and massage for a running task
		String message = result.getDescription();
		// service nodes must response the execution result code back to ZFlow
		String taskCase = (!StringUtil.isEmpty(result.getResult().toString())) ? result.getResult().toString() : "";

		logger.info(jobIdPattern + "[" + taskCase + "]Task" + taskNamePattern + "in process" + processIdPattern
				+ " execution is done![TASK_MASSAGE: " + result.getDescription() + "]");

		switch (taskCase) {
		case ZflowConstant.TASK_STATE_SUCCESS:
			updateTaskStateMessage(ZflowConstant.TASK_STATE_SUCCESS, task, message);
			return ZflowConstant.TASK_STATE_SUCCESS;

		case ZflowConstant.TASK_STATE_FAILED:
			updateTaskStateMessage(ZflowConstant.TASK_STATE_FAILED, task, message);
			return ZflowConstant.TASK_STATE_FAILED;

		case ZflowConstant.TASK_STATE_ERROR:
			updateTaskStateMessage(ZflowConstant.TASK_STATE_ERROR, task, message);
			return ZflowConstant.TASK_STATE_FAILED;

		case ZflowConstant.TASK_STATE_EXCEPTION:
			updateTaskStateMessage(ZflowConstant.TASK_STATE_EXCEPTION, task, message);
			return ZflowConstant.TASK_STATE_FAILED;

		default:
			message = jobIdPattern + "[" + ZflowConstant.TASK_STATE_ERROR + "]Task" + taskNamePattern + "in process"
					+ processIdPattern + " execution encountered exception, service node[url=" + uri
					+ "] did not response correctly![TASK_MASSAGE: " + result.getDescription() + "]";
			updateTaskStateMessage(ZflowConstant.TASK_STATE_ERROR, task, message);
			return ZflowConstant.TASK_STATE_ERROR;
		}
	}

	/**
	 * set state and massage for a running task
	 * 
	 * @author zhang
	 * @time 2018年2月7日下午4:01:06
	 * @param state
	 * @param task
	 * @return
	 */
	private TaskEntity updateTaskStateMessage(String state, TaskEntity task, String msg) {
		String message = msg;
		if (StringUtil.isEmpty(msg)) {
			message = "[" + state + "]Task[" + task.getName()
					+ "] execution is done, please check the relevant logs for more information!";
		}
		task.setMessage(message);
		task.setState(state);
		task.setFinishTime(DateUtil.getTime());
		task.setUpdateTime(DateUtil.getTime());
		// update the status of the finished task
		TaskDao taskDao = (TaskDao) SpringUtil.getBean("taskDao");
		taskDao.updateTask(task);

		return task;
	}

	/**
	 * initialization of task runtime parameters
	 * 
	 * @author zhang
	 * @time 2018年2月6日上午11:29:53
	 * @param taskEntity
	 * @return
	 * @throws Exception
	 */
	private TaskEntity initTaskParams(Map<String, String> vMap) throws Exception {
		TaskEntity taskEntity = new TaskEntity();

		taskEntity.setFlowId(vMap.get(ZflowConstant.FLOW_ID));
		taskEntity.setFlowName(vMap.get(ZflowConstant.FLOW_NAME));
		String taskId = ZflowConstant.TASK_UPPER + ZflowConstant.FLOW_SEPEATOR + DateUtil.getDate(0)
				+ ZflowConstant.FLOW_SEPEATOR + SequenceUtil.getSequence();
		taskEntity.setId(taskId);
		taskEntity.setName(vMap.get(ZflowConstant.TASK_NAME));
		taskEntity.setProcessName(vMap.get(ZflowConstant.PROCESS_NAME));
		taskEntity.setProcessId(vMap.get(ZflowConstant.PROCESS_ID));
		taskEntity.setCreateTime(DateUtil.getTime());
		taskEntity.setVariables(JsonUtil.toJson(vMap));
		taskEntity.setState(ZflowConstant.TASK_STATE_RUNNING);
		String message = "The task[name=" + vMap.get(ZflowConstant.TASK_NAME) + "] is running now...";
		taskEntity.setMessage(message);
		taskEntity.setVariablesMap(vMap);

		return taskEntity;
	}
}
