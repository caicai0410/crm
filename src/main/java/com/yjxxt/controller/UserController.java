package com.yjxxt.controller;


import com.github.pagehelper.PageException;
import com.sun.net.httpserver.HttpServer;
import com.yjxxt.annotation.RequiredPermission;
import com.yjxxt.base.BaseController;

import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.User;
import com.yjxxt.exceptions.ParamsException;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.aspectj.asm.IModelFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo sayLogin(String username,String userpwd){
        //实例化对象
        ResultInfo resultInfo = new ResultInfo();
        //调用
       // try {
            UserModel um = userService.doLogin(username, userpwd);
            resultInfo.setResult(um);
        //}catch (ParamsException pe){
        //    pe.printStackTrace();
        //    resultInfo.setCode(pe.getCode());
        //    resultInfo.setMsg(pe.getMsg());
        //}catch (Exception ex){
        //    ex.printStackTrace();
        //    resultInfo.setMsg("操作失败了");
        //    resultInfo.setCode(500);
        //}

        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String sayUpdate(){
        return "user/password";
    }

    @RequestMapping("toSettingPage")
    public String saySetting(HttpServletRequest req, Model model){
        //获取当前对象信息
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用查询
        User user = userService.selectByPrimaryKey(userId);
        //存储
        model.addAttribute("user",user);
        return "user/setting";
    }

    //跳转用户模块user
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    //跳转用户模块user 添加或者修改
    @RequestMapping("addOrUpdatePage")
    public String addUpdate(Integer id, Model model){
        //判断 添加还是修改
        //id不为空为 为修改
        if(id!=null){
            //查询的对象信息
            User user = userService.selectByPrimaryKey(id);
            //存储到作用域
            System.out.println(user);
            model.addAttribute("user",user);
        }
        return "user/add_updata";
    }

    //user添加
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user){
        //调用方法添加
        userService.addUser(user);
        //返回目标对象
        return success("目标添加成功");
    }

    //修改
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(User user){
        //调用方法添加
        userService.changeUser(user);
        //返回目标对象
        return success("目标修改成功");
    }

    //删除
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo dels(Integer [] ids){
        //调用方法删除
        userService.removeIds(ids);
        //返回目标对象
        return success("目标删除成功");
    }


    /**
     *
     * @param req
     * @param oldPassword
     * @param newPassword
     * @param confirmPwd
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    //工具 老密码 新密码 确定密码
    public ResultInfo sayUpdate(HttpServletRequest req, String oldPassword, String newPassword, String confirmPwd) {
        //实例化
        ResultInfo resultInfo = new ResultInfo();
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法修改
        //try {
            userService.updatePassword(userId,oldPassword,newPassword,confirmPwd);
        //} catch (ParamsException pex) {
        //    pex.printStackTrace();
        //    resultInfo.setCode(pex.getCode());
        //    resultInfo.setMsg(pex.getMsg());
        //} catch (Exception ex) {
        //    ex.printStackTrace();
        //    resultInfo.setCode(500);
        //    resultInfo.setMsg("操作异常");
        //}
        //返回对象
        return resultInfo;
    }

    //查询销售
    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String, Object>> sayListSales(){
        //调用方法 service方法 查询所有销售
        List<Map<String, Object>> list = userService.findAllSales();
        //
        return list;
    }

    //三个条件联合查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery query){ //添加条件查询参数
        //调用Service条件查询
        return userService.findUserByParam(query);
    }
}
