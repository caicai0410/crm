package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.User;
import com.yjxxt.query.UserQuery;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    //根据用户名名查询对象信息
    public User selectUserByUserName(String userName);

    //查询所有的销售
    @MapKey("")/*误报*/
    public List<Map<String,Object>> selectAllSales();

    //条件查询 三个搜索框
    public List<User> selectByParams(UserQuery query);

}