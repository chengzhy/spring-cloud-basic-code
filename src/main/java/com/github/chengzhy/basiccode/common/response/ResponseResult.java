package com.github.chengzhy.basiccode.common.response;

import com.github.chengzhy.basiccode.common.Constants;
import org.springframework.http.HttpStatus;

import java.util.StringJoiner;

/**
  * 基本返回响应体
  * @author chengzhy
  * @date 2021/8/4 16:56
  **/
public final class ResponseResult<T> extends AbstractResponseResult<T> {

    private static final long serialVersionUID = -690024474918963592L;

    private ResponseResult(int code, String message, T data, Boolean success, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = timestamp;
    }

    /**
     * 请求成功()
     *
     * @author chengzhy
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static ResponseResult success() {
        return success(null, Constants.SUCCESS_MESSAGE);
    }

    /**
     * 请求成功(响应结果)
     *
     * @author chengzhy
     * @param data 响应结果
     * @param <T> 响应结果类型
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static <T> ResponseResult<T> success(T data) {
        return success(data, Constants.SUCCESS_MESSAGE);
    }

    /**
     * 请求成功(响应信息)
     *
     * @author chengzhy
     * @param message 响应信息
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static ResponseResult success(String message) {
        return success(null, message);
    }

    /**
     * 请求成功(响应结果, 响应信息)
     *
     * @author chengzhy
     * @param data 响应结果
     * @param message 响应信息
     * @param <T> 响应结果类型
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        return ResponseResult.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .success(true)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 请求失败()
     *
     * @author chengzhy
     * @date 2021/8/4 16:56
     * @return 请求失败返回响应体
     */
    public static ResponseResult fail() {
        return fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.FAILURE_MESSAGE);
    }

    /**
     * 请求失败(响应编码)
     *
     * @author chengzhy
     * @param code 响应编码
     * @date 2021/8/4 16:56
     * @return 请求失败返回响应体
     */
    public static ResponseResult fail(int code) {
        return fail(code, Constants.FAILURE_MESSAGE);
    }

    /**
     * 请求失败(响应信息)
     *
     * @author chengzhy
     * @param message 响应信息
     * @date 2021/8/4 16:56
     * @return 请求失败返回响应体
     */
    public static ResponseResult fail(String message) {
        return fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    /**
     * 请求失败(响应编码, 响应信息)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param message 响应信息
     * @date 2021/8/4 16:56
     * @return 请求失败返回响应体
     */
    public static ResponseResult fail(int code, String message) {
        return ResponseResult.builder()
                .code(code)
                .message(message)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 请求响应(响应编码, 响应信息)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param message 响应信息
     * @date 2021/8/27 9:43
     * @return 请求响应返回体
     */
    public static ResponseResult response(int code, String message) {
        return response(code, message, null);
    }

    /**
     * 请求响应(响应编码, 响应结果)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param data 响应结果
     * @param <T> 响应结果类型
     * @date 2021/8/27 9:43
     * @return 请求响应返回体
     */
    public static <T> ResponseResult<T> response(int code, T data) {
        return response(code, null, data);
    }

    /**
     * 请求响应(响应编码, 响应信息, 响应结果)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param message 响应信息
     * @param data 响应结果
     * @param <T> 响应结果类型
     * @date 2021/8/27 9:43
     * @return 请求响应返回体
     */
    public static <T> ResponseResult<T> response(int code, String message, T data) {
        return ResponseResult.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponseResult.class.getSimpleName() + "[", "]")
                .add("code=" + getCode())
                .add("message='" + getMessage() + "'")
                .add("data=" + getData())
                .add("success=" + getSuccess())
                .add("timestamp=" + getTimestamp())
                .toString();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private int code;
        private String message;
        private T data;
        private Boolean success;
        private long timestamp;

        public Builder() {
        }

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> success(Boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ResponseResult<T> build() {
            return new ResponseResult(this.code, this.message, this.data, this.success, this.timestamp);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Builder.class.getSimpleName() + "[", "]")
                    .add("code=" + code)
                    .add("message='" + message + "'")
                    .add("data=" + data)
                    .add("success=" + success)
                    .add("timestamp=" + timestamp)
                    .toString();
        }
    }

}
