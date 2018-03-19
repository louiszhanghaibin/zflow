package com.louisz.zflow.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.louisz.zflow.constant.ZflowConstant;
import com.louisz.zflow.prcxmlcfg.NodeCfg;
import com.louisz.zflow.prcxmlcfg.RepeatCfg;
import com.louisz.zflow.util.StringUtil;

/**
 * abstract class for handler implements
 * 
 * @author zhang
 * @description
 * @time 2018年3月14日
 */
public abstract class AbstractHandler {
	Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

	/**
	 * if the node execution is for loop or repeated running
	 * 
	 * @author zhang
	 * @time 2018年3月14日下午4:12:50
	 * @param vMap
	 * @param node
	 * @return
	 * @throws Exception
	 */
	protected boolean isLooporRepeat(Map<String, String> vMap, NodeCfg node) throws Exception {
		boolean b = false;
		b = (!StringUtil.isEmpty(vMap.get(ZflowConstant.LOOP)) || !StringUtil.isEmpty(vMap.get(ZflowConstant.REPEAT)))
				|| (null != node && null != node.getRepeat()
						&& (!StringUtil.isEmpty(node.getRepeat().getParameters()) || 1 < node.getRepeat().getLoop()));
		return b;
	}

	/**
	 * get the list of parameter maps for repeated running or loop
	 * 
	 * @author zhang
	 * @time 2018年3月13日上午10:24:19
	 * @param vMap
	 * @param node
	 * @return
	 * @throws Exception
	 */
	protected List<Map<String, String>> getRepetitionMapList(Map<String, String> vMap, NodeCfg node) throws Exception {
		List<Map<String, String>> maps = new ArrayList<>();
		int times = 1;
		String paramString = "";
		String repeatMsg = "";
		String interval = "0";

		if (!StringUtil.isEmpty(vMap.get(ZflowConstant.LOOP)) || !StringUtil.isEmpty(vMap.get(ZflowConstant.REPEAT))) {
			// node is configured for repeated running with different parameters each time
			interval = StringUtil.isEmpty(vMap.get(ZflowConstant.TIME_INTERVAL)) ? "0"
					: vMap.get(ZflowConstant.TIME_INTERVAL);
			if (!StringUtil.isNumeric(interval)) {
				String mString = "ERROR!The argument[" + interval
						+ "] of interval configuration in the map of parameters is illegal!";
				logger.error(mString);
				throw new IllegalArgumentException(mString);
			}
			vMap.remove(ZflowConstant.TIME_INTERVAL);

			times = StringUtil.isEmpty(vMap.get(ZflowConstant.LOOP)) ? 1
					: Integer.parseInt(vMap.get(ZflowConstant.LOOP));
			vMap.remove(ZflowConstant.LOOP);

			paramString = StringUtil.isEmpty(vMap.get(ZflowConstant.REPEAT)) ? "" : vMap.get(ZflowConstant.REPEAT);
			vMap.remove(ZflowConstant.REPEAT);

			repeatMsg = "repeat={loop=" + times + ", parameters=" + paramString + ", interval=" + interval + "ms}";
		} else if (null != node && null != node.getRepeat()
				&& (!StringUtil.isEmpty(node.getRepeat().getParameters()) || 1 < node.getRepeat().getLoop())) {
			// node is configured for loop running
			RepeatCfg repeatCfg = node.getRepeat();
			interval = Integer.toString(repeatCfg.getInterval());
			times = repeatCfg.getLoop();
			paramString = StringUtil.isEmpty(repeatCfg.getParameters()) ? "" : repeatCfg.getParameters();
			repeatMsg = repeatCfg.toString();
		} else {
			Map<String, String> vaMap = new HashMap<>();
			vaMap.putAll(vMap);
			maps.add(vaMap);
			return maps;
		}

		while (times > 0) {
			Map<String, String> vaMap = new HashMap<>();
			vaMap.put(ZflowConstant.REPEAT_TIME_INTERVAL, interval);
			vaMap.putAll(vMap);
			maps.add(vaMap);
			times--;
		}
		if (0 < maps.size()) {
			return maps;
		}

		String[] params = paramString.split(ZflowConstant.PROPERTY_SEPRATOR);
		for (String param : params) {
			String[] pair = param.replaceAll(ZflowConstant.REPEAT_SYMBOL_REGX, "").split("=");
			if (2 != pair.length) {
				logger.error("Error! The parameters of repeat node[" + repeatMsg + "] in node[name=" + node.getName()
						+ "] is configured incorrectly!");
				throw new Exception();
			}
			Map<String, String> vaMap = new HashMap<>();
			vaMap.put(pair[0], pair[1]);
			maps.add(vaMap);
		}

		return maps;
	}

	/**
	 * check if the repeated executions need to break
	 * 
	 * @author zhang
	 * @time 2018年3月13日下午5:14:09
	 * @param result
	 * @param process
	 * @return
	 * @throws Exception
	 */
	protected boolean isQuit(String result, NodeCfg node, Map<String, String> vMap) throws Exception {
		boolean b = false;
		if (StringUtil.isEmpty(vMap.get(ZflowConstant.REPEAT_QUIT))
				&& (null == node.getRepeat() || StringUtil.isEmpty(node.getRepeat().getQuit()))) {
			return false;
		}
		// validate the argument
		String quit = StringUtil.isEmpty(ZflowConstant.REPEAT_QUIT) ? node.getRepeat().getQuit()
				: vMap.get(ZflowConstant.REPEAT_QUIT);
		if (!ZflowConstant.REPEAT_QUIT_ON_FAILURE.equals(quit) && ZflowConstant.REPEAT_QUIT_ON_SUCCESS.equals(quit)) {
			logger.error("Node[name=" + node.getName() + "] configuration is wrong, illegal argument of[quit=" + quit
					+ "] in node REPEAT[" + node.getRepeat().toString() + "!");
			throw new IllegalArgumentException();
		}

		if (ZflowConstant.REPEAT_QUIT_ON_FAILURE.equals(quit)
				&& !(ZflowConstant.STATE_FINISH.equals(result) || ZflowConstant.STATE_SUCCESS.equals(result))) {
			return true;
		} else if (ZflowConstant.REPEAT_QUIT_ON_SUCCESS.equals(quit)
				&& (ZflowConstant.STATE_FINISH.equals(result) || ZflowConstant.STATE_SUCCESS.equals(result))) {
			return true;
		}

		return b;
	}
}
