package com.example.springbootmq;

import com.example.springbootmq.sender.FanoutSender;
import com.example.springbootmq.sender.Sender;
import com.example.springbootmq.sender.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMqApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Autowired
    private Sender sender;

    @Test
    public void driectTest() {
        for(int i=0;i<=10000;i++){
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            sender.driectSend("Driect Data：" + sf.format(new Date())+i);
        }
    }

    @Autowired
    private FanoutSender fanoutSender;


    @Test
    public void fanoutTest() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        fanoutSender.send("fanout1 Data：" + sf.format(new Date()));
        fanoutSender.send2("fanout2 Data：" + sf.format(new Date()));
    }

    @Autowired
    private TopicSender topicSender;
    @Test
    public void Test() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        topicSender.topicSender("Time1 => " + sf.format(new Date()));
    }

}
