package com.furing.chatgroupservice.impl;

import com.furing.chatgroupservice.entity.bo.LoginBo;
import com.furing.chatgroupservice.entity.bo.UserLoginBo;
import com.furing.chatgroupservice.entity.po.User;
import com.furing.chatgroupservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void registerUser() {
        User user = new User();
        user.setAccount("10001");
        user.setUserName("函数");
        user.setPassword("10001");
        user.setCreatTime(LocalDate.now());
        userService.registerUser(user);
    }

    @Test
    void login() {

        LoginBo loginBo = new LoginBo();
        loginBo.setAccount("10001");
        loginBo.setPassword("10001");
        UserLoginBo login = userService.login(loginBo);
        System.out.println(login);
    }
}