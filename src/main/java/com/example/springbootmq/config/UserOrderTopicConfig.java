package com.example.springbootmq.config;


import com.example.springbootmq.listener.UserOrderListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class UserOrderTopicConfig {

    @Autowired
    private Environment env;

//    public final static String QUEUE_NAME = "orderQueue";

    public final static String EXCHANGE_NAME = "orderTopicExchange"; //交换器名称

    @Bean
    public Queue userOrderQueue() {
        return new Queue(env.getProperty("user.order.queue.name"));
    }

    // 配置交换器
    @Bean
    public TopicExchange userOrderExchange() {
        return new TopicExchange(env.getProperty("user.order.exchange.name"));
    }

    // 绑定队列到交换器，并设置路由键（log.#）
    @Bean
    public Binding userOrderBinder(Queue userOrderQueue, TopicExchange userOrderExchange) {
        return BindingBuilder.bind(userOrderQueue).to(userOrderExchange).with(env.getProperty("user.order.routing.key.name"));
    }

    @Autowired
    private UserOrderListener userOrderListener;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public SimpleMessageListenerContainer listenerContainerUserOrder(@Qualifier("userOrderQueue") Queue userOrderQueue){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.setMessageConverter(new Jackson2JsonMessageConverter());

        //并发配置
        container.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.concurrency",Integer.class));
        container.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.max-concurrency",Integer.class));
        container.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.prefetch",Integer.class));

        //消息确认
        container.setQueues(userOrderQueue);
        container.setMessageListener(userOrderListener);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        return container;
    }

}
