package com.example.springbootmq.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TestRest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    private LoadingCache<Long, AtomicLong> counter= CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.DAYS)
            .build(new CacheLoader<Long, AtomicLong>() {
                @Override
                public AtomicLong load(Long aLong) throws Exception {
                    return new AtomicLong(0);
                }
            });
    private int limit=2;
    private int unit = 1000 * 60 * 60 *24;

    @RequestMapping(value = "/order/add",method = RequestMethod.GET)
    public String orderCreate() throws ExecutionException, UnsupportedEncodingException {
        //漏桶
        Long current = System.currentTimeMillis()/unit;
        //当天第几笔订单
        long l = counter.get(current).incrementAndGet();
        String message="第"+l+"笔订单";
        rabbitTemplate.setExchange(env.getProperty("user.order.exchange.name"));
        rabbitTemplate.setRoutingKey(env.getProperty("user.order.routing.key.name"));
        rabbitTemplate.convertAndSend(MessageBuilder.withBody(message.getBytes("UTF-8")).build());
        return message;
    }
}
