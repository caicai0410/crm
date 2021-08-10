package com.yjxxt;

import com.alibaba.fastjson.JSON;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalException implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
        /**
         * 判断异常类型
         *     如果是未登录异常，则先执行相关的拦截操作
         *     直接返回到登陆页面
         */
        if (ex instanceof NoLoginException) {
            // 如果捕获的是未登录异常，则重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }
        //实例化ModelAndView
        ModelAndView mav=new ModelAndView();
        mav.setViewName("error");
        mav.addObject("code",400);
        mav.addObject("msg","系统异常，请稍微访问..");
        //json,试图名
        if(handler instanceof HandlerMethod){
            //强制转换
            HandlerMethod handlerMethod =(HandlerMethod)handler;
            //
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断
            if(responseBody ==null){
                //view
                if(ex instanceof ParamsException){
                    ParamsException pe= (ParamsException) ex;
                    mav.addObject("code",pe.getCode());
                    mav.addObject("msg",pe.getMsg());
                }
                return  mav;
            }else{
                //json
                //实例化
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，稍后访问...");

                if(ex instanceof ParamsException){
                    ParamsException pe= (ParamsException) ex;
                    mav.addObject("code",pe.getCode());
                    mav.addObject("msg",pe.getMsg());
                }
                //构建响应
                resp.setContentType("application/json;charset=utf-8");
                //获取流
                PrintWriter out = null;
                try {
                    out = resp.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    //刷新
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(out!=null){
                        out.close();
                    }
                }
                return null;
            }
        }
        //返回目标对象
        return mav;
    }
}
