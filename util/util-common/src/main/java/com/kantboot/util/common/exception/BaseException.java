package com.kantboot.util.common.exception;

import com.kantboot.util.common.result.RestResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理的基类
 * @author 方某方
 */
@ControllerAdvice
@Setter
@Getter
@Accessors(chain = true)
public class BaseException extends RuntimeException{

    /**
     * 便捷错误码(数字编码)
     */
    private Integer state = 3000;
    private String stateCode = "FAIL";
    private String message;

    public BaseException() {
    }

    public BaseException(Integer state, String message) {
        this.state = state;
        this.message = message;
    }

    public BaseException(Integer state, String stateCode, String message) {
        this.state = state;
        this.stateCode = stateCode;
        this.message = message;
    }

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public RestResult<String> baseException(BaseException e){
        return new RestResult<String>().setState(e.getState()).setErrMsg(e.getMessage()).setStateCode(e.getStateCode());
    }
}
