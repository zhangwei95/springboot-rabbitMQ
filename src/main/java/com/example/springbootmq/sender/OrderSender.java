package com.example.springbootmq.sender;


import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class OrderSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    public void topicSender(String message) throws UnsupportedEncodingException {
        rabbitTemplate.setExchange(env.getProperty("user.order.exchange.name"));
        rabbitTemplate.setRoutingKey(env.getProperty("user.order.routing.key.name"));
        rabbitTemplate.convertAndSend(MessageBuilder.withBody(message.getBytes("UTF-8")).build());
    }
}
