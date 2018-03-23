package com.louisz.zflow.util;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.louisz.zflow.constant.Result;
import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.entity.ReturnResult;
import com.louisz.zflow.handler.Handler;
import com.louisz.zflow.handler.ProcessExecutionHandler;
import com.louisz.zflow.handler.TaskExecutionHandler;
import com.louisz.zflow.prcxmlcfg.NodeCfg;

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
	public Future<ReturnResult> asyncExecutor(NodeCfg node, Map<String, String> variablesMap) throws Exception {

		ReturnResult result;

		if (ZflowConstant.NODE_TYPE_TASK.equals(node.getType())) {
			// run task execution
			Handler taskHandler = new TaskExecutionHandler();
			try {
				result = taskHandler.handle(variablesMap, node);
			} catch (Exception e) {
				throw e;
			}
		} else if (ZflowConstant.NODE_TYPE_PROCESS.equals(node.getType())) {
			// run process execution
			Handler handler = new ProcessExecutionHandler();
			try {
				result = handler.handle(variablesMap, node);
			} catch (Exception e) {
				throw e;
			}
		} else {
			// if the node if not a type of task or process,then just pass it
			return new AsyncResult<ReturnResult>(new ReturnResult(Result.SUCCESS));
		}

		return new AsyncResult<ReturnResult>(result);

	}

}
