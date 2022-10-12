package com.neoderm.core.exception;

import com.neoderm.core.enums.BusinessCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;

/**
 * 微服务异常
 */
@Slf4j
@Configuration
@Primary
public class ServiceErrorAttributes extends DefaultErrorAttributes {

    /**
     * 让后端抛出的异常，以固定格式返回
     * @param webRequest
     * @param includeStackTrace
     * @return
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        Throwable error = this.getError(webRequest);
        if (error instanceof BusinessException) {
            log.error("服务异常", error);
            BusinessException businessException = (BusinessException) error;
            errorAttributes.put("respCode", businessException.getRespCode());
            errorAttributes.put("message", businessException.getMessage());
        }
        return errorAttributes;
    }

}
