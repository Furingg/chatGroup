package com.furing.chatgroupservice.service.impl;

import com.furing.chatgroupservice.entity.bo.LoginBo;
import com.furing.chatgroupservice.entity.bo.UserLoginBo;
import com.furing.chatgroupservice.entity.po.User;
import com.furing.chatgroupservice.mapper.UserMapper;
import com.furing.chatgroupservice.service.UserService;
import com.furing.commons.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author furing
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Boolean registerUser(User user) {
        return 1 == userMapper.registerUser(user);
    }

    @Override
    public UserLoginBo login(LoginBo loginBo) {
        String account = loginBo.getAccount();
        String password = loginBo.getPassword();
        User user = userMapper.selectUserByAccount(account);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getPassword().equals(password), "登录失败，密码错误");

        UserLoginBo userLoginBo = new UserLoginBo();
        userLoginBo.setUser(user);
        String token = JwtTools.createToken(user.getUserId());
        if (redisUtil.set(token, user.getUserId(), TokenConstant.EXPIRE_TIME)) {
            userLoginBo.setToken(token);
            return userLoginBo;
        }
        return null;
    }
}
