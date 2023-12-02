package com.zsj.sms.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://gitee.com/zhengshengjun">zsj</a>
 * @date 2023/12/2.
 */
@Component
public class JavaMailSenderUtil {

    //构建自定义的邮件发送对象
    public static JavaMailSender getJavaMailSender(String host, String username, String pass) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(pass);
        return javaMailSender;
    }
}
