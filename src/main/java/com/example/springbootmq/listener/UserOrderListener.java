package com.example.springbootmq.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component("userOrderListener")
public class UserOrderListener implements ChannelAwareMessageListener {

    private static final Logger log = LoggerFactory.getLogger(UserOrderListener.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        byte[] body = message.getBody();
        try {
            if(System.currentTimeMillis()%2==1){
                log.info("订单消息监听 {}",new String(body,"UTF-8"));
                //消息确认
                //Tag 可以看作消息的编号，它是一个64 位的长整型值；multiple一般设为false，
                // 如果设为true则表示确认当前deliveryTag 编号及之前所有未被当前消费者确认的消息
                channel.basicAck(tag,false);
            } else {
                throw new Exception("随机处理失败");
            }

        } catch (Exception e){
            log.info("订单处理异常 {}",new String(body,"UTF-8"));
            //消息拒绝
            //multiple一般设为false，如果设为true则表示拒绝当前deliveryTag 编号及之前所有未被当前消费者确认的消息。
            // requeue参数表示是否重回队列，如果requeue 参数设置为true ，则RabbitMQ 会重新将这条消息存入队列尾部（注意是队列尾部），
            // 等待继续投递给订阅该队列的消费者，当然也可能是自己；如果requeue 参数设置为false ，则RabbitMQ立即会把消息从队列中移除，
            // 而不会把它发送给新的消费者
            channel.basicRecover(true);

            //channel.basicReject(deliveryTag, true);
            //        basic.reject方法拒绝deliveryTag对应的消息，第二个参数是否requeue，true则重新入队列，否则丢弃或者进入死信队列。
            //
            //该方法reject后，该消费者还是会消费到该条被reject的消息。
            //
            //channel.basicNack(deliveryTag, false, true);
            //        basic.nack方法为不确认deliveryTag对应的消息，第二个参数是否应用于多消息，
            // 第三个参数是否requeue，与basic.reject区别就是同时支持多个消息，可以nack该消费者先前接收未ack的所有消息。nack后的消息也会被自己消费到。
            //
            //channel.basicRecover(true);
            //        basic.recover是否恢复消息到队列，参数是是否requeue，true则重新入队列，
            // 并且尽可能的将之前recover的消息投递给其他消费者消费，而不是自己再次消费。false则消息会重新被投递给自己。

        }

    }
}
