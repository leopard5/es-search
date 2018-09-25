package com.yz.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloSender1 {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String sendMsg = "{\"database\":\"kxtx-tc\",\"table\":\"tc_retrying\",\"type\":\"insert\",\"ts\":1537859460,\"xid\":1092,\"commit\":true,\"data\":{\"id\":3,\"batch_no\":\"1000\",\"error_code\":\"Champs\",\"error_msg\":\"Champs\",\"doing_type\":2,\"doing_type_name\":\"test\",\"final_result\":0,\"retry_count\":1,\"trade_scene_no\":\"test\",\"create_time\":\"2018-09-25 07:11:00\",\"create_user\":\"\",\"create_user_name\":\"\",\"update_time\":\"2018-09-25 07:11:00\",\"update_user\":\"\",\"update_user_name\":\"\",\"is_deleted\":0}}";
        System.out.println("Sender1 : " + sendMsg);
        this.rabbitTemplate.convertAndSend("maxwell", "kxtx-tc.tc_retrying", sendMsg);
    }
}
