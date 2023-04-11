package com.kantboot.admin.util.old.nanshouxiangku.exception;

import com.kantboot.util.common.exception.BaseException;

/**
 * 不在登录状态的异常
 */
public class NotLoginException extends BaseException {

    @Override
    public Integer getState() {
        return 7777;
    }

    @Override
    public String getMessage() {
        return "不在回话状态，请登录或重新登录";
    }

}
