package com.neoderm.core.aspect.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 管理员
 */
@Getter
@Setter
public class SysCustomer implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户唯一标识
     */
    private String uniqueIdentity;

}
