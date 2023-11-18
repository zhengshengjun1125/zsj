package com.zsj.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
@Configuration
@Slf4j
public class RabbitMQTemplateConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void initRabbitMQTemplate() {
        /*
             void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause);
         */
        /**
         * correlationData(可空)  当前消息的唯一关联数据(消息的唯一id)
         * ack  消息是否成功收到
         * cause(可空)  消息失败的原因
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            //消息应答和消息是否被消费没有关系 这个只是说消息进入了哪个队列而已
            log.info("消息应答机制confirm----correlationData:{},ack?:{},cause:{}", correlationData, ack, cause);
        });

        /*
         * ReturnsCallback的构造方法中是一个ReturnedMessage方法
         * 新版中 这里得到一个ReturnedMessage对象 属性就是上面注释中的属性
         * @message 投递失败的消息详细信息
         * @replyCode 回复的状态码
         * @replyText 回复的文本信息
         * @exchange 当时发给哪个交换机
         * @routingKey 当时消息的路由键
         * 只要消息没有被投递给指定的队列,就触发这个失败回调
         */
        rabbitTemplate.setReturnsCallback((returnedMessage) -> {
            log.error("消息失败回调fail message:{} " +
                            "replyCode:{} " +
                            "replyText:{} " +
                            "exchange:{}" +
                            "routingKey:{} ",
                    returnedMessage.getMessage(),
                    returnedMessage.getReplyCode(),
                    returnedMessage.getReplyText(),
                    returnedMessage.getExchange(),
                    returnedMessage.getRoutingKey());
        });

    }


}
