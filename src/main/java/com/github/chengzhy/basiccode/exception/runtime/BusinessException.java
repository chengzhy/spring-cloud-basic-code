package com.github.chengzhy.basiccode.exception.runtime;

import org.springframework.http.HttpStatus;

/**
 * 业务异常
 * @author chengzhy
 * @date 2021/8/6 16:44
 **/
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1660220342106062823L;

    private static final String CODE_MESSAGE_FORMAT = "code:%d, message:%s";

    private HttpStatus httpStatus;

    public BusinessException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        super(String.format(CODE_MESSAGE_FORMAT, httpStatus.value(), message));
        this.httpStatus = httpStatus;
    }

    public BusinessException(HttpStatus httpStatus, Throwable throwable) {
        super(throwable);
        this.httpStatus = httpStatus;
    }

    public BusinessException(HttpStatus httpStatus, String message, Throwable throwable) {
        super(String.format(CODE_MESSAGE_FORMAT, httpStatus.value(), message), throwable);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
