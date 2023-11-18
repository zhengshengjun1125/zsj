package com.zsj.sms;

import com.zsj.common.utils.GlobalValueToExchange;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZsjSmsApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;


    @Test
    void create() {
        //String name, boolean durable, boolean autoDelete
        DirectExchange directExchange =
                new DirectExchange(GlobalValueToExchange.EMAIL_EXCHANGE, true, false);
        amqpAdmin.declareExchange(directExchange);
        //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete)
        Queue queue = new Queue(GlobalValueToExchange.EMAIL_QUEUE,true,false,false);
        amqpAdmin.declareQueue(queue);
        //(String destination, DestinationType destinationType, String exchange, String routingKey,
        Binding binding = new Binding(GlobalValueToExchange.EMAIL_QUEUE,
                Binding.DestinationType.QUEUE,
                GlobalValueToExchange.EMAIL_EXCHANGE,GlobalValueToExchange.EMAIL_QUEUE,null);
        amqpAdmin.declareBinding(binding);
    }


    @Test
    void dos(){
        System.out.println("111aacc23f5".toUpperCase());
    }

}
