package com.neoderm.core.exception;

import com.neoderm.core.enums.BusinessCodeEnum;
import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BusinessException extends RuntimeException{

    private Integer respCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer respCode, String respMsg) {
        super(respMsg);
        this.respCode = respCode;
    }

    public BusinessException(BusinessCodeEnum businessCodeEnum) {
        super(businessCodeEnum.getMessage());
        this.respCode = businessCodeEnum.getCode();
    }

}
