package com.neoderm.core.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.neoderm.core.annotation.LoginUser;
import com.neoderm.core.aspect.user.SysCustomer;
import com.neoderm.core.aspect.user.User;
import com.neoderm.core.constant.SecurityConstants;
import com.neoderm.core.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录信息切面
 * 从请求头获取userId、username、userIdentity
 */
@Aspect
public class LoginUserAspect {

    @Before("@within(loginUser) || @annotation(loginUser)")
    public void beforLogin(JoinPoint joinPoint, LoginUser loginUser) {
        // 获取上下文request，解析用户信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userId = request.getHeader(SecurityConstants.USER_ID_HEADER);
        if (ObjectUtil.isEmpty(userId)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "未登录");
        }
        // 获取参数并赋值
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            // 对于不同的Vo类进行相应的赋值
            if (arg instanceof SysCustomer) {
                String uniqueIdentity = request.getHeader(SecurityConstants.USER_UNIQUE_IDENTITY_HEADER);
                if (ObjectUtil.isEmpty(uniqueIdentity)) {
                    throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), "token过期");
                }
                SysCustomer customerVo = (SysCustomer) arg;
                customerVo.setUserId(userId);
                customerVo.setUniqueIdentity(uniqueIdentity);
            }
            if (arg instanceof User) {
                String userName = request.getHeader(SecurityConstants.USER_NAME_HEADER);
                User userVo = (User) arg;
                userVo.setUserId(userId);
                userVo.setUserName(userName);
            }
        }
    }

}
