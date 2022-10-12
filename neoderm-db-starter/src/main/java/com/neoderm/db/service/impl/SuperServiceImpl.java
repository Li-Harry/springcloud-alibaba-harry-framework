package com.neoderm.db.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neoderm.db.service.ISuperService;

/**
 * service接口实现父类
 * @param <M>
 * @param <T>
 */
public class SuperServiceImpl <M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T> {
}
