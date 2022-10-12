package com.neoderm.core.enums;

import lombok.Getter;

/**
 * 自定义Request attribute key
 *
 * @author channing
 * @date 2020/3/26 下午2:57
 */
@Getter
public enum RequestAttrEnum {

	/** 令牌 */
	token("令牌"),
	/** post请求对象 */
	bodyObject("post请求对象"),
	/** post请求文本 */
	bodyString("post请求文本"),
	/** feign接口日志 */
	feignApiLog("feign接口日志");

	RequestAttrEnum(String name) {
		this.code = this.name();
		this.name = name;
	}

	private String code;
	private String name;

}
