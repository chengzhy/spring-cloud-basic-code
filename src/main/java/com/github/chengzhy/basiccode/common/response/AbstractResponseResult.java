package com.github.chengzhy.basiccode.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
  * 接口返回响应体
  * @author chengzhy
  * @date 2021/8/4 16:56
  **/
@Getter
@Setter
public abstract class AbstractResponseResult<T> implements Serializable {

    /**
     * 响应编码
     */
    protected int code;

    /**
     * 响应信息
     */
    protected String message;

    /**
     * 响应数据
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected T data;

    /**
     * 请求是否成功
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected Boolean success;

    /**
     * 当前时间戳
     */
    protected long timestamp;

}
