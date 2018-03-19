package com.louisz.zflow.prcxmlcfg;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * entity for node repeat
 * 
 * @author zhang
 * @description
 * @time 2018年3月12日
 */
@XStreamAlias("repeat")
public class RepeatCfg {
	@XStreamAsAttribute
	String parameters = "";
	@XStreamAsAttribute
	int loop = 1;
	@XStreamAsAttribute
	int interval = 0;
	@XStreamAsAttribute
	String quit = "";

	public String getQuit() {
		return quit;
	}

	public void setQuit(String quit) {
		this.quit = quit;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("repeat={").append("parameters=").append(this.parameters);
		sBuilder.append(", loop=").append(this.loop);
		sBuilder.append(", interval=").append(this.interval).append("}");
		return sBuilder.toString();
	}

}
