package com.neoderm.core.aspect.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户
 */
@Getter
@Setter
public class User implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

}
