package com.sfp.service.impl;

import com.sfp.entity.UserMsg;
import com.sfp.mapper.UserMsgMapper;
import com.sfp.service.UserMsgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yjm
 * @version 1.0.0
 * @description 用户信息
 * @createTime 2022/7/27 10:04
 */
@Service
public class UserMsgServiceImpl implements UserMsgService {
    @Resource
    private UserMsgMapper userMsgMapper;

    @Override
    public UserMsg getUserMsgByLoginName(String loginName) {
        return userMsgMapper.getUserMsgByLoginName(loginName);
    }

    @Override
    public UserMsg getUserMsgByUsername(String username) {
        return userMsgMapper.getUserMsgByUsername(username);
    }

    @Override
    public List<UserMsg> getAllUserMsg() {
        return userMsgMapper.getAllUserMsg();
    }

    @Override
    public boolean isExistByLoginName(String loginName) {
        return userMsgMapper.isExistByLoginName(loginName);
    }

    @Override
    public boolean isExistByUsername(String username) {
        return userMsgMapper.isExistByUsername(username);
    }

    @Override
    public int insertUser(UserMsg userMsg) {
        return userMsgMapper.insertUser(userMsg);
    }

    @Override
    public int updateUser(UserMsg userMsg) {
        return userMsgMapper.updateUser(userMsg);
    }

    @Override
    public int updatePassword(Integer id, String password) {
        return userMsgMapper.updatePassword(id, password);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMsgMapper.deleteUser(id);
    }
}
