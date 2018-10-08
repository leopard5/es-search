package com.yz.search.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestMaxwellDataSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;


    public void send() {
        String sendMsg = "{\"database\":\"kxtx-tc\",\"table\":\"tc_retrying\",\"type\":\"insert\",\"ts\":1537859460,\"xid\":1092,\"commit\":true,\"data\":{\"id\":3,\"batch_no\":\"1000\",\"error_code\":\"Champs\"}}";
        System.out.println("Sender1 : " + sendMsg);
        this.rabbitTemplate.convertAndSend("maxwell", "kxtx-tc.tc_retrying", sendMsg);
    }
}
