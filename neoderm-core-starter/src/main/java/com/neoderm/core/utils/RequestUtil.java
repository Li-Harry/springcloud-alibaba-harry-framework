package com.neoderm.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.neoderm.core.enums.RequestAttrEnum;
import com.neoderm.core.enums.RequestHeaderEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * 工具类：request
 *
 * @author channing
 * @date 2020/3/26 下午2:57
 */
public class RequestUtil {
	private static final Pattern PATTERN_SESSIONID = Pattern.compile(";jsessionid=[0-9a-zA-Z]*",
			Pattern.CASE_INSENSITIVE);
	private static final String BEARER_TYPE = "Bearer";

	/**
	 * 获取request
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		if (RequestContextHolder.getRequestAttributes() != null) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			return request;
		}
		return null;
	}

	/**
	 * requestInfo
	 *
	 * @param request
	 * @return
	 */
	public static RequestInfo getRequestInfo(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String returnUrl = request.getRequestURL().toString();
		if (StrUtil.isNotBlank(request.getQueryString())) {
			returnUrl = returnUrl + "?" + request.getQueryString();
		}
		String ip = ServletUtil.getClientIP(request);
		String host = request.getHeader("X-Host");
		String referer = request.getHeader("X-Http-Referer");
		String proto = request.getHeader("X-Forwarded-Proto");
		String port = request.getHeader("X-Forwarded-Port");
		// 有代理
		if (ip == null) {
			ip = request.getRemoteAddr();
			host = request.getServerName();
			referer = request.getHeader("referer");
			proto = request.getScheme();
			port = request.getServerPort() + "";
		}
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setPort(port);
		requestInfo.setProto(proto);
		requestInfo.setReturnUri(request.getRequestURI());
		requestInfo.setReturnUrl(returnUrl);
		requestInfo.setIp(ip);
		requestInfo.setHost(host);
		requestInfo.setReferer(referer);
		requestInfo.setContextPath(request.getContextPath());
		requestInfo.setAgent(request.getHeader(HttpHeaders.USER_AGENT));
		return requestInfo;
	}

	/**
	 * 计算接口url
	 *
	 * @param request
	 * @return [path, action]
	 */
	public static String[] calApiUrl(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		// 计算requestUri
		requestUri = PATTERN_SESSIONID.matcher(requestUri).replaceAll("");
		requestUri = requestUri.replaceFirst(request.getContextPath(), "");
		if (!requestUri.equals("") && !requestUri.equals("/")) {
			requestUri = requestUri.replaceAll("/+$", "");
		}
		// 这种接口url，只有前面部分为接口地址：/apifront/weixincallback/msg/{appid}
		String[] sp = requestUri.split("\\/");
		String path = sp[0] + "/" + sp[1] + "/" + sp[2];
		String action = sp[2];
		return new String[] { path, action };
	}

	/**
	 * 计算接口完整url
	 *
	 * @param request
	 */
	public static String calApiUrlWhole(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return requestUri.endsWith("/") ? requestUri.substring(0, requestUri.length() - 1) : requestUri;
	}

	/**
	 * 获取Request的header - token
	 */
	public static String getHeader_token(HttpServletRequest request) {
		if (request != null) {
			String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (StrUtil.isNotBlank(authorization)) {
				return authorization.replace(BEARER_TYPE, StrUtil.EMPTY).trim();
			}
		}
		return null;
	}

	/**
	 * 获取Request的header
	 */
	public static String getHeader(HttpServletRequest request, RequestHeaderEnum key) {
		return (request == null) ? null : request.getHeader(key.getCode());
	}

	/**
	 * 获取Request的header
	 */
	public static String getHeader(RequestHeaderEnum key) {
		HttpServletRequest request = getRequest();
		return (request == null) ? null : request.getHeader(key.getCode());
	}

	/**
	 * 设置Request的attribute
	 */
	public static void setAttr(HttpServletRequest request, RequestAttrEnum key, Object obj) {
		if (request != null && obj != null) {
			request.setAttribute(key.getCode(), obj);
		}
	}

	/**
	 * 设置Request的attribute
	 */
	public static void setAttr(RequestAttrEnum key, Object obj) {
		setAttr(getRequest(), key, obj);
	}

	/**
	 * 获取Request的attribute
	 */
	public static <T> T getAttr(HttpServletRequest request, RequestAttrEnum key) {
		return request == null ? null : (T) request.getAttribute(key.getCode());
	}

	/**
	 * 获取Request的attribute
	 */
	public static <T> T getAttr(RequestAttrEnum key) {
		return getAttr(getRequest(), key);
	}
}
