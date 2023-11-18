package com.zsj.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
@Data
public class EmailVoProperties implements Serializable {

    //用户登录邮件类型
    public static final String LOGIN_USER = "LOGIN_USER";

    //用户注册邮件类型
    public static final String REGISTER_USER = "REGISTER_USER";

    //活动推送邮件类型
    public static final String ACTIVITY = "ACTIVITY";


    //邮件类型
    private String type;

    //接收人
    private String to;

    //是否为系统信息
    private Boolean IS_SYSTEM_MESSAGE;

    //用户名称
    private String username;

    //角色名称
    private String roleName;

    //全参构造
    public EmailVoProperties(String type, String to, Boolean IS_SYSTEM_MESSAGE) {
        this.type = type;
        this.to = to;
        this.IS_SYSTEM_MESSAGE = IS_SYSTEM_MESSAGE;
    }

    //单参数构造  默认系统信息
    public EmailVoProperties(String to) {
        this.type = REGISTER_USER;
        this.to = to;
        this.IS_SYSTEM_MESSAGE = true;
    }


    public EmailVoProperties(String type, String to) {
        this.type = type;
        this.to = to;
        this.IS_SYSTEM_MESSAGE = true;
    }

    public EmailVoProperties(String to, String username, String roleName) {
        this.type = REGISTER_USER;
        this.to = to;
        this.IS_SYSTEM_MESSAGE = true;
        this.username = username;
        this.roleName = roleName;
    }

    public EmailVoProperties() {
    }
}
