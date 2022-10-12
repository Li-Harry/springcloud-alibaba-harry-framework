package com.neoderm.core.utils;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * request请求信息
 *
 * @author channing
 * @date 2020/3/26 下午2:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RequestInfo {
	private String ip;
	private String contextPath;
	private String host;
	private String referer;
	private String returnUri;
	private String returnUrl;
	private String returnParam;
	private String agent;
	private Object sessionUser;
	private String proto;
	private String port;

	/**
	 * 获取没有代理的ip，有代理的ip为client, proxy1, proxy2,比如ip = "58.33.99.206 ,
	 * 101.226.125.121, 101.226.125.121"，newIp=58.33.99.206
	 *
	 * @return
	 */
	public String getIthoutProxy() {
		String newIp = (StrUtil.isBlank(ip)) ? ip : (ip.replaceAll(",.*", "").trim());
		return newIp;
	}
}
