package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.bean.UserRole;
import com.yjxxt.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends BaseService<UserRole,Integer>{

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

}
