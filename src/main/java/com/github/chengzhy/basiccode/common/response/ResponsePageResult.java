package com.github.chengzhy.basiccode.common.response;

import com.github.chengzhy.basiccode.common.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.StringJoiner;

/**
 * 计数返回响应体
 * @author chengzhy
 * @date 2021/8/4 16:56
 **/
@Getter
@Setter
public final class ResponsePageResult<T> extends AbstractResponseResult<T> {

    private static final long serialVersionUID = -4891998292422962459L;

    /**
     * 响应结果总数
     */
    private long total;

    private ResponsePageResult(int code, String message, T data, Boolean success, long timestamp, long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = timestamp;
        this.total = total;
    }

    /**
     * 请求成功(响应结果, 响应结果总数)
     *
     * @author chengzhy
     * @param data 响应结果
     * @param total 响应结果总数
     * @param <T> 响应结果类型
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static <T> ResponsePageResult<T> success(T data, long total) {
        return success(data, total, Constants.SUCCESS_MESSAGE);
    }

    /**
     * 请求成功(响应结果, 响应结果总数, 响应信息)
     *
     * @author chengzhy
     * @param data 响应结果
     * @param total 响应结果总数
     * @param message 响应信息
     * @param <T> 响应结果类型
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static <T> ResponsePageResult<T> success(T data, long total, String message) {
        return ResponsePageResult.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .success(true)
                .timestamp(System.currentTimeMillis())
                .total(total)
                .build();
    }

    /**
     * 请求失败()
     *
     * @author chengzhy
     * @date 2021/8/4 16:56
     * @return 请求失败返回响应体
     */
    public static ResponsePageResult fail() {
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
    public static ResponsePageResult fail(int code) {
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
    public static ResponsePageResult fail(String message) {
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
    public static ResponsePageResult fail(int code, String message) {
        return ResponsePageResult.builder()
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
     * @date 2021/8/27 10:20
     * @return 请求响应返回体
     */
    public static ResponsePageResult response(int code, String message) {
        return ResponsePageResult.response(code, null, 0L, message);
    }

    /**
     * 请求响应(响应编码, 响应结果, 响应结果总数)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param data 响应结果
     * @param total 响应结果总数
     * @param <T> 响应结果类型
     * @date 2021/8/27 10:20
     * @return 请求响应返回体
     */
    public static <T> ResponsePageResult<T> response(int code, T data, long total) {
        return ResponsePageResult.response(code, data, total, Constants.RESPONSE_MESSAGE);
    }

    /**
     * 请求响应(响应编码, 响应结果, 响应结果总数, 响应信息)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param data 响应结果
     * @param total 响应结果总数
     * @param message 响应信息
     * @param <T> 响应结果类型
     * @date 2021/8/27 10:20
     * @return 请求响应返回体
     */
    public static <T> ResponsePageResult<T> response(int code, T data, long total, String message) {
        return ResponsePageResult.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .total(total)
                .build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponsePageResult.class.getSimpleName() + "[", "]")
                .add("code=" + getCode())
                .add("message='" + getMessage() + "'")
                .add("data=" + getData())
                .add("success=" + getSuccess())
                .add("timestamp=" + getTimestamp())
                .add("total=" + getTotal())
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
        private long total;

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

        public Builder<T> total(long total) {
            this.total = total;
            return this;
        }

        public ResponsePageResult<T> build() {
            return new ResponsePageResult<>(this.code, this.message, this.data, this.success, this.timestamp, this.total);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Builder.class.getSimpleName() + "[", "]")
                    .add("code=" + code)
                    .add("message='" + message + "'")
                    .add("data=" + data)
                    .add("success=" + success)
                    .add("timestamp=" + timestamp)
                    .add("total=" + total)
                    .toString();
        }
    }

}
