package com.jiangnan.handler.exception;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.enums.AppHttpCodeEnum;
import com.jiangnan.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
//@ResponseBody
//1+1=2
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException systemException) {

        //打印异常信息
        log.error("出现异常！{}", systemException);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(systemException.getCode(), systemException.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception exception) {

        //打印异常信息
        log.error("出现异常！{}", exception);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), exception.getMessage());
    }
}
