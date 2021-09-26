package com.github.chengzhy.basiccode.interceptor.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 加解密类注解
 * @author chengzhy
 * @date 2021/8/16 15:08
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncDecClass {
}
