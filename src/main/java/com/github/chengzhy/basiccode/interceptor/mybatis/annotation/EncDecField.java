package com.github.chengzhy.basiccode.interceptor.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 加解密域注解(与{@link EncDecClass @EncDecClass}注解结合使用)
 * @author chengzhy
 * @date 2021/8/16 15:09
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncDecField {
}
