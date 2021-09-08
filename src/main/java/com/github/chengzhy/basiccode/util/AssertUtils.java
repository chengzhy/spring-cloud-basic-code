package com.github.chengzhy.basiccode.util;

import com.github.chengzhy.basiccode.exception.runtime.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 断言工具类
 * @author chengzhy
 * @date 2021/8/11 14:29
 **/
public final class AssertUtils extends Assert {

    private AssertUtils() {

    }

    public static void isTrue(boolean expression, HttpStatus httpStatus, String message) {
        if (!expression) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void isTrue(boolean expression, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(Object object, HttpStatus httpStatus, String message) {
        if (Objects.nonNull(object)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void isNull(Object object, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (Objects.nonNull(object)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(Object object, HttpStatus httpStatus, String message) {
        if (Objects.isNull(object)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void notNull(Object object, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (Objects.isNull(object)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void isEmpty(String text, HttpStatus httpStatus, String message) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void isEmpty(String text, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void isNotEmpty(String text, HttpStatus httpStatus, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void isNotEmpty(String text, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void isBlank(String text, HttpStatus httpStatus, String message) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void isBlank(String text, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void isNotBlank(String text, HttpStatus httpStatus, String message) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void isNotBlank(String text, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Object[] arrays, HttpStatus httpStatus, String message) {
        if (ObjectUtils.isEmpty(arrays)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void notEmpty(Object[] arrays, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (ObjectUtils.isEmpty(arrays)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Collection<?> collection, HttpStatus httpStatus, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void notEmpty(Collection<?> collection, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    public static void notEmpty(Map<?, ?> map, HttpStatus httpStatus, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new BusinessException(httpStatus, message);
        }
    }

    public static void notEmpty(Map<?, ?> map, HttpStatus httpStatus, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new BusinessException(httpStatus, nullSafeGet(messageSupplier));
        }
    }

    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return messageSupplier != null ? messageSupplier.get() : null;
    }

}
