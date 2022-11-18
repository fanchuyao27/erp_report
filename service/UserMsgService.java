package com.sfp.service;

import com.sfp.entity.UserMsg;

import java.util.List;

/**
 * @author yjm
 * @version 1.0.0
 * @description 用户信息
 * @createTime 2022/7/27 10:04
 */
public interface UserMsgService {
    //通过登录名获得用户信息
    UserMsg getUserMsgByLoginName(String loginName);

    //通过登录名获得用户信息
    UserMsg getUserMsgByUsername(String username);

    //获取所有用户信息
    List<UserMsg> getAllUserMsg();

    //判断登录名是否存在
    boolean isExistByLoginName(String loginName);

    //判断用户名是否存在
    boolean isExistByUsername(String username);

    //插入一条用户信息
    int insertUser(UserMsg userMsg);

    //更新用户信息(用户名，登录名，角色)
    int updateUser(UserMsg userMsg);

    //修改密码
    int updatePassword(Integer id, String password);

    //删除一条用户信息
    int deleteUser(Integer id);
}
