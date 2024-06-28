package com.jiangnan.aspect;

import com.alibaba.fastjson.JSON;
import com.jiangnan.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 切面类
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.jiangnan.annotation.SystemLog)")
    public void pc() {
    }

    @Around("pc()")
    public Object printLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object ret;
        try {
            //调用目标方法之前
            handlerBefore(proceedingJoinPoint);
            ret = proceedingJoinPoint.proceed();
            //调用目标方法之后
            handlerAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======================end=======================" + System.lineSeparator());
        }
        return ret;
    }

    private void handlerAfter(Object ret) {
        // 打印出参  response
        log.info("返回参数   : {}", JSON.toJSONString(ret));
    }

    private void handlerBefore(ProceedingJoinPoint proceedingJoinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(proceedingJoinPoint);

        log.info("======================Start======================");
        // 打印请求 URL
        log.info("请求URL   : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("请求方式   : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法  Class Method
        log.info("请求类名   : {}.{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("访问IP    : {}", request.getRemoteHost());
        // 打印请求入参
        log.info("传入参数   : {}", JSON.toJSONString(proceedingJoinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        return systemLog;
    }
}
