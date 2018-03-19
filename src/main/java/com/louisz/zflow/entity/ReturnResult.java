package com.louisz.zflow.entity;

import java.io.Serializable;
import java.util.Map;

import com.louisz.zflow.constant.Result;
import com.louisz.zflow.util.IpUtil;

public class ReturnResult implements Serializable {

	/**
	 * version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 执行结果，状态有三种： 1.成功,Result.SUCCESS 2.失败,Result.FAILED 3.出错,Result.ERROR
	 * 出错主要用在系统配置不正确等特殊情况下，一般情况下，大家使用FAILED即可
	 **/
	private Result result;
	/** 结果描述，执行结果为失败或异常时，需要带具体描述信息 **/
	private String description;
	/**
	 * 返回给框架引擎的变量map 如果你有变量需要传给下一个任务使用，则需要将variableMap
	 * set给ReturnResult返回。这样到下一个task中，可以从map中取出此变量
	 **/
	private Map<String, String> variableMap;

	/** 当前执行的客户端的ip **/
	private String ip;

	/**
	 * 空构造函数，返回此对象时，至少需要设置result属性的值
	 * <p>
	 * result有三种可能值:1.成功,Result.SUCCESS 2.失败,Result.FAILED 3.出错,Result.ERROR
	 * </p>
	 */
	public ReturnResult() {
		this.ip = IpUtil.getIp();
	}

	/**
	 * @param result
	 *            执行结果,有三种可能值：1.成功,Result.SUCCESS 2.失败,Result.FAILED
	 *            3.出错,Result.ERROR
	 * @param description
	 *            结果描述 结果描述
	 */
	public ReturnResult(Result result) {
		this.result = result;
		this.ip = IpUtil.getIp();
	}

	/**
	 * @param result
	 *            执行结果 有三种可能值：1.成功,Result.SUCCESS 2.失败,Result.FAILED
	 *            3.出错,Result.ERROR
	 * @param description
	 *            结果描述 结果描述，执行结果为失败或异常时，需要带具体描述信息
	 */
	public ReturnResult(Result result, String description) {
		this.result = result;
		this.description = description;
		this.ip = IpUtil.getIp();
	}

	/**
	 * @param result
	 *            执行结果
	 * @param description
	 *            结果描述 结果描述，执行结果为失败或异常时，需要带具体描述信息
	 * @param variableMap
	 *            返回给框架引擎的变量map。 如果你有变量需要传给下一个任务使用，则需要将variableMap
	 *            set给ReturnResult。这样到下一个task中，可以从map中取出此变量
	 */
	public ReturnResult(Result result, String description, Map<String, String> variableMap) {
		this.result = result;
		this.description = description;
		this.variableMap = variableMap;
		this.ip = IpUtil.getIp();
	}

	/**
	 * @param result
	 *            执行结果
	 * @param variableMap
	 *            返回给框架引擎的变量map。 如果你有变量需要传给下一个任务使用，则需要将variableMap
	 *            set给ReturnResult。这样到下一个task中，可以从map中取出此变量
	 */
	public ReturnResult(Result result, Map<String, String> variableMap) {
		this.result = result;
		this.variableMap = variableMap;
		this.ip = IpUtil.getIp();
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map<String, String> variableMap) {
		this.variableMap = variableMap;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
