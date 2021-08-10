package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.mapper.ModuleMapper;
import com.yjxxt.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yjxxt.bean.Module;

import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;


    /***
     * 查询所有的资源信息
     * @return
     */
   /* public List<TreeDto> findAllModules(){
        return moduleMapper.selectAllModules();
    }*/

    //根据角色查询
    public List<TreeDto> findAllModules(Integer roleId){
        //所有的资源信息
        List<TreeDto> modules = moduleMapper.selectAllModules();
        //当前角色拥有的资源id的集合
        List<Integer> mlist= permissionMapper.selectModuleByRoleId(roleId);
        //遍历
        //checkecd=true,checked=false;
        for (TreeDto treeDto: modules) {
            if(mlist.contains(treeDto.getId())){
                //拥有模块的id,则对象的checked=true,否则checked=false;
                treeDto.setChecked(true);
            }
        }
        return  modules;
    }
}
