package com.yjxxt.base;


import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {


    //动态生成项目名 拼接路径
    @ModelAttribute
    public void preHandler(HttpServletRequest request){
        System.out.println(request.getContextPath()+"<<");
        request.setAttribute("ctx", request.getContextPath());
    }

    /**
     *每次判断返回值都是1 每次都要拿这个new这个对象
     *将对象装到方法里面 需要直接调用 并实例化
     * @return
     *  并且将success传到下个方法里面 实例化resultInfo
     * msg为消息 result为异常对象 将消息传到方法里面并且实例化
     */
    public ResultInfo success(){
        return new ResultInfo();
    }

    public ResultInfo success(String msg){
        ResultInfo resultInfo= new ResultInfo();
        resultInfo.setMsg(msg);
        return resultInfo;
    }

    public ResultInfo success(String msg,Object result){
        ResultInfo resultInfo= new ResultInfo();
        resultInfo.setMsg(msg);
        resultInfo.setResult(result);
        return resultInfo;
    }
}
