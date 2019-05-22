package com.example.springbootmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
    public final static String QUEUE_NAME = "log";
    public final static String QUEUE_NAME2 = "log.all";
    public final static String QUEUE_NAME3 = "log.all.error";
    public final static String EXCHANGE_NAME = "topicExchange"; //交换器名称
    @Bean
    public Queue topicQueue() {
        return new Queue(TopicConfig.QUEUE_NAME);
    }
    @Bean
    public Queue topicQueue2() {
        return new Queue(TopicConfig.QUEUE_NAME2);
    }
    @Bean
    public Queue topicQueue3() {
        return new Queue(TopicConfig.QUEUE_NAME3);
    }
    // 配置交换器
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TopicConfig.EXCHANGE_NAME);
    }
    // 绑定队列到交换器，并设置路由键（log.#）
    @Bean
    Binding bindingtopicExchangeQueue(Queue topicQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("log.#");
    }
    // 绑定队列到交换器，并设置路由键（log.*）
    @Bean
    Binding bindingtopicExchangeQueue2(Queue topicQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("log.*");
    }
    // 绑定队列到交换器，并设置路由键（log.*.error）
    @Bean
    Binding bindingtopicExchangeQueue3(Queue topicQueue3, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue3).to(topicExchange).with("log.*.error");
    }

}
