package com.sfp.mapper;

import com.sfp.entity.UserMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMsgMapper {
    //通过登录名获得用户信息
    UserMsg getUserMsgByLoginName(@Param("loginName") String loginName);

    //通过登录名获得用户信息
    UserMsg getUserMsgByUsername(@Param("username") String username);

    //获取所有用户信息
    List<UserMsg> getAllUserMsg();

    //判断登录名是否存在
    boolean isExistByLoginName(@Param("loginName") String loginName);

    //判断用户名是否存在
    boolean isExistByUsername(@Param("username") String username);

    //插入一条用户信息
    int insertUser(@Param("userMsg") UserMsg userMsg);

    //更新用户信息(用户名，登录名，角色)
    int updateUser(@Param("userMsg") UserMsg userMsg);

    //修改密码
    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    //删除一条用户信息
    int deleteUser(@Param("id") Integer id);

}
