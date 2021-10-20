package com.github.chengzhy.basiccode.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户信息表
 * @author chengzhy
 * @date 2021/8/24 14:55
 */
@Table(name = "user_info")
@Data
public class UserInfo {
    /**
     * 用户id
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "USER_NAME")
    @NotBlank(message = "用户名不能为空！")
    @Size(message = "用户名不能超过32个字符！", max = 32)
    private String userName;

    /**
     * 密码
     */
    @Column(name = "PASS_WORD")
    @NotBlank(message = "密码不能为空！")
    private String passWord;

    /**
     * 邮箱
     */
    @Column(name = "EMAIL_ADDRESS")
    @Size(message = "邮箱不能超过64个字符！", max = 64)
    @Email(message = "邮箱格式不正确！")
    private String emailAddress;

    /**
     * 手机号
     */
    @Column(name = "MOBILE_PHONE")
    @Size(message = "手机号不能超过11个字符！", max = 11)
    private String mobilePhone;

    /**
     * 创建时间
     * {@link JsonFormat @JsonFormat} 从数据库读出日期格式时，进行转换的规则
     * {@link DateTimeFormat @DateTimeFormat} 接受从前端传入的日期格式，映射到java类日期属性的规则
     */
    @Column(name = "CREATE_TIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
