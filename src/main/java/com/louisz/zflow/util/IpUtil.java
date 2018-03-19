package com.louisz.zflow.util;

import java.net.InetAddress;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * utility for obtaining IP address and define the IP property into spring
 * container
 * 
 * @author zhang
 * @description
 * @time 2018年3月1日
 */
public class IpUtil extends PropertyDefinerBase {

	@Override
	public String getPropertyValue() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "0.0.0.0";
		}

	}

	/**
	 * get host IP address
	 * 
	 * @author zhang
	 * @time 2018年3月14日下午3:12:27
	 * @return
	 */
	public static String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "0.0.0.0";
		}
	}

}
