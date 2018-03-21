package com.louisz.zflow.handler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.dao.FlowDao;
import com.louisz.zflow.dao.ProcessDao;
import com.louisz.zflow.entity.FlowEntity;
import com.louisz.zflow.entity.ProcessEntity;
import com.louisz.zflow.prcxmlcfg.FieldCfg;
import com.louisz.zflow.prcxmlcfg.ForkCfg;
import com.louisz.zflow.prcxmlcfg.NodeCfg;
import com.louisz.zflow.prcxmlcfg.ProcessCfg;
import com.louisz.zflow.prcxmlcfg.TransitionCfg;
import com.louisz.zflow.util.AsyncExecutorUtil;
import com.louisz.zflow.util.DateUtil;
import com.louisz.zflow.util.JsonUtil;
import com.louisz.zflow.util.ProcessXmlParseUtil;
import com.louisz.zflow.util.SequenceUtil;
import com.louisz.zflow.util.SpringUtil;

/**
 * @author zhang
 * @description process execution handle
 * @time 2018年2月5日
 */
@Component("flowExecutionHandler")
public class ProcessExecutionHandler extends AbstractHandler implements Handler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Object handle(Map<String, String> variablesMap, NodeCfg nodeCfg) throws Exception {
		Map<String, String> vMap = new HashMap<>();
		vMap.putAll(variablesMap);
		String jobIdPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + "]";

		String processId = "";
		if (null != nodeCfg) {
			ProcessCfg process = (ProcessCfg) nodeCfg;
			processId = process.getId();
			vMap.put(ZflowConstant.FLOW_FATHER, vMap.get(ZflowConstant.FLOW_ID));
			vMap.remove(ZflowConstant.FLOW_ID);
			vMap = putFieldsintoVMap(process.getFields(), vMap);
		} else {
			processId = variablesMap.get(ZflowConstant.PROCESS_ID);
		}

		logger.info(jobIdPattern + "Handling process[processId=" + processId + "] execution...");

		// get process entity
		ProcessEntity processEntity = null;
		try {
			processEntity = getProcessEntity(processId);
		} catch (Exception e) {
			throw e;
		}

		if (null == processEntity) {
			String mString = "There is no process of [processId=" + processId
					+ "] found, this process execution can not be started!";
			logger.error(mString);
			return "[ERROR!]\n" + mString;
		}

		String result = "";
		if (isLooporRepeat(vMap, nodeCfg)) {
			result = doRepeatHandle(vMap, processEntity);
		} else {
			result = doHandle(vMap, processEntity);
		}

