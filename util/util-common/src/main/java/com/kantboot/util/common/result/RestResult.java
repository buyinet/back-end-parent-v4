package com.kantboot.util.common.result;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 这是一个方便用户在rest操作的工具类
 * @param <T> 泛型
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class RestResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private String code;

    /**
     * 操作编码
     */
    private String operationCode;

    /**
     * 便捷错误码(数字编码)
     */
    public static Integer FAIL_STATE = 3000;

    /**
     * 便捷错误码(字符串编码)
     */
    public static String FAIL_STATE_CODE = "FAIL";

    /**
     * 便捷成功码(数字编码)
     */
    public static Integer SUCCESS_STATE = 2000;

    /**
     * 便捷成功码(字符串编码)
     */
    public static String SUCCESS_STATE_CODE = "SUCCESS";


    /**
     * 返回的状态(数字编码)
     */
    private Integer state;

    /**
     * 返回的状态(字符串编码)
     */
    private String stateCode;

    private String msg;

    private String errMsg;

    /**
     * 返回的数据(泛型)
     */
    private T data;

    public static <T> RestResult<T> success(T data, String msg) {
        return new RestResult()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsg(msg)
                .setStateCode(SUCCESS_STATE_CODE);
    }


    public static <T> RestResult<T> success(T data, String msg, String code) {
        return new RestResult()
                .setState(SUCCESS_STATE)
                .setData(data)
                .setMsg(msg)
                .setCode(code)
                .setStateCode(SUCCESS_STATE_CODE);
    }

    /**
     * 便捷的错误返回
     *
     * @param msg 错误信息
     * @return 返回结果
     */
    public static RestResult error(String msg) {
        return new RestResult()
                .setState(FAIL_STATE)
                .setErrMsg(msg)
                .setStateCode(FAIL_STATE_CODE);
    }

    /**
     * 通用的错误返回
     *
     * @param msg       错误信息
     * @param state     错误状态
     * @param stateCode 错误状态编码
     * @return 返回结果
     */
    public static RestResult error(String msg, Integer state, String stateCode) {
        return new RestResult()
                .setState(state)
                .setErrMsg(msg)
                .setStateCode(stateCode);
    }
}