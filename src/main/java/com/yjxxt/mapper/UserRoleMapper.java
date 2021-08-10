package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.UserRole;


public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    //查询角色数量
    Integer countUserRoleByUserId(Integer userId);

    //删除原有角色属性
    Integer deleteUserRoleByUserId(Integer userId);
}