package com.github.chengzhy.basiccode.exception.handler;

import com.github.chengzhy.basiccode.common.response.ResponseResult;
import com.github.chengzhy.basiccode.exception.runtime.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 统一异常处理
 * @author chengzhy
 * @date 2021/8/6 15:54
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String BUSINESS_EXCEPTION_MESSAGE_PREFIX = "message:";

    /**
     * 请求方式不正确HttpRequestMethodNotSupportedException异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/9/6 14:25
     * @return 错误返回响应体
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request,
                                                                           HttpRequestMethodNotSupportedException e) {
        StringBuilder errorMessage = new StringBuilder("请求方式");
        errorMessage.append(e.getMethod())
                .append("错误, 请改为")
                .append(String.join("、", e.getSupportedMethods()));
        log.error("请求[{}]请求方式错误！", request.getRequestURL());
        log.error("错误信息: {}", e.getMessage());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
    }


    /**
     * 参数类型不匹配MethodArgumentTypeMismatchException异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/9/6 13:54
     * @return 错误返回响应体
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseResult<?> methodArgumentTypeMismatchExceptionHandler(HttpServletRequest request,
                                                                        MethodArgumentTypeMismatchException e) {
        StringBuilder errorMessage = new StringBuilder("参数[");
        errorMessage.append(e.getName())
                .append(": ")
                .append(e.getValue())
                .append("]类型错误！");
        log.error("请求[{}]参数类型错误！", request.getRequestURL());
        log.error("错误信息: {}", errorMessage.toString());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
    }

    /**
     * 请求参数缺失MissingServletRequestParameterException异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/10/26 14:32
     * @return 错误返回响应体
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResult<?> missingServletRequestParameterExceptionHandler(HttpServletRequest request,
                                                                            MissingServletRequestParameterException e) {
        log.error("请求[{}]请求参数缺失！", request.getRequestURL());
        log.error("错误信息: 缺失参数[{}, 类型:{}]", e.getParameterName(), e.getParameterType());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(),
                String.format("缺失参数[%s, 类型:%s]", e.getParameterName(), e.getParameterType()));
    }

    /**
     * 请求体参数校验失败MethodArgumentNotValidException异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/8/6 15:54
     * @return 错误返回响应体
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> methodArgumentNotValidExceptionHandler(HttpServletRequest request,
                                                                    MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()){
            errorMessage.append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage());
        }
        log.error("请求[{}]请求体参数校验失败！", request.getRequestURL());
        log.error("错误信息: {}", errorMessage.toString());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
    }

    /**
     * 调用方法传参校验失败ConstraintViolationException异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/10/26 8:54
     * @return 错误返回响应体
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResult<?> constraintViolationExceptionHandler(HttpServletRequest request,
                                                                 ConstraintViolationException e) {
        log.error("请求[{}]调用方法传参校验失败！", request.getRequestURL());
        log.error("错误信息: {}", e.getMessage(), e);
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 业务BusinessException异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/8/6 17:04
     * @return 错误返回响应体
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<?> businessExceptionHandler(HttpServletRequest request,
                                                      BusinessException e) {
        int messageIndex = e.getMessage().indexOf(BUSINESS_EXCEPTION_MESSAGE_PREFIX) == -1 ? 0 :
                e.getMessage().indexOf(BUSINESS_EXCEPTION_MESSAGE_PREFIX) + BUSINESS_EXCEPTION_MESSAGE_PREFIX.length();
        switch (e.getHttpStatus()) {
            case OK:
                log.info("请求[{}]业务处理成功！", request.getRequestURL());
                log.info("异常信息: {}", e.getMessage().substring(messageIndex));
                return ResponseResult.success(e.getMessage().substring(messageIndex));
            case INTERNAL_SERVER_ERROR:
                log.error("请求[{}]业务处理错误！", request.getRequestURL());
                log.error("错误信息: {}", e.getMessage(), e);
                return ResponseResult.fail(e.getMessage().substring(messageIndex));
            default:
                log.error("请求[{}]业务处理异常！", request.getRequestURL());
                log.error("异常信息: {}", e.getMessage(), e);
                return ResponseResult.response(e.getHttpStatus().value(),
                        e.getMessage().substring(messageIndex));
        }
    }

    /**
     * 异常处理
     *
     * @author chengzhy
     * @param request 请求
     * @param e 异常
     * @date 2021/8/6 15:54
     * @return 错误返回响应体
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> exceptionHandler(HttpServletRequest request, Exception e) {
        return handlerException(request.getRequestURL(), e);
    }

    /**
     * 统一异常处理
     *
     * @author chengzhy
     * @param requestUrl 请求url
     * @param e 异常
     * @date 2021/8/6 15:54
     * @return 错误返回响应体
     */
    private ResponseResult<?> handlerException(StringBuffer requestUrl, Exception e) {
        String errorMessage = e.getMessage();
        if (StringUtils.isBlank(errorMessage)) {
            errorMessage = "操作失败！";
        }
        log.error("请求[{}]错误！", requestUrl);
        log.error("错误信息: {}", errorMessage, e);
        return ResponseResult.fail(errorMessage);
    }

}
