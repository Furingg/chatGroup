package com.furing.chatgroupservice.entity.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author furing
 */
@Data
@Accessors
public class Group {

    /**
     * 群聊id
     */
    private Long groupId;

    /**
     * 群聊名
     */
    private String groupName;

    /**
     * 群聊人数
     */
    private Long number;


}
