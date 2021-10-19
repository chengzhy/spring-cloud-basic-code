package com.github.chengzhy.basiccode.demo.service;

import com.github.chengzhy.basiccode.demo.entity.UserInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * 用户信息Service
 * @author chengzhy
 * @date 2021/8/24 15:27
 */
public interface UserInfoService {

    /**
     * 用户信息分页列表
     *
     * @author chengzhy
     * @param pageNum 分页数
     * @param pageSize 每页大小
     * @param userName 用户名
     * @param emailAddress 邮箱地址
     * @param mobilePhone 电话号码
     * @date 2021/9/6 10:17
     * @return 用户信息分页列表
     */
    PageInfo<UserInfo> pagelistUserInfo(int pageNum, int pageSize, String userName,
                                        String emailAddress, String mobilePhone);

    /**
     * 根据用户id获取用户信息
     *
     * @author chengzhy
     * @param id 用户id
     * @date 2021/8/24 15:23
     * @return 用户信息
     */
    Optional<UserInfo> getUserInfo(@NonNull String id);

}