		return result;
	}

	/**
	 * method for repeated process execution
	 * 
	 * @author zhang
	 * @time 2018年3月14日下午4:19:06
	 * @param variablesMap
	 * @param processEntity
	 * @return
	 * @throws Exception
	 */
	private String doRepeatHandle(Map<String, String> variablesMap, ProcessEntity processEntity) throws Exception {
		String processId = variablesMap.get(ZflowConstant.PROCESS_ID);
		String jobIdPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + "]";
		ProcessCfg process = processEntity.getProcess();

		List<Map<String, String>> vMaps = getRepetitionMapList(variablesMap, process);
		List<String> results = new LinkedList<>();
		int size = vMaps.size();
		int count = 0;
		String result = "";
		for (Map<String, String> map : vMaps) {
			count++;
			String res = this.doHandle(map, processEntity);
			results.add(res);
			if (isQuit(result, process, map)) {
				return res;
			}
			int interval = Integer.parseInt(map.get(ZflowConstant.REPEAT_TIME_INTERVAL));
			Thread.sleep(interval);
		}
		if (1 >= size) {
			return results.get(0);
		}

		String msg = jobIdPattern + "Process[" + processId + "] has been executed for " + count
				+ " times, result for these executions are:[";
		for (String res : results) {
			msg += res + ";";
			if (ZflowConstant.STATE_FINISH.equals(res)) {
				result = res;
			}
		}
		msg += "].As there is no configuration for executions' quiting policy, the final result will be estimated by default"
				+ "(FINISHED if there is at least one execution succeeded)!So, the final result is [" + result + "]!";
		logger.info(msg);

		return result;
	}

	/**
	 * handle method for each process execution
	 * 
	 * @author zhang
	 * @time 2018年3月13日下午4:56:58
	 * @param variablesMap
	 * @param processEntity
	 * @return
	 * @throws Exception
	 */
	private String doHandle(Map<String, String> variablesMap, ProcessEntity processEntity) throws Exception {

		ProcessCfg process = processEntity.getProcess();
		String processName = process.getName();
		variablesMap.put(ZflowConstant.PROCESS_NAME, processName);
		// initialize the flow entity
		FlowEntity flow = newFlowInit(processEntity, variablesMap);

		String idPattern = "[jobId=" + variablesMap.get(ZflowConstant.JOB_ID) + ", flowId=" + flow.getId() + "]";

		// insert the flow's information into database
		logger.info("Flow[" + flow.toString() + "] started...");
		FlowDao flowDao = (FlowDao) SpringUtil.getBean("flowDao");
		flowDao.insertFlow(flow);

		if (null == process.getStart()) {
			logger.error("Process[" + processName + "] does not contain a valid START node!");
			updateFlow(flowDao, ZflowConstant.TASK_STATE_ERROR, flow);
			return ZflowConstant.TASK_STATE_ERROR;
		}

		String nodeName = process.getStart().getTransition().getTo();
		// find out if the process has a valid start
		if (null == nodeName || 0 == nodeName.length()) {
			logger.error("Process[" + processName + "] does not contain a valid START[start="
					+ process.getStart().toString() + "] service node!");
			updateFlow(flowDao, ZflowConstant.TASK_STATE_ERROR, flow);
			return ZflowConstant.TASK_STATE_ERROR;
		}

		// get the nodes for start execution by its name
		List<NodeCfg> startNodes = ProcessXmlParseUtil.getNextNodes(process, process.getStart().getTransition());
		if (1 != startNodes.size()) {
			logger.error("Process[" + processName + "] does not contain one and only valid node[name=" + nodeName
					+ "] for starting execution according to node START[start=" + process.getStart().toString()
					+ "],process cannot start to execute!");
			updateFlow(flowDao, ZflowConstant.TASK_STATE_ERROR, flow);
			return ZflowConstant.TASK_STATE_ERROR;
		}

		// do the node executions, all the same level nodes can execute asynchronously
		AsyncExecutorUtil asyncExecutorUtil = SpringUtil.getBean("asyncExecutorUtil", AsyncExecutorUtil.class);

		Map<String, NodeCfg> nextNodesMap = new HashMap<>();
		// put the start nodes into the list of upcoming executions to trigger execution
		for (NodeCfg node : startNodes) {
			nextNodesMap.put(node.getName(), node);
		}

		Map<String, Future<String>> futuresMap = new HashMap<>();
		List<TransitionCfg> nextTransList = new LinkedList<>();
		// set a flag for check if the running execution is a fork node or not
		boolean isForkExecution = false;
		boolean isEnd = false;
		while (!isEnd && !nextNodesMap.isEmpty()) {
			// clear the residual data
			nextTransList.clear();
			futuresMap.clear();
			for (String name : nextNodesMap.keySet()) {
				NodeCfg node = nextNodesMap.get(name);

				// if the running flow can reach the final end node, then it must run through
				// all tasks or child processes successfully,so this flow can be ended by
				// reset the "isEnd" flag value to TRUE
				if (ZflowConstant.NODE_TYPE_END.equals(node.getType())) {
					isEnd = true;
				}

				// if the running execution is a fork node one, then get the next running nodes
				// from the fork node
				if (ZflowConstant.NODE_TYPE_FORK.equals(node.getType())) {
					ForkCfg forkCfg = (ForkCfg) node;
					// add the nodes for upcoming execution into the list
					nextTransList.addAll(forkCfg.getTransitions());
					isForkExecution = true;
				}

				try {
					Future<String> future = asyncExecutorUtil.asyncExecutor(node, variablesMap);
					// if the running node is a fork from former FORK node("isForkExecution" being
					// true) and it does not contain a
					// valid transition node, then it is a execution that should run on its own and
					// the state of it should be ignored
					if (!isForkExecution && null != node.getTransition() && 0 < node.getTransition().getTo().length()) {
						futuresMap.put(name, future);
						// get the next node from the executed node into one list
						nextTransList.add(node.getTransition());
					}
				} catch (Exception e) {
					logger.error("Exception happend while flow[name=" + flow.getName() + ", ID=" + flow.getId()
							+ "] execution, this flow execution [" + ZflowConstant.TASK_STATE_ERROR + "]!", e);
					// update the failed status of the flow
					updateFlow(flowDao, ZflowConstant.TASK_STATE_ERROR, flow);
					throw e;
				}
			}

			// check the last execution is success or not, if not, just stop the whole
			// execution by returning a failed state
			boolean b = true;
			for (String name : futuresMap.keySet()) {
				Future<String> future = futuresMap.get(name);
				b = (ZflowConstant.STATE_FINISH.equals(future.get())
						|| ZflowConstant.TASK_STATE_SUCCESS.equals(future.get()));
				if (!b) {
					logger.warn(idPattern + "Node[name=" + name + "] execution is [" + ZflowConstant.STATE_FAILED
							+ "], this flow is [" + ZflowConstant.STATE_FAILED + "]!");
					updateFlow(flowDao, ZflowConstant.STATE_FAILED, flow);
					return ZflowConstant.STATE_FAILED;
				}
			}

			// after fork node's executions are done, reset the "isForkExecution" flag to
			// false
			isForkExecution = false;
			// reset the nodes list for upcoming execution
			nextNodesMap = ProcessXmlParseUtil.getNextNodes(process, nextTransList);
		}

		// if the flow execution ends with not reaching the end node, then it must
		// encounter some error
		if (!isEnd) {
			logger.info("Flow[name=" + flow.getName() + ", ID=" + flow.getId()
					+ "] execution encountered some error, this flow's state is [" + ZflowConstant.TASK_STATE_ERROR
					+ "]!");
			updateFlow(flowDao, ZflowConstant.TASK_STATE_ERROR, flow);
			return ZflowConstant.TASK_STATE_ERROR;
		}

		logger.info(idPattern + "Process[name=" + processName + "] executed successfully, this flow is ["
				+ ZflowConstant.STATE_FINISH + "]!");
		updateFlow(flowDao, ZflowConstant.STATE_FINISH, flow);
		return ZflowConstant.STATE_FINISH;
	}

	/**
	 * initialize a entity for a new running flow
	 * 
	 * @author zhang
	 * @time 2018年2月5日上午11:27:03
	 * @param processEntity
	 * @param variablesMap
	 * @return
	 * @throws Exception
	 */
	private FlowEntity newFlowInit(ProcessEntity processEntity, Map<String, String> vMap) throws Exception {
		if (processEntity.getProcess() == null) {
			logger.error("Can not find valid process model for process[processName=" + processEntity.getName()
					+ ", processId=" + processEntity.getId() + "]!");
			throw new NullPointerException();
		}

		// one parameter map for one running flow
		Map<String, String> variablesMap = new HashMap<>();
		variablesMap.putAll(vMap);

		// put the customized parameters into the variables map
		List<FieldCfg> fields = processEntity.getProcess().getFields();
		if (null != fields) {
			fields.forEach(f -> variablesMap.put(f.getKey(), f.getValue()));
		}

		// parameters initialization for a new running flow
		FlowEntity flowEntity = new FlowEntity();
		String flowId = ZflowConstant.FLOW_UPPER + ZflowConstant.FLOW_SEPEATOR + DateUtil.getDate(0)
				+ ZflowConstant.FLOW_SEPEATOR + SequenceUtil.getSequence();
		vMap.put(ZflowConstant.FLOW_ID, flowId);
		String flowName = variablesMap.get(ZflowConstant.FLOW_NAME) == null ? variablesMap.get(ZflowConstant.FLOW_NAME)
				: variablesMap.get(ZflowConstant.PROCESS_NAME);
		flowName = null != flowName ? flowName : processEntity.getId();
		vMap.put(ZflowConstant.FLOW_NAME, flowName);
		flowEntity.setId(flowId);
		flowEntity.setName(flowName);
		flowEntity.setProcessId(processEntity.getId());
		flowEntity.setProcessName(processEntity.getName());
		flowEntity.setVariables(JsonUtil.toJson(variablesMap));
		flowEntity.setCreateTime(DateUtil.getTime());
		flowEntity.setState(ZflowConstant.STATE_ACTIVE);
		flowEntity.setUpdateTime(DateUtil.getTime());
		// if this flow is a child flow, get the father flow information
		if (vMap.containsKey(ZflowConstant.FLOW_FATHER) && 0 < vMap.get(ZflowConstant.FLOW_FATHER).length()) {
			flowEntity.setFather(vMap.get(ZflowConstant.FLOW_FATHER));
		}

		return flowEntity;
	}

	/**
	 * get process content and parse it into beans from database
	 * 
	 * @author zhang
	 * @time 2018年3月12日下午3:09:00
	 * @param processId
	 * @return
	 * @throws Exception
	 */
	private ProcessEntity getProcessEntity(String processId) throws Exception {
		// get process content and parse it into beans from database
		ProcessEntity processEntity = null;
		ProcessCfg process = null;
		try {
			ProcessDao processDao = (ProcessDao) SpringUtil.getBean("processDao");
			processEntity = processDao.findProcessById(processId);

			if (null == processEntity) {
				return null;
			}

			process = ProcessXmlParseUtil.getProcess(new String(processEntity.getContent()));
			processEntity.setProcess(process);
		} catch (Exception e) {
			logger.error(
					"Exception happened while loading process[processId=" + processId + "],this execution is [Failed]!",
					e);
			throw e;
		}

		return processEntity;
	}

	/**
	 * upate the record of running flow
	 * 
	 * @author zhang
	 * @time 2018年3月16日下午5:18:56
	 * @param flowDao
	 * @param state
	 * @param flow
	 * @throws Exception
	 */
	private void updateFlow(FlowDao flowDao, String state, FlowEntity flow) throws Exception {
		if (ZflowConstant.STATE_FINISH.equals(state)) {
			flow.setFinishTime(DateUtil.getTime());
		}
		flow.setState(state);
		flow.setUpdateTime(DateUtil.getTime());
		flowDao.updateFlowState(flow);
	}

}
