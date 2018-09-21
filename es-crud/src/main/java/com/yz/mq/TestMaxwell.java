package com.yz.mq;

import com.alibaba.fastjson.JSON;
import com.yz.dto.MaxwellDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "test")
public class TestMaxwell {

    @RabbitHandler
    public void process(MaxwellDTO message) {
        System.out.println("maxwell Receiver1  : " + JSON.toJSONString(message));
    }
}
