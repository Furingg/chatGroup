package com.furing.chatgroupservice.controller;

import com.furing.chatgroupservice.entity.bo.LoginBo;
import com.furing.chatgroupservice.entity.bo.UserLoginBo;
import com.furing.chatgroupservice.entity.po.User;
import com.furing.chatgroupservice.service.UserService;
import com.furing.commons.result.CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult<UserLoginBo> login(@Validated @RequestBody LoginBo loginBo) {
        return CommonResult.operateSuccess(userService.login(loginBo));
    }
}
