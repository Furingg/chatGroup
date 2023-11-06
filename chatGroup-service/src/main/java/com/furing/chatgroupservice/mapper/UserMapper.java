package com.furing.chatgroupservice.mapper;

import com.furing.chatgroupservice.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author furing
 */
@Mapper
public interface UserMapper {

    /**
     * 注册账号
     * @param user 用户对象
     * @return 返回插入结果
     */
    Long registerUser(User user);

    /**
     * 通过账号找用户
     * @param account 账号
     * @return 返回结果
     */
    User selectUserByAccount(@Param("account") String account);
}
