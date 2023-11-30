package com.zsj.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
@Data
@NoArgsConstructor
public class EmailVoProperties implements Serializable {

    //用户登录邮件类型
    public static final String LOGIN_USER = "LOGIN_USER";

    //用户注册邮件类型
    public static final String REGISTER_USER = "REGISTER_USER";

    //用户消费短信
    public static final String CONSUMPTION = "CONSUMPTION";

    //充值
    public static final String RECHARGE = "RECHARGE";

    //余额不足
    public static final String INSUFFICIENT_BALANCE = "INSUFFICIENT_BALANCE";

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

    //消费金额
    private Double consumption;

    //充值金额
    private Double recharge;

    //发生时间
    private String time;


    //单参数构造  默认系统信息
    public EmailVoProperties(String to) {
        this.type = REGISTER_USER;
        this.to = to;
        this.IS_SYSTEM_MESSAGE = true;
    }

    //专属金额方法
    public EmailVoProperties(String to, String type, Double price, String name, String time) {
        if (type.equals(EmailVoProperties.CONSUMPTION)) {//消费事件
            this.type = EmailVoProperties.CONSUMPTION;
            this.consumption = price;
        } else if (type.equals(EmailVoProperties.RECHARGE)){//充值事件
            this.type = EmailVoProperties.RECHARGE;
            this.recharge = price;
        }
        this.time = time;
        this.username = name;
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

}
