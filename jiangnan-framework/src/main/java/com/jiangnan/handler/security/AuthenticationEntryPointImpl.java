package com.jiangnan.handler.security;

import com.alibaba.fastjson.JSON;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.enums.AppHttpCodeEnum;
import com.jiangnan.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        //打印错误信息
        authenticationException.printStackTrace();

        //InsufficientAuthenticationException
        //BadCredentialsException
        ResponseResult result = null;
        if (authenticationException instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(), authenticationException.getMessage());
        } else if (authenticationException instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), authenticationException.getMessage());
        } else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证或授权失败");
        }

//        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);

        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
