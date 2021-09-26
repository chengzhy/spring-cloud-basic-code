package com.github.chengzhy.basiccode.demo.controller;

import com.github.chengzhy.basiccode.common.response.ResponsePageResult;
import com.github.chengzhy.basiccode.common.response.ResponseResult;
import com.github.chengzhy.basiccode.demo.entity.UserInfo;
import com.github.chengzhy.basiccode.demo.service.UserInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息接口
 * @author chengzhy
 * @date 2021/8/24 15:23
 */
@RestController
@RequestMapping("/v1/userInfo")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

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
    @GetMapping("/pagelist")
    public ResponsePageResult<List<UserInfo>> pagelistUserInfo(@RequestParam(defaultValue = "1") Integer pageNum,
                                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                                               @RequestParam(required = false) String userName,
                                                               @RequestParam(required = false) String emailAddress,
                                                               @RequestParam(required = false) String mobilePhone) {
        PageInfo<UserInfo> pageInfo = userInfoService.pagelistUserInfo(pageNum, pageSize, userName, emailAddress, mobilePhone);
        return ResponsePageResult.success(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 根据用户id获取用户信息
     *
     * @author chengzhy
     * @param id 用户id
     * @date 2021/8/24 15:23
     * @return 用户信息
     */
    @GetMapping("/get")
    public ResponseResult<UserInfo> getUserInfo(@RequestParam("id") String id) {
        return ResponseResult.success(userInfoService.getUserInfo(id));
    }

}
