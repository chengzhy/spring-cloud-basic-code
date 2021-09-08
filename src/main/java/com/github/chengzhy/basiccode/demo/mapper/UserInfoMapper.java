package com.github.chengzhy.basiccode.demo.mapper;

import com.github.chengzhy.basiccode.common.mybatis.BasicMapper;
import com.github.chengzhy.basiccode.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息Mapper
 * @author chengzhy
 * @date 2021/8/24 15:28
 */
@Mapper
public interface UserInfoMapper extends BasicMapper<UserInfo> {

    /**
     * 条件查询
     *
     * @author chengzhy
     * @param userName 用户名
     * @param emailAddress 邮箱地址
     * @param mobilePhone 手机号码
     * @date 2021/9/6 16:00
     * @return 用户信息结果集
     */
    List<UserInfo> selectByConditions(@Param("userName") String userName,
                                      @Param("emailAddress") String emailAddress,
                                      @Param("mobilePhone") String mobilePhone);

}
