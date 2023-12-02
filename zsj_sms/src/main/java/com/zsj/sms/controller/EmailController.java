package com.zsj.sms.controller;

import java.util.Date;
import java.util.UUID;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zsj.common.utils.GlobalValueToExchange;
import com.zsj.common.utils.ObjectUtil;
import com.zsj.common.vo.BalanceVoProperties;
import com.zsj.common.vo.EmailVoProperties;
import com.zsj.common.vo.User;
import com.zsj.sms.feign.SystemFeign;
import com.zsj.sms.util.JavaMailSenderUtil;
import com.zsj.sms.util.MarkdownUtil;
import com.zsj.sms.vo.PropsContentEmail;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import com.zsj.sms.entity.EmailEntity;
import com.zsj.sms.service.EmailService;
import com.zsj.common.utils.R;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-17 15:37:42
 */
@RestController
@RequestMapping("sms/email")
public class EmailController {
    @Autowired
    private EmailService emailService;


    @Autowired
    @Lazy
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private SystemFeign systemFeign;

    @PostMapping("/list/{cur}/{size}")
    public R pageListInfo(@PathVariable("cur") int cur,
                          @PathVariable("size") int size,
                          @NotNull @RequestBody EmailEntity entity) {
        IPage<EmailEntity> ans = emailService.pageByCondition(cur, size, entity);
        return R.ok("success").put("data", ans);
    }


    //自定义发送邮件信息
    @PostMapping("/sendEmail")
    public R sendEmail(@NotNull @RequestHeader("system_api_Authorize_name") String sender,
                       @NotNull @RequestBody PropsContentEmail content) {
        double z = content.getContent().length();
        if (checkContent(content)) {
            String text = MarkdownUtil.markdownToHtmlExtensions(content.getContent());
            if (content.getIsSystemMessage().equals("true")) {
                z = z * 2;
                if (isInsufficient(sender, z, content.getUsername())) {
                    return R.error("余额不足请充值");
                }
                EmailVoProperties emailVoProperties = new EmailVoProperties(EmailVoProperties.ACTIVITY, content.getReceiver(), text, true);
                rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE, emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
                rabbitTemplate.convertAndSend(GlobalValueToExchange.DEDUCT_EXCHANGE, GlobalValueToExchange.DEDUCT_QUEUE, new BalanceVoProperties(z, sender), new CorrelationData(UUID.randomUUID().toString()));
            } else {
                if (isInsufficient(sender, z, content.getUsername())) {
                    return R.error("余额不足请充值");
                }
                if (checkContentSelf(content)) {
                    JavaMailSender javaMailSender = JavaMailSenderUtil.getJavaMailSender(content.getHost(), content.getUsername(), content.getSmtp());
                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                    EmailEntity entity = new EmailEntity();
                    try {
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom(content.getUsername());
                        //接收者
                        helper.setTo(content.getReceiver());
                        //邮件主题
                        String title = sender + "给您发的邮件";
                        entity.setTitle(title);
                        helper.setSubject(title);
                        helper.setText(text, true);
                        javaMailSender.send(mimeMessage);
                        rabbitTemplate.convertAndSend(GlobalValueToExchange.DEDUCT_EXCHANGE, GlobalValueToExchange.DEDUCT_QUEUE, new BalanceVoProperties(z, sender), new CorrelationData(UUID.randomUUID().toString()));
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }finally {
                        //自定义消息手动保存一下
                        entity.setContent(text);
                        entity.setSender(content.getUsername());
                        entity.setRecipient(content.getReceiver());
                        entity.setIsSystem(0);
                        entity.setCreateTime(new Date(System.currentTimeMillis()));
                        emailService.save(entity);
                    }
                } else return R.error("请配置邮件属性");
            }
            return R.ok("发送成功");
        }
        return R.error("发送失败");
    }

    private boolean checkContentSelf(PropsContentEmail content) {
        return ObjectUtil.isNotNullOrEmpty(content.getHost()) &&
                ObjectUtil.isNotNullOrEmpty(content.getUsername()) &&
                ObjectUtil.isNotNullOrEmpty(content.getSmtp());
    }


    private boolean isInsufficient(String sender, double z, String username) {
        R balance = systemFeign.getCurUserBalance(new User(sender));
        double curB = (double) balance.get("data");
        if (curB < z) {
            //发送邮件提醒余额不足
            EmailVoProperties emailVoProperties =
                    new EmailVoProperties(EmailVoProperties.INSUFFICIENT_BALANCE, username);
            rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE,
                    emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
            return true;
        }
        return false;
    }

    private boolean checkContent(PropsContentEmail content) {
        return ObjectUtil.isNotNullOrEmpty(content.getContent()) &&
                ObjectUtil.isNotNullOrEmpty(content.getReceiver()) &&
                ObjectUtil.isNotNullOrEmpty(content.getIsSystemMessage());
    }

}
