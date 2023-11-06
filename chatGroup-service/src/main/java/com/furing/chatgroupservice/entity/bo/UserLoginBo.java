package com.furing.chatgroupservice.entity.bo;

import com.furing.chatgroupservice.entity.po.User;
import lombok.Data;

/**
 * @author furing
 */
@Data
public class UserLoginBo {
    /**
     * 用户
     */
    private User user;

    /**
     * token
     */
    private String token;
}
