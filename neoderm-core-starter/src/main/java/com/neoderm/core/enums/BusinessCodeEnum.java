package com.neoderm.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务异常码定义
 */
@Getter
@AllArgsConstructor
public enum BusinessCodeEnum {

    COMMON_SUCCESS(200, "common_success", "操作成功"),

    COMMON_ERROR(500, "common_error", "操作失败");

    private Integer code;

    private String value;

    private String message;

}
