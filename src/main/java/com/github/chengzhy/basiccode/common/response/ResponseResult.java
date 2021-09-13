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
        ResponseResult<T> responseResult = new ResponseResult<T>();
        responseResult.setCode(HttpStatus.OK.value());
        responseResult.setMessage(message);
        responseResult.setData(data);
        responseResult.setSuccess(true);
        responseResult.setTimestamp(System.currentTimeMillis());
        return responseResult;
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
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        responseResult.setSuccess(false);
        responseResult.setTimestamp(System.currentTimeMillis());
        return responseResult;
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
        ResponseResult<T> responseResult = new ResponseResult<T>();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        responseResult.setData(data);
        responseResult.setTimestamp(System.currentTimeMillis());
        return responseResult;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponseResult.class.getSimpleName() + "[", "]")
                .add("code=" + getCode())
                .add("message='" + getMessage() + "'")
                .add("data=" + getData())
                .add("success=" + getSuccess())
                .add("timestamp='" + getTimestamp() + "'")
                .toString();
    }

}
