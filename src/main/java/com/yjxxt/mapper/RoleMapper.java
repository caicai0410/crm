package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.base.BaseQuery;
import com.yjxxt.bean.Role;
import com.yjxxt.query.RoleQuery;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {


    /*查询所有的角色*/
    @MapKey("")
    public List<Map<String,Object>> selectAllRoles(Integer userId);

    /*条件查询*/
    public List<Role> selectParams(RoleQuery query);

    //根据角色名称查询角色信息
    Role selectRoleByRoleName(String roleName);
}