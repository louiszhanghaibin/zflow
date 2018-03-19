package com.louisz.zflow.prcxmlcfg;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 自定义参数节点
 * 
 * @author zhang
 * @description
 * @time 2018年2月2日
 */
@XStreamAlias("field")
public class FieldCfg {
	@XStreamAsAttribute
	private String key = "";
	@XStreamAsAttribute
	private String value = "";

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		String result = "";

		result = this.key + "=" + this.value;

		return result;
	}

}
