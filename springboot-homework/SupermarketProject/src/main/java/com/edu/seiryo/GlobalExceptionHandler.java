package com.edu.seiryo;

import com.edu.seiryo.admin.exceptions.ParamsException;
import com.edu.seiryo.admin.model.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕捉器
 * @author TianTian
 * @date 2022/1/8 15:21
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @RequestMapping
    public String AccessDeniedExceptionHandler(AccessDeniedException e){
        return "403";
    }


    @ExceptionHandler(ParamsException.class)
    @ResponseBody
    public RespBean paramsExceptionHandler(ParamsException e){
        return  RespBean.error(e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RespBean exceptionHandler(Exception e){
        return  RespBean.error(e.getMessage());
    }

}
