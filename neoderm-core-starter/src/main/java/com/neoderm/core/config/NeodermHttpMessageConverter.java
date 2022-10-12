package com.neoderm.core.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoderm.core.enums.RequestAttrEnum;
import com.neoderm.core.utils.JsonUtil;
import com.neoderm.core.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * controller类，请求和返回json转换
 */
@Slf4j
@Component
public class NeodermHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * xml字符串的开始
	 */
	private static final String XML_PRE = "<";

	@Override
	protected boolean supports(Class<?> clazz) {
		return false;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
	}

	/**
	 * 只有当这个方法的返回true，才会执行readInternal
	 */
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	/**
	 * 只有当这个方法的返回true，才会执行writeInternal
	 */
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return true;
	}

	/**
	 * 只有canRead方法返回true，才会执行
	 */
	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = inputMessage.getBody().read()) != -1) {
			baos.write(i);
		}
		String bodyString = baos.toString("utf-8");
		RequestUtil.setAttr(RequestAttrEnum.bodyString, bodyString);
		Object requestBodyObject = null;
		if (StrUtil.isNotBlank(bodyString)) {
			bodyString = bodyString.trim();
			if (bodyString.indexOf(XML_PRE) != 0) {
				// 非xml，按照json处理
				try {
					requestBodyObject = JsonUtil.jsonStringToBeanThrowException(objectMapper, bodyString, clazz);
				} catch (Exception e) {
					log.error("readInternal,body解析失败：{}", bodyString);
				}
			}
		}
		if (requestBodyObject == null) {
			requestBodyObject = JsonUtil.jsonStringToBean(objectMapper, "{}", clazz);
		}
		return requestBodyObject;
	}

	/**
	 * 只有canWrite方法返回true，才会执行
	 */
	@Override
	protected void writeInternal(Object t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String result = "";
		if (t != null) {
			if (t.getClass().equals(String.class)) {
				result = t.toString();
			} else {
				result = JsonUtil.beanToJsonString(objectMapper, t);
			}
		}
		outputMessage.getBody().write(result.getBytes("utf-8"));
	}
}
