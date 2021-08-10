package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //查询
    Integer countPermissionByRoleId(Integer roleId);

    //删除原有角色
    Integer deletePermissionByRoleId(Integer roleId);

    List<Integer> selectModuleByRoleId(Integer roleId);

    List<String> queryUserHasRolesPermissions(int userId);
}