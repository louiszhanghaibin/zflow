package com.louisz.zflow.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.louisz.zflow.prcxmlcfg.EndCfg;
import com.louisz.zflow.prcxmlcfg.FieldCfg;
import com.louisz.zflow.prcxmlcfg.ForkCfg;
import com.louisz.zflow.prcxmlcfg.JoinCfg;
import com.louisz.zflow.prcxmlcfg.NodeCfg;
import com.louisz.zflow.prcxmlcfg.ProcessCfg;
import com.louisz.zflow.prcxmlcfg.StartCfg;
import com.louisz.zflow.prcxmlcfg.TaskCfg;
import com.louisz.zflow.prcxmlcfg.TransitionCfg;
import com.thoughtworks.xstream.XStream;

/**
 * @author zhang
 * @description utility for parsing process XML
 * @time 2018年2月2日
 */
public class ProcessXmlParseUtil {
	private static Logger logger = LoggerFactory.getLogger(ProcessXmlParseUtil.class);

	/**
	 * get the process file parsed
	 * 
	 * @author zhang
	 * @time 2018年2月2日下午2:27:46
	 * @param processContent
	 * @param processName
	 * @return
	 * @throws Exception
	 */
	public static ProcessCfg getProcess(String processContent) throws Exception {
		ProcessCfg process = new ProcessCfg();

		XStream xs = new XStream();
		XStream.setupDefaultSecurity(xs);
		xs.setMode(XStream.NO_REFERENCES);
		Class<?>[] classes = new Class[] { ProcessCfg.class, TaskCfg.class, FieldCfg.class, ForkCfg.class,
				JoinCfg.class, TransitionCfg.class, StartCfg.class, EndCfg.class };
		xs.allowTypes(classes);
		xs.processAnnotations(classes);
		xs.autodetectAnnotations(true);

		logger.info("starting to parse process XML content...");
		try {
			xs.setClassLoader(ProcessCfg.class.getClassLoader());
			process = (ProcessCfg) xs.fromXML(processContent);
		} catch (Exception e) {
			logger.error("[Failed]Exception happend parsing process XML content!", e);
			throw e;
		}

		logger.info("parsing process[processId=" + process.getId() + "] XML content [succeed]!");
		return process;
	}

	/**
	 * get nodes for next execution
	 * 
	 * @author zhang
	 * @time 2018年2月7日下午3:27:42
	 * @param process
	 * @param nextTransMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, NodeCfg> getNextNodes(ProcessCfg process, List<TransitionCfg> nextTrans)
			throws Exception {
		Map<String, NodeCfg> nextNodesMap = new HashMap<>();

		if (nextTrans.isEmpty()) {
			return nextNodesMap;
		}

		for (TransitionCfg trans : nextTrans) {
			nextNodesMap.put(trans.getTo(), getNextNode(process, trans));
		}

		return nextNodesMap;
	}

	/**
	 * get nodes for next execution
	 * 
	 * @author zhang
	 * @time 2018年2月7日下午3:27:42
	 * @param process
	 * @param transitions
	 * @return
	 * @throws Exception
	 */
	public static NodeCfg getNextNode(ProcessCfg process, TransitionCfg transition) throws Exception {
		String processId = process.getId();
		String nodeName = transition.getTo();

		// get the next node for execution by its name
		List<NodeCfg> nextNodes = getNextNodes(process, transition);
		if (1 != nextNodes.size()) {
			String mString = "Process[ID=" + processId + "] does not contain the ONE and ONLY valid node[name="
					+ nodeName + "] for next execution!";
			logger.error(mString);
			throw new IllegalArgumentException(mString);
		}

		return nextNodes.get(0);
	}

	/**
	 * 
	 * @author zhang
	 * @time 2018年3月15日下午5:07:59
	 * @param process
	 * @param transition
	 * @return
	 * @throws Exception
	 */
	public static List<NodeCfg> getNextNodes(ProcessCfg process, TransitionCfg transition) throws Exception {
		// put all the node into one list for traversal
		List<NodeCfg> nodeCfgs = new ArrayList<>();
		if (null != process.getProcesses()) {
			nodeCfgs.addAll(process.getProcesses());
		}
		if (null != process.getTasks()) {
			nodeCfgs.addAll(process.getTasks());
		}
		nodeCfgs.add(process.getEnd());
		String nodeName = transition.getTo();

		// get the next node for execution by its name
		List<NodeCfg> nextNodes = new ArrayList<>();
		nodeCfgs.forEach(n -> {
			if (n.getName() == nodeName)
				nextNodes.add(n);
		});

		return nextNodes;
	}
}
