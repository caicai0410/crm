package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.SaleChance;
import com.yjxxt.query.SaleChanceQuery;

import java.util.List;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer>{
    /**
     *新增加Query条件查询
     *营销机会数据查询
     * 查询 查询所有的条件
     * 查询
     * 查询
     */
    public List<SaleChance> selectParams(SaleChanceQuery query);
}