package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.Role;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /*查询全部*/
    @RequestMapping("roles")
    @ResponseBody
    public List<Map<String, Object>> sayRoles(Integer userId) {
        return roleService.findAllRoles(userId);
    }

    /*条件查询*/
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(RoleQuery query) {
        //调用方法
        return roleService.findAll(query);
    }

    /*首页面*/
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /*修改 添加*/
    @RequestMapping("addOrUpdate")
    public String addOrUpdate(Integer roleId, Model model) {
        if (roleId != null) {
            //根据ID查询对象信息
            Role role = roleService.selectByPrimaryKey(roleId);
            //存储到作用域
            model.addAttribute("role", role);
        }
        return "role/add_update";
    }

    /*删除*/
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo sayDels(Integer roleId){
        roleService.removeRoleById(roleId);
        return  success("删除成功");
    }

    /*添加*/
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(Role role){
        roleService.addRole(role);
        return  success("角色添加成功");
    }

    /*修改*/
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.changeRole(role);
        return  success("角色修改成功");
    }

    //授权页面
    @RequestMapping("toGrantPage")
    public String sayGrand(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    //授权 角色 真实授权
    @RequestMapping("addGrand")
    @ResponseBody
    public ResultInfo addGrand(Integer roleId,Integer [] mids){
        roleService.addGrand(roleId,mids);
        return success("角色授权成功");
    }
}
