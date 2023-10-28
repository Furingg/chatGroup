package com.furing.chatgroup.service;

import com.furing.chatgroup.entity.bo.LoginBo;
import com.furing.chatgroup.entity.bo.UserLoginBo;
import com.furing.chatgroup.entity.po.User;
import org.springframework.stereotype.Service;

/**
 * @author furing
 */
@Service
public interface UserService {
    /**
     * 用户注册账号
     *
     * @param user 用户对象
     * @return 返回是否注册成功
     */
    Boolean registerUser(User user);

    /**
     * 用户登录
     *
     * @param loginBo 登录对象
     * @return 登录成功结果
     */
    UserLoginBo login(LoginBo loginBo);
}
