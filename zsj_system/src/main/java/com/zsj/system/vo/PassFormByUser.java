package com.zsj.system.vo;

import lombok.Data;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/16.
 * 用于验证用户的密码环节
 */
@Data
public class PassFormByUser {


    /**
     * 密码
     */
    private String oldPassword;
    /**
     * 密码
     */
    private String newPassword;
}
