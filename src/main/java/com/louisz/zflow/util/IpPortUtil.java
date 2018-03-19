package com.louisz.zflow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * @author zhang
 * @description utility for getting the client's IP and PORT
 * @time 2018年2月6日
 */
public class IpPortUtil {
	private static Logger logger = LoggerFactory.getLogger(IpPortUtil.class);

	public static String getRemoteIpPortString(HttpHeaders headers) throws Exception {
		String remoteAddr;
		try {
			remoteAddr = headers.getFirst("X-Forwarded-For");
			// 如果通过多级反向代理，X-Forwarded-For的值不止一个，而是一串用逗号分隔的IP值，此时取X-Forwarded-For中第一个非unknown的有效IP字符串
			if (isEffective(remoteAddr) && (remoteAddr.indexOf(",") > -1)) {
				String[] array = remoteAddr.split(",");
				for (String element : array) {
					if (isEffective(element)) {
						remoteAddr = element;
						break;
					}
				}
			}
			if (!isEffective(remoteAddr)) {
				remoteAddr = headers.getFirst("X-Real-IP");
			}
			if (!isEffective(remoteAddr)) {
				remoteAddr = headers.getLocation().toString();
			}
		} catch (Exception e) {
			logger.error("get romote ip error,error message:" + e);
			throw e;
		}
		// get the client's server port
		String port = Long.toString(getRemotePort(headers));

		return remoteAddr + port;
	}

	/**
	 * 获取客户端源端口
	 * 
	 * @param request
	 * @return
	 */
	public static Long getRemotePort(HttpHeaders headers) throws Exception {
		try {
			String port = headers.getFirst("remote-port");
			if (StringUtil.isNotEmpty(port)) {
				try {
					return Long.parseLong(port);
				} catch (NumberFormatException ex) {
					logger.error("convert port to long error , port:	" + port);
					throw ex;
				}
			} else {
				return 0l;
			}
		} catch (Exception e) {
			logger.error("get romote port error,error message:" + e.getMessage());
			throw e;
		}
	}

	/**
	 * 远程地址是否有效.
	 * 
	 * @param remoteAddr
	 *            远程地址
	 * @return true代表远程地址有效，false代表远程地址无效
	 */
	private static boolean isEffective(final String remoteAddr) {
		boolean isEffective = false;
		if ((null != remoteAddr) && (!"".equals(remoteAddr.trim()))
				&& (!"unknown".equalsIgnoreCase(remoteAddr.trim()))) {
			isEffective = true;
		}
		return isEffective;
	}

}
