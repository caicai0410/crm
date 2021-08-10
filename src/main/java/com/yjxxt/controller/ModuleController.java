package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    /*//全查
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> sayAllModule(){
       return moduleService.findAllModules();
    }*/

    //原来是全查 现在改为条件查 查询该用户拥有的角色
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> sayAllModule(Integer roleId){
        return moduleService.findAllModules(roleId);
    }
}
