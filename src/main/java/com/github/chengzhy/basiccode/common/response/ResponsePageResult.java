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
        return success(data, Constants.SUCCESS_MESSAGE, total);
    }

    /**
     * 请求成功(响应结果, 响应信息, 响应结果总数)
     *
     * @author chengzhy
     * @param data 响应结果
     * @param message 响应信息
     * @param total 响应结果总数
     * @param <T> 响应结果类型
     * @date 2021/8/4 16:56
     * @return 请求成功返回响应体
     */
    public static <T> ResponsePageResult<T> success(T data, String message, long total) {
        ResponsePageResult<T> responsePageResult = new ResponsePageResult<T>();
        responsePageResult.setCode(HttpStatus.OK.value());
        responsePageResult.setMessage(message);
        responsePageResult.setData(data);
        responsePageResult.setTotal(total);
        responsePageResult.setSuccess(true);
        responsePageResult.setTimestamp(System.currentTimeMillis());
        return responsePageResult;
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
        ResponsePageResult responsePageResult = new ResponsePageResult();
        responsePageResult.setCode(code);
        responsePageResult.setMessage(message);
        responsePageResult.setSuccess(false);
        responsePageResult.setTimestamp(System.currentTimeMillis());
        return responsePageResult;
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
        return ResponsePageResult.response(code, message, null, 0);
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
        return ResponsePageResult.response(code, null, data, total);
    }

    /**
     * 请求响应(响应编码, 响应信息, 响应结果, 响应结果总数)
     *
     * @author chengzhy
     * @param code 响应编码
     * @param message 响应信息
     * @param data 响应结果
     * @param total 响应结果总数
     * @param <T> 响应结果类型
     * @date 2021/8/27 10:20
     * @return 请求响应返回体
     */
    public static <T> ResponsePageResult<T> response(int code, String message, T data, long total) {
        ResponsePageResult<T> responsePageResult = new ResponsePageResult<>();
        responsePageResult.setCode(code);
        responsePageResult.setMessage(message);
        responsePageResult.setData(data);
        responsePageResult.setTimestamp(System.currentTimeMillis());
        responsePageResult.setTotal(total);
        return responsePageResult;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponsePageResult.class.getSimpleName() + "[", "]")
                .add("code=" + getCode())
                .add("message='" + getMessage() + "'")
                .add("data=" + getData())
                .add("success=" + getSuccess())
                .add("timestamp='" + getTimestamp() + "'")
                .add("total=" + getTotal())
                .toString();
    }

}
