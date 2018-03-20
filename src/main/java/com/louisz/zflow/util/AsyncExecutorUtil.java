package com.louisz.zflow.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.handler.Handler;
import com.louisz.zflow.handler.ProcessExecutionHandler;
import com.louisz.zflow.handler.TaskExecutionHandler;
import com.louisz.zflow.prcxmlcfg.FieldCfg;
import com.louisz.zflow.prcxmlcfg.NodeCfg;
import com.louisz.zflow.prcxmlcfg.ProcessCfg;
import com.louisz.zflow.prcxmlcfg.TaskCfg;

/**
 * @author zhang
 * @description utility for execution threads
 * @time 2018年2月7日
 */
@Component("asyncExecutorUtil")
public class AsyncExecutorUtil {
	/**
	 * according to different type of node, handle differently by using ASYNC on
	 * 
	 * @author zhang
	 * @time 2018年2月7日下午5:00:33
	 * @param node
	 * @param variablesMap
	 * @return
	 * @throws Exception
	 */
	@Async
	public Future<String> asyncExecutor(NodeCfg node, Map<String, String> variablesMap) throws Exception {

		String result = "";

		if (ZflowConstant.NODE_TYPE_TASK.equals(node.getType())) {
			// run task execution
			TaskCfg taskCfg = (TaskCfg) node;
			Map<String, String> map = new HashMap<>();
			map.putAll(variablesMap);
			map.put(ZflowConstant.TASK_NAME, taskCfg.getName());
			map.put(ZflowConstant.TASK_REFURI, taskCfg.getRefUri());
			Handler taskHandler = new TaskExecutionHandler();
			try {
				map = putFieldsintoVMap(taskCfg.getFields(), map);
				result = (String) taskHandler.handle(map, taskCfg);
			} catch (Exception e) {
				throw e;
			}
		} else if (ZflowConstant.NODE_TYPE_PROCESS.equals(node.getType())) {
			// run process execution
			ProcessCfg process = (ProcessCfg) node;
			Map<String, String> map = new HashMap<>();
			map.putAll(variablesMap);
			map.put(ZflowConstant.PROCESS_ID, process.getId());
			if (map.containsKey(ZflowConstant.FLOW_ID)) {
				map.remove(ZflowConstant.FLOW_ID);
			}
			Handler handler = new ProcessExecutionHandler();
			try {
				map = putFieldsintoVMap(process.getFields(), map);
				result = (String) handler.handle(map, process);
			} catch (Exception e) {
				throw e;
			}
		} else {
			// if the node if not a type of task or process,then just pass it
			return new AsyncResult<String>(ZflowConstant.STATE_SUCCESS);
		}

		return new AsyncResult<String>(result);

	}

	/**
	 * put the custom parameters into the variables map
	 * 
	 * @author zhang
	 * @time 2018年3月12日下午3:20:43
	 * @param fields
	 * @param VMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> putFieldsintoVMap(List<FieldCfg> fields, Map<String, String> vMap) throws Exception {
		if (null == fields || 0 == fields.size()) {
			return vMap;
		}

		for (FieldCfg fieldCfg : fields) {
			vMap.put(fieldCfg.getKey(), fieldCfg.getValue());
		}

		return vMap;
	}

}
