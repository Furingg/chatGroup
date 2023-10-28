package com.furing.chatgroup.controller;

import com.furing.chatgroup.entity.bo.LoginBo;
import com.furing.chatgroup.entity.bo.UserLoginBo;
import com.furing.chatgroup.entity.po.User;
import com.furing.chatgroup.result.CommonResult;
import com.furing.chatgroup.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author furing
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/registerUser")
    public CommonResult<Void> registerUser(@Validated @RequestBody User user) {
        return CommonResult.autoResult(userService.registerUser(user));
    }

    @PostMapping("/login")
    public CommonResult<UserLoginBo> login(LoginBo loginBo) {
        return CommonResult.operateSuccess(userService.login(loginBo));
    }
}
