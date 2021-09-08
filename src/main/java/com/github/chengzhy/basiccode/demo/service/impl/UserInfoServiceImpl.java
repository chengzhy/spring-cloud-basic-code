package com.github.chengzhy.basiccode.demo.service.impl;

import com.github.chengzhy.basiccode.demo.entity.UserInfo;
import com.github.chengzhy.basiccode.demo.mapper.UserInfoMapper;
import com.github.chengzhy.basiccode.demo.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息Service实现
 * @author chengzhy
 * @date 2021/8/24 15:28
 **/
@Service
@CacheConfig(cacheNames = "userInfo")
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageInfo<UserInfo> pagelistUserInfo(int pageNum, int pageSize, String userName,
                                               String emailAddress, String mobilePhone) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfo> userInfoList = userInfoMapper.selectByConditions(userName, emailAddress, mobilePhone);
        return new PageInfo<>(userInfoList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "#id", unless = "#result == null")
    public UserInfo getUserInfo(String id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

}
