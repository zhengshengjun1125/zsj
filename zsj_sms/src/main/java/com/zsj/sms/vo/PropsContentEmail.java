package com.zsj.sms.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-12-02 14:52:48
 */
@Data
public class PropsContentEmail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 邮件服务器
     */
    private String host;
    /**
     * 邮箱账号
     */
    private String username;
    /**
     * 邮箱认证码
     */
    private String smtp;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 接收人
     */
    private String receiver;
    /**
     * 是否为系统消息
     */
    private String isSystemMessage;

}
