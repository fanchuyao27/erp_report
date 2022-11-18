package com.sfp.controller;

import com.sfp.entity.UserMsg;
import com.sfp.entity.RespJson;
import com.sfp.service.UserMsgService;
import com.sfp.utils.MD5Tool;
import com.sfp.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserMsgService userMsgServiceImpl;

    private Result result;

    /**
     * 用户登录验证
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String loginName, String password){
        loginName = loginName.trim();
        password = password.trim();
        try {
            password = MD5Tool.md5(password, "UTF8");
            Subject subject = SecurityUtils.getSubject();
            if(!subject.isAuthenticated()){
                UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
                subject.login(token);
                subject.getSession().setAttribute("loginName", loginName);
            }
            return RespJson.createSuccess("success").toString();
        } catch (UnknownAccountException e) {
            log.error("未知的用户");
            return RespJson.createNoMatch("未知的用户").toString();
        } catch (IncorrectCredentialsException e){
            log.error("用户名或者密码错误");
            return RespJson.createNoMatch("用户名或者密码错误").toString();
        } catch (Exception e){
            log.error("登录失败");
            return RespJson.createError("登录失败").toString();
        }
    }

    /**
     * 用户管理
     */
    @RequestMapping(value = "/userManage", method = RequestMethod.POST)
    public String getUserMsg(String username){
        List<UserMsg> getUserMsgList = new ArrayList<>();
        try {
            if (username == null || username.equals("")) { //获取所有用户信息
                List<UserMsg> allUserMsgList = userMsgServiceImpl.getAllUserMsg();
                if (allUserMsgList == null || allUserMsgList.size() == 0) {
                    return new Result(1, "无用户数据", 0, allUserMsgList).toString();
                }
                getUserMsgList.addAll(allUserMsgList);
            } else { //查询用户信息
                UserMsg userMsg = userMsgServiceImpl.getUserMsgByUsername(username);
                if (userMsg == null) {
                    userMsg = userMsgServiceImpl.getUserMsgByLoginName(username);
                    if (userMsg == null) {
                        result = new Result(1, "未查到该用户", 0, userMsg);
                    }
                }
                getUserMsgList.add(userMsg);
            }
            result = new Result(0, "获取成功", getUserMsgList.size(), getUserMsgList);
        } catch (Exception e){
            log.error("数据异常，未能成功获取用户数据");
            result = new Result(1, "数据异常，未能成功获取数据");
        } finally {
            return result.toString();
        }
    }

    /**
     * 新建用户
     */
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String createUser(UserMsg userMsg) {
        Result result = new Result();
        try {
            //判断登录名、用户名是否存在，保证唯一性
            boolean validation = true;
            if (userMsg.getLoginName() != null && !userMsg.getLoginName().equals("") && userMsgServiceImpl.isExistByLoginName(userMsg.getLoginName())) {
                result = new Result(1, "登录名已存在");
                validation = false;
            }
            if (userMsg.getUsername() != null && !userMsg.getUsername().equals("") && userMsgServiceImpl.isExistByUsername(userMsg.getUsername())) {
                result = new Result(1, "用户名已存在");
                validation = false;
            }

            //验证通过后
            if(validation){
                //密码加密
                userMsg.setPassword(MD5Tool.md5(userMsg.getPassword(),"UTF-8"));
                int insertResult = userMsgServiceImpl.insertUser(userMsg);
                StringBuffer addFailMsg = new StringBuffer().append("用户").append(userMsg.getUsername()).append("添加失败");
                StringBuffer addSuccessMsg = new StringBuffer().append("用户").append(userMsg.getUsername()).append("添加成功");
                if (insertResult == 0) {
                    log.error(addFailMsg.toString());
                    result = new Result(1, addFailMsg.toString());
                } else {
                    result = new Result(0, addSuccessMsg.toString());
                }
            }
        } catch (Exception e){
            log.error("数据异常，未能成功获取用户数据");
            result = new Result(1, "数据异常，未能成功获取数据");
        } finally {
            return result.toString();
        }
    }

    /**
     * 编辑用户
     */
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public String editUser(UserMsg userMsg){
        try {
            int editResult = userMsgServiceImpl.updateUser(userMsg);
            if (editResult == 0) {
                log.error("数据异常，修改用户信息失败");
                result = new Result(1, "数据异常，修改失败");
            } else {
                result = new Result(0, "修改成功");
            }
        } catch (Exception e) {
            log.error("数据异常，修改用户信息失败");
            result = new Result(1, "数据异常，修改失败");
        } finally {
            return result.toString();
        }
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(UserMsg userMsg) {
        StringBuffer deleteFailMsg = new StringBuffer().append("数据异常，用户").append(userMsg.getUsername()).append("删除失败");
        StringBuffer deleteSuccessMsg = new StringBuffer().append("用户").append(userMsg.getUsername()).append("删除成功");
        try {
            int deleteResult = userMsgServiceImpl.deleteUser(userMsg.getId());
            if (deleteResult == 0) {
                log.error(deleteFailMsg.toString());
                result = new Result(1, deleteFailMsg.toString());
            } else {
                result = new Result(0, deleteSuccessMsg.toString());
            }
        } catch (Exception e) {
            log.error(deleteFailMsg.toString());
            result = new Result(1, deleteFailMsg.toString());
        } finally {
            return result.toString();
        }
    }

    /**
     * 密码重置
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(UserMsg userMsg){
        StringBuffer resultMsg = new StringBuffer().append(userMsg.getUsername());
        try {
            //初始密码“123456”进行加密
            String initialPassword = MD5Tool.md5("123456", "UTF8");
            //插入数据库
            int resetResult = userMsgServiceImpl.updatePassword(userMsg.getId(), initialPassword);
            if (resetResult == 0) {
                log.error(resultMsg.append("密码重置失败").toString());
                result = new Result(1, resultMsg.append("密码重置失败").toString());
            } else {
                result = new Result(0, resultMsg.append("密码重置成功").toString());
            }
        } catch (Exception e) {
            log.error(resultMsg.append("密码重置失败").toString());
            result = new Result(1, resultMsg.append("密码重置失败").toString());
        } finally {
            return result.toString();
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(int id, String username, String password, String oldPassword, String newPassword){
        StringBuffer resultMsg = new StringBuffer().append(username);
        //输入的旧密码加密
        oldPassword = MD5Tool.md5(oldPassword, "UTF8");
        try {
            if (!password.equals(oldPassword)) { //判断旧密码与数据库中的密码是否一致
                result = new Result(1, "旧密码错误，请输入正确密码");
            } else {
                newPassword = MD5Tool.md5(newPassword, "UTF8");
                int updateResult = userMsgServiceImpl.updatePassword(id, newPassword);
                if (updateResult == 0) {
                    log.error(resultMsg.append("密码修改失败").toString());
                    result = new Result(1, resultMsg.append("密码修改失败").toString());
                } else {
                    result = new Result(0, resultMsg.append("密码修改成功").toString());
                }
            }
        } catch (Exception e) {
            log.error(resultMsg.append("密码修改失败").toString());
            result = new Result(1, resultMsg.append("密码修改失败").toString());
        } finally {
            return result.toString();
        }
    }
}
