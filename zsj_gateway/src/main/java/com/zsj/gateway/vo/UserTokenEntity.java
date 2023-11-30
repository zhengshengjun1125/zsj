package com.zsj.gateway.vo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统用户Token
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@Data
@NoArgsConstructor
public class UserTokenEntity {





    public UserTokenEntity(Long userId, String token, Date expireTime, Date updateTime) {
        this.userId = userId;
        this.token = token;
        this.expireTime = expireTime;
        this.updateTime = updateTime;
    }

    /**
     *
     */
    private Long userId;
    /**
     * token
     */
    private String token;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
