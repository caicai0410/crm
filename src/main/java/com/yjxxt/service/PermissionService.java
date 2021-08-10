package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.bean.Permission;
import com.yjxxt.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    public List<String> queryUserHasRolesHasPermissions(int userId) {
        return permissionMapper.queryUserHasRolesPermissions(userId);
    }
}
