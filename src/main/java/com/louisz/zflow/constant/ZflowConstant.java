package com.louisz.zflow.constant;

/**
 * @author zhang
 * @description constants of project ZFLOW
 * @time 2018年2月2日
 */
public class ZflowConstant {
	public static final String FLAG_YES_LOWER = "yes";
	public static final String FLAG_YES_UPPER = "YES";
	public static final String FLAG_NO_LOWER = "no";
	public static final String FLAG_NO_UPPER = "NO";
	public static final String BOOLEAN_TRUE = "true";
	public static final String BOOLEAN_FALSE = "false";

	public static final String ID_UPPER = "ID";
	public static final String ID_LOWER = "id";
	public static final String JOB_ID = "jobId";

	public static final String CONDITION_UPPER = "CONDITION";
	public static final String CONDITION_REFCLASS = "CONDITION_REFCLASS";
	public static final String CONDITION_EXPRESSION = "CONDITION_EXPRESSION";

	public static final String REPEAT = "REPEAT_PARAMETERS";
	public static final String LOOP = "REPEAT_LOOP";
	public static final String TIME_INTERVAL = "REPEAT_INTERVAL";
	public static final String REPEAT_SYMBOL_REGX = "\\(|\\)|\\s";
	public static final String REPEAT_TIME_INTERVAL = "REPEAT_TIME_INTERVAL";
	public static final String REPEAT_QUIT = "REPEAT_QUIT";
	public static final String REPEAT_QUIT_ON_FAILURE = "onFailure";
	public static final String REPEAT_QUIT_ON_SUCCESS = "onSuccess";

	public static final String PROPERTY_SEPRATOR = "\\|";

	public static final String PROCESS_NAME = "processName";
	public static final String PROCESS_ID = "processId";
	public static final String PROCESS_FILE_PATH = "processFilePath";
	public static final String PROCESS_FILE_CONTENT = "processFileContent";

	public static final String PROCESS_DEPLOY_TYPE = "type";
	public static final String PROCESS_DEPLOY_FORCE = "forceDeploy";
	public static final String PROCESS_DEPLOY_TYPE_PATH = "path";
	public static final String PROCESS_DEPLOY_TYPE_CONTENT = "content";
	/**
	 * 流程定义状态
	 */
	public static final Integer STATE_AVAIABLE = 0;
	public static final Integer STATE_DISABLE = -1;

	public static final String FLOW_UPPER = "FLOW";
	public static final String FLOW_SEPEATOR = "-";
	public static final String FLOW_NAME = "flowName";
	public static final String FLOW_ID = "flowId";
	public static final String FLOW_FATHER = "fatherFlow";
	/**
	 * flow runtime status
	 */
	public static final String STATE_ACTIVE = "ACTIVE";
	public static final String STATE_FAILED = "FAILED";
	public static final String STATE_FAILED_TASK = "FAILED_TASK";
	public static final String STATE_FAILED_FLOW = "FAILED_FLOW";
	public static final String STATE_FINISH = "FINISH";
	public static final String STATE_SUCCESS = "SUCCESS";

	public static final String TASK_UPPER = "TASK";
	public static final String TASK_ID = "taskId";
	public static final String TASK_NAME = "taskName";
	public static final String TASK_REFURI = "refUri";
	public static final String TASK_RUN_RESULT = "TASK_RESULT";
	public static final String TASK_RUN_MESSAGE = "TASK_MESSAGE";

	/**
	 * status for tasks running
	 */
	public static final String TASK_STATE_START = "START";
	public static final String TASK_STATE_RUNNING = "RUNNING";
	public static final String TASK_STATE_EXCEPTION = "EXCEPTION";
	public static final String TASK_STATE_FAILED = "FAILED";
	public static final String TASK_STATE_SUCCESS = "SUCCESS";
	public static final String TASK_STATE_ERROR = "ERROR";

	public static final String NODE_TYPE_PROCESS = "process";
	public static final String NODE_TYPE_TASK = "task";
	public static final String NODE_TYPE_FORK = "fork";
	public static final String NODE_TYPE_JOIN = "join";
	public static final String NODE_TYPE_START = "start";
	public static final String NODE_TYPE_END = "end";

	public static final String REGEX_FOR_LEGAL_LETTER = "^[a-zA-Z0-9_.\\-\\(\\)\u4e00-\u9fa5]+$";
	public static final String REGEX_FOR_SQL = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

}
