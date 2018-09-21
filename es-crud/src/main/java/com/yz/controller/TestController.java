package com.yz.controller;

import com.yz.mq.HelloSender1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private HelloSender1 helloSender1;

    public Object sendMessage(){
        helloSender1.send();
        return null;
    }

}
