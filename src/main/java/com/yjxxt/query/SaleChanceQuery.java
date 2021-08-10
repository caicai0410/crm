package com.yjxxt.query;

import com.yjxxt.base.BaseQuery;

/**
 * 营销机会管理查询 三个板块查询 总查询
 * 查询客户名称
 * 查询创建人
 * 查询分配状态
 */

public class SaleChanceQuery extends BaseQuery {
    //客户名称
    private String customerName;
    //创建人
    private String createMan;
    //分配状态，0，未分配，1，已经分配
    private Integer state;


    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state=" + state +
                '}';
    }
}
