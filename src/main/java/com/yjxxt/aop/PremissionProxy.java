package com.yjxxt.aop;

import com.yjxxt.annotation.RequiredPermission;
import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.exceptions.PermissionException;
import com.yjxxt.interceptors.NoLoginInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PremissionProxy {

    @Autowired
    private HttpSession httpSession;

    @Around(value="@annotation(com.yjxxt.annotation.RequiredPermission)")
    public Object sayAround(ProceedingJoinPoint pjp) throws Throwable {
        //获取当前用户的权限
        List<String> permissions= (List<String>) httpSession.getAttribute("permissions");
        //判断
        if(permissions==null){
            throw new NoLoginException();
        }
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        //获取方法
        RequiredPermission re = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //比对权限码
        if(!(permissions.contains(re.code()))){
            //权限不足异常
            throw new PermissionException();
        }
        Object reulst = pjp.proceed();
        //返回目标对象
        return  reulst;
    }
}
