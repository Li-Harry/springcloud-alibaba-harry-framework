package com.neoderm.core.enums;

import com.neoderm.core.constant.SecurityConstants;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

/**
 * 自定义Request header key
 *
 * @author channing
 * @date 2020/3/26 下午2:57
 */
@Getter
public enum RequestHeaderEnum {
	/** 接受的语言 */
	ACCEPT_LANGUAGE(HttpHeaders.ACCEPT_LANGUAGE, "接受的语言"),
	/** 认证头 */
	AUTHORIZATION(HttpHeaders.AUTHORIZATION, "认证头"),
	/** 客户端类型 */
	CLIENT_TYPE("Client-Type", "客户端类型"),
	/** 短信发送的随机数 */
	SESSIONKEY("SessionKey", "短信发送的随机数"),
	/** 客户端类型 */
	WEIXIN_OPENID("Weixin-Openid", "微信openid"),
	/** 页面参数 */
	PAGE_PARAM("Page-Param", "页面参数"),
	/** feign接口日志 */
	FEIGN_APILOG("Feign-Apilog", "feign接口日志"),
	/** 服务调用来源 */
	FROM(SecurityConstants.FROM, "服务调用来源");

	RequestHeaderEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private String code;
	private String name;

}
