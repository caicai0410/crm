package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.bean.User;
import com.yjxxt.service.PermissionService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;


    @RequestMapping("index")
    public String sayIndex() {
        return "index";
    }

    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest req) {
        //获取Cookie用户的数据
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //获取当前用户的权限码
        List<String> permissions=permissionService.queryUserHasRolesHasPermissions(userId);
        //存储
        req.setAttribute("user",user);
        //存储当前用户的权限码到会话作用域
        req.getSession().setAttribute("permissions",permissions);
        //转发
        return "main";
    }
}
