package com.neoderm.db.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.neoderm.db.properties.MybatisPlusAutoFillProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 元数据处理
 */
@Slf4j
public class DateMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private MybatisPlusAutoFillProperties autoFillProperties;

    public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    @Override
    public boolean openInsertFill() {
        return this.autoFillProperties.getEnableInsertFill();
    }

    @Override
    public boolean openUpdateFill() {
        return this.autoFillProperties.getEnableUpdateFill();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入时自动填充...");
        Object createTime = this.getFieldValByName(this.autoFillProperties.getCreateTimeField(), metaObject);
        Object createBy = this.getFieldValByName(this.autoFillProperties.getCreateByField(), metaObject);
        if (ObjectUtils.isEmpty(createTime)) {
            this.setFieldValByName(this.autoFillProperties.getCreateTimeField(), DateUtil.now(), metaObject);
        }
        if (ObjectUtils.isEmpty(createBy)) {
            this.setFieldValByName(this.autoFillProperties.getCreateByField(), getCurrentUser(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新时自动填充...");
        this.setFieldValByName(this.autoFillProperties.getUpdateTimeField(), DateUtil.now(), metaObject);
        this.setFieldValByName(this.autoFillProperties.getUpdateByField(), getCurrentUser(), metaObject);
    }

    /**
     * 获取当前操作人
     * */
    private String getCurrentUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userName = request.getHeader("x-user-header");
        if (ObjectUtil.isEmpty(userName)) {
            return "";
        }
        log.info("操作人为:{}", userName);
        return userName;
    }

}
