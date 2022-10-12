package com.neoderm.feign.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.neoderm.core.exception.BusinessException;
import com.neoderm.core.utils.JsonUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * feign异常解码器
 */
@Configuration
@Slf4j
public class FeignExceptionDecoder extends ErrorDecoder.Default {

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = super.decode(methodKey, response);
        ByteBuffer responseBody = ((FeignException) exception).responseBody().get();
        try {
            // 获取原始的返回内容
            String json = StandardCharsets.UTF_8.newDecoder().decode(responseBody.asReadOnlyBuffer()).toString();
            // 异常处理
            exception = parseException(json);
            log.error("feign调用异常，异常信息：{}", json);
            return exception;
        } catch (IOException ex) {
            log.error("异常转化错误" + ex.getMessage(), ex);
        }
        return exception;
    }

    /**
     * 参数校验异常和业务异常解析
     *
     * @param json feign抛出的异常数据
     * @return
     * @throws
     */
    private Exception parseException(String json) {
        ValidationExceptionInfo validationExceptionInfo = JsonUtil.jsonStringToBean(json, ValidationExceptionInfo.class);
        Exception exception;
        if (Objects.nonNull(validationExceptionInfo)) {
            Integer respCode = Optional.ofNullable(validationExceptionInfo.getStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR.value());
            if (Objects.nonNull(validationExceptionInfo.getRespCode())) {
                respCode = validationExceptionInfo.getRespCode();
            }
            if (CollectionUtil.isNotEmpty(validationExceptionInfo.getErrors())) {
                exception = new BusinessException(respCode, validationExceptionInfo.getErrors().get(0).getDefaultMessage());
            } else {
                exception = new BusinessException(respCode, validationExceptionInfo.getMessage());
            }
        } else {
            exception = new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常");
        }
        return exception;
    }

}
