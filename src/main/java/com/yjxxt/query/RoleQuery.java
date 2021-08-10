package com.yjxxt.query;

import com.yjxxt.base.BaseQuery;

public class RoleQuery extends BaseQuery {

    private String roleName;

    public RoleQuery() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
