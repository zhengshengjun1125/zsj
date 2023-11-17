package com.zsj.sms.service.impl;

import com.rabbitmq.client.Channel;
import com.zsj.common.utils.GlobalValueToExchange;
import com.zsj.common.vo.EmailVoProperties;
import com.zsj.sms.util.EmailContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.Query;

import com.zsj.sms.dao.EmailDao;
import com.zsj.sms.entity.EmailEntity;
import com.zsj.sms.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service("emailService")
@RabbitListener(queues = {GlobalValueToExchange.EMAIL_QUEUE})
@Slf4j
public class EmailServiceImpl extends ServiceImpl<EmailDao, EmailEntity> implements EmailService {


    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String sender;

    @RabbitHandler
    public void sendEmail(Message message, EmailVoProperties emailVoProperties, Channel channel) throws IOException {
        log.info("接收到的邮件信息为:{}", emailVoProperties);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();//当前消息的tag
        //发送邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.REGISTER_USER)) {
            //用户注册邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                //第二个参数是 whether to create a multipart message that supports alternative texts, inline
                //是否创建支持内联替代文本的多部分消息
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                //发送者
                helper.setFrom(sender);
                //接收者
                String to = emailVoProperties.getTo();
                helper.setTo(to);
                //邮件主题
                helper.setSubject("祝贺您 注册ZSJ博客系统成功!");
                //邮件内容
                helper.setText(EmailContentUtil.createRegisterUserEmailContent(to), true);
                mailSender.send(mimeMessage);
                log.info("发送给{}的邮件成功发送了~ 这是一封{}类型的邮件", to, emailVoProperties.getType());
                channel.basicAck(deliveryTag, false);//手动应答消息
            } catch (MessagingException e) {
                //出现异常 将消息回队
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EmailEntity> page = this.page(
                new Query<EmailEntity>().getPage(params),
                new QueryWrapper<EmailEntity>()
        );

        return new PageUtils(page);
    }

}