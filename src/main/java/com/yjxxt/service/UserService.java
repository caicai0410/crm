package com.yjxxt.service;

import ch.qos.logback.core.net.AbstractSSLSocketAppender;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mchange.v1.identicator.IdList;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.User;
import com.yjxxt.bean.UserRole;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.mapper.UserRoleMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;


    /**
     * 1)用户名，密码非空
     * 2）用户名是否存在的校验
     * 3）密码是否正确的校验
     * 4）构建一个UserModel
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel doLogin(String userName, String userPwd) {
        // 1)用户名，密码非空
        checkLoginUserAndPwd(userName, userPwd);
        //2）用户名是否存在的校验
        User temp = userMapper.selectUserByUserName(userName);
        //用户是否存在的校验
        AssertUtil.isTrue(temp == null, "用户不存在或者已经注销");
        //用户密码的校验
        checkLoginPwd(userPwd, temp.getUserPwd());
        //构建UserModel
        return builderUser(temp);
    }

    /**
     * 用户名密码验证
     *
     * @param userName 用户名
     * @param userPwd  密码
     */
    private void checkLoginUserAndPwd(String userName, String userPwd) {
        AssertUtil.isTrue(userName == null, "用户名不能为空");
        AssertUtil.isTrue(userPwd == null, "密码不能为空");
    }

    /**
     *
     * @param temp
     * @return
     */
    private UserModel builderUser(User temp) {
        //实例化对象
        UserModel um = new UserModel();
        //分别赋值
        um.setUserName(temp.getUserName());
        um.setTrueName(temp.getTrueName());
        //加密
        um.setUserIdStr(UserIDBase64.encoderUserID(temp.getId()));
        //返回目标对象
        return um;
    }

    /**
     * 校验用户密码
     *
     * @param userPwd
     * @param userPwd1
     */
    private void checkLoginPwd(String userPwd, String userPwd1) {
        //由于数据库中的密码是加密的，故此，输入的密码加密和数据中的密码比较
        //加密
        userPwd = Md5Util.encode(userPwd);
        //比较
        AssertUtil.isTrue(!userPwd.equals(userPwd1), "输入的密码不正确");
    }


    /**
     * 1)userId 存在，user对象  登录
     *
     * 2)原始密码，非空，和密文密码一致
     *
     * 3）新密码，非空，不能和原始密码一致
     *
     * 4）确认密码 非空，确认密码和新密码一致
     *
     * 5）修改密码，加密
     *
     * 6）修改成功与否，<1;
     *
     *
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId,String oldPassword,String newPassword,String confirmPwd){
        //根据用户UserId查询对象
        User user = userMapper.selectByPrimaryKey(userId);
        //1.参数校验
        checkPwdParam(user,oldPassword,newPassword,confirmPwd);
        //2.修改密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //3.修改是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");
    }

    /**
     * 验证参数列表
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPwd
     */
    private void checkPwdParam(User user, String oldPassword, String newPassword, String confirmPwd) {
        //1,user是否存在
        AssertUtil.isTrue(null==user,"用户未登录或者不存在");
        //非空，和密文密码一致
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        //和密文密码一致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        //新密码非空，不能和原始密码一致
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        //不能和原始密码一致
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新和原始不能一致");
        //确认密码非空，新密码和确认密码一致
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        //新密码和确认密码一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPwd)),"新密码和确认密码必须一致");
    }

    //查询所有的销售人员的方法 等待调用
    public List<Map<String, Object>> findAllSales(){
        return userMapper.selectAllSales();
    }

    //查询用户三个条件模块
    public Map<String, Object> findUserByParam(UserQuery query) {
        //实例化Map
        Map<String, Object> map = new HashMap<String, Object>();
        //初始化分页数据
        PageHelper.startPage(query.getPage(), query.getLimit());
        //查询
        List<User> ulist = userMapper.selectByParams(query);
        //开始分页
        PageInfo<User> plist = new PageInfo<User>(ulist);
        //构建数据
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", plist.getTotal());
        map.put("data", plist.getList());
        //返回目标map
        return map;
    }

    //user添加用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //参数校验 检查用户参数
        checkUserParam(user.getUserName(),user.getEmail(),user.getPhone());
        //用户名唯一
        User temp = userMapper.selectUserByUserName(user.getUserName());
        AssertUtil.isTrue(temp != null, "用户已经存在");
        //设置默认值
        user.setIsValid(1);//开发状态
        user.setUpdateDate(new Date());//修改时间
        user.setCreateDate(new Date());//创建时间
        //密码默认加密 对表单的密码加密
        user.setUserPwd(Md5Util.encode("222"));
        //判断添加就结果
        //insertHasKey 查找自动增长的主键
        AssertUtil.isTrue(userMapper.insertHasKey(user)<1,"添加失败");
        //查找用户id
        System.out.println(user.getId()+"><<<");

        //用户id //角色id
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    //用户 //角色id 校验 批量添加
    private void relaionUserRole(Integer userId, String roleIds) {
        //统计当前用户拥有的角色
        Integer count=userRoleMapper.countUserRoleByUserId(userId);
        System.out.println(userId);
        //有角色就删除原有
        if(count>0){
            System.out.println(count);
            //删除当前用户的的所有角色信息
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"角色更改分配失败");
        }
        //非空 如果不为空 便可添加用户
        if(StringUtils.isNotBlank(roleIds)){
            //添加用户id -->角色id 创建中间表
            List<UserRole> ulist = new ArrayList<UserRole>();
            //insertBatch
            //动态添加 中间表
            String[] arrayIds = roleIds.split(",");
            //遍历
            for (String roleId:arrayIds) {
                //实例化对象
                UserRole ur=new UserRole();
                ur.setRoleId(Integer.parseInt(roleId));
                ur.setUserId(userId);
                ur.setCreateDate(new Date());
                ur.setUpdateDate(new Date());
                //存储到集合对象ulsit
                ulist.add(ur);
            }
            //判断添加
            System.out.println(ulist.size()+"<<<<<<<<<<<<<<<<<<<<<<<<<");
            AssertUtil.isTrue(userRoleMapper.insertBatch(ulist)!=ulist.size(),"角色分配失败");
        }
    }

    /**
     *
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParam(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"请输入用户名");
        //邮箱
        AssertUtil.isTrue(StringUtils.isBlank(email),"请输入邮箱");
        //手机号
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入手机号");
        //判断手机号是否合法
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机输错了傻蛋");

    }

    //修改校验
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeUser(User user) {
        //1. 参数校验
        // id  非空  记录必须存在
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp == null, "待修改的记录不存在");
        //用户名唯一
        //User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(user.getId())), "用户已经存在");
        //参数校验
        checkUserParam(user.getUserName(), user.getEmail(), user.getPhone());
        //2. 设置默认参数
        user.setUpdateDate(new Date());
        //3修改是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改失败了");
        /**/
        //修改中间表信息
        Integer userId = userMapper.selectUserByUserName(user.getUserName()).getId();
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    //删除校验
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeIds(Integer[] ids) {
        //length==0 未选中数据 或者没有值
        AssertUtil.isTrue(ids == null || ids.length == 0, "请选择删除数据");
        //删除
        AssertUtil.isTrue(userMapper.deleteBatch(ids) < 1, "删除失败了");

        for (Integer userId: ids) {
            //统计当前用户拥有的角色
            Integer count=userRoleMapper.countUserRoleByUserId(userId);
            if(count>0){
                //删除当前用户的的所有角色信息
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"角色分配失败");
            }
        }
    }
}
