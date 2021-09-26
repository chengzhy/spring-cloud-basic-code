package com.github.chengzhy.basiccode.common.mybatis;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * BasicMapper
 * @author chengzhy
 * @date 2021/8/4 10:51
 */
public interface BasicMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T>, ConditionMapper<T> {
}
