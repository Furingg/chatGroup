package com.furing.chatgroupservice.entity.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author furing
 */
@Data
public class LoginBo {
    @NotBlank(message = "账号不能为空")
    private String account;
    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean isRemember = false;

}
