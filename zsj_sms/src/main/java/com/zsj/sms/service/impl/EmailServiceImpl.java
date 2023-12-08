package com.zsj.sms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbitmq.client.Channel;
import com.zsj.common.utils.*;
import com.zsj.common.vo.EmailVoProperties;
import com.zsj.sms.util.EmailContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    private EmailDao emailDao;


    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String sender;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void sendEmail(Message message, EmailVoProperties emailVoProperties, Channel channel) throws IOException {
        log.info("接收到的邮件信息为:{}", emailVoProperties);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();//当前消息的tag
        //发送注册成功邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.REGISTER_USER)) {
            //用户注册邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailEntity entity = new EmailEntity();
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
                String title = "祝贺您 注册ZSJ博客系统成功!";
                helper.setSubject(title);
                //邮件内容
                String content = EmailContentUtil.createRegisterUserEmailContent(to, emailVoProperties.getUsername(), emailVoProperties.getRoleName());
                send(emailVoProperties, channel, deliveryTag, mimeMessage, entity, helper, to, title, content);
            } catch (MessagingException e) {
                //出现异常 将消息回队
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            } finally {
                this.save(entity);
            }
        }
        //发送验证码邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.LOGIN_USER)) {
            //存放email邮件code 其中key是他的邮箱号码+emailcode  value 就是我们生成的uuid五位
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String value = Encrypt.encrypt_uuid_6().toUpperCase();
            ops.set(emailVoProperties.getTo().toUpperCase() + "emailCode", value, 120, TimeUnit.SECONDS);//设置2分钟的过期时间
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailEntity entity = new EmailEntity();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                //发送者
                helper.setFrom(sender);
                //接收者
                String to = emailVoProperties.getTo();
                helper.setTo(to);
                //邮件主题
                String title = "zsj博客登录的验证码";
                helper.setSubject(title);
                //邮件内容
                String emailContent = EmailContentUtil.createLoginUserEmailContent(value);
                send(emailVoProperties, channel, deliveryTag, mimeMessage, entity, helper, to, title, emailContent);
            } catch (MessagingException e) {
                //出现异常 将消息回队
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            } finally {
                this.save(entity);
            }
        }
        //发送消费邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.CONSUMPTION)) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailEntity entity = new EmailEntity();
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
                String title = "ZSJ_BlOG消费提醒";
                helper.setSubject(title);
                //邮件内容
                String content = EmailContentUtil
                        .createConsumptionEmailContent(emailVoProperties.getUsername(),
                                emailVoProperties.getConsumption(),
                                emailVoProperties.getTime());
                send(emailVoProperties, channel, deliveryTag, mimeMessage, entity, helper, to, title, content);
            } catch (MessagingException e) {
                //出现异常 将消息回队
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            } finally {
                this.save(entity);
            }
        }
        //发送充值邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.RECHARGE)) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailEntity entity = new EmailEntity();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(sender);
                String to = emailVoProperties.getTo();
                helper.setTo(to);
                String title = "ZSJ_BlOG充值提醒";
                helper.setSubject(title);
                String content = EmailContentUtil
                        .createRechargeEmailContent(emailVoProperties.getUsername(),
                                emailVoProperties.getRecharge(),
                                emailVoProperties.getTime());
                send(emailVoProperties, channel, deliveryTag, mimeMessage, entity, helper, to, title, content);
            } catch (MessagingException e) {
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            } finally {
                this.save(entity);
            }
        }
        //余额不足邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.INSUFFICIENT_BALANCE)) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailEntity entity = new EmailEntity();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(sender);
                String to = emailVoProperties.getTo();
                helper.setTo(to);
                String title = "ZSJ_BlOG余额提醒";
                helper.setSubject(title);
                String content = EmailContentUtil.createInsufficientBalanceEmailContent();
                send(emailVoProperties, channel, deliveryTag, mimeMessage, entity, helper, to, title, content);
            } catch (MessagingException e) {
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            } finally {
                this.save(entity);
            }
        }
        //普通活动邮件
        if (emailVoProperties.getType().equals(EmailVoProperties.ACTIVITY)) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            EmailEntity entity = new EmailEntity();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(sender);
                String to = emailVoProperties.getTo();
                helper.setTo(to);
                String title = "ZSJ_BlOG系统推送";
                helper.setSubject(title);
                String content = emailVoProperties.getContent();
                send(emailVoProperties, channel, deliveryTag, mimeMessage, entity, helper, to, title, content);
            } catch (MessagingException e) {
                channel.basicReject(deliveryTag, true);
                throw new RuntimeException(e);
            } finally {
                this.save(entity);
            }
        }
    }

    private void send(EmailVoProperties emailVoProperties, Channel channel, long deliveryTag, MimeMessage mimeMessage, EmailEntity entity, MimeMessageHelper helper, String to, String title, String emailContent) throws MessagingException, IOException {
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
        log.info("发送给{}的邮件成功发送了~ 这是一封{}类型的邮件", to, emailVoProperties.getType());
        channel.basicAck(deliveryTag, false);//手动应答消息
        entity.setContent(emailContent);
        entity.setSender(sender);
        entity.setRecipient(to);
        entity.setIsSystem(1);
        entity.setTitle(title);
        entity.setCreateTime(new Date(System.currentTimeMillis()));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EmailEntity> page = this.page(
                new Query<EmailEntity>().getPage(params),
                new QueryWrapper<EmailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IPage<EmailEntity> pageByCondition(int cur, int size, EmailEntity entity) {
        Page<EmailEntity> page = new Page<>(cur, size);
        QueryWrapper<EmailEntity> queryWrapper = new QueryWrapper<>();
        String base = entity.getSender();
        String title = entity.getTitle();
        String recipient = entity.getRecipient();
        Integer isSystem = entity.getIsSystem();
        String id = entity.getId();
        if (ObjectUtil.isNotNullOrEmpty(id)) {
            queryWrapper.eq("id", id);
        }
        if (ObjectUtil.isNotNullOrEmpty(base)) {
            queryWrapper.like("sender", base);
        }
        if (ObjectUtil.isNotNullOrEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (ObjectUtil.isNotNullOrEmpty(recipient)) {
            queryWrapper.like("recipient", recipient);
        }
        if (ObjectUtil.objectIsNotNull(isSystem)) {
            queryWrapper.eq("is_system", isSystem);
        }
        queryWrapper.eq("deleted", 0);//未被删除的邮件
        queryWrapper.orderByDesc("create_time");
        return emailDao.selectPage(page, queryWrapper);
    }

}