package com.yz.search.mq;

import com.alibaba.fastjson.JSON;
import com.yz.search.handle.ChangeDataPushES;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "test")
public class ReceiveMaxwellMessage {
    @Autowired
    private ChangeDataPushES changeDataPushES;

    @RabbitHandler
    public void process(@Payload String message) {
        System.out.println("maxwell Receiver1  : " + JSON.toJSONString(message));
        changeDataPushES.pushData2ES(message);
    }
}
