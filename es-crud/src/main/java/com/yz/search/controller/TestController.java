package com.yz.search.controller;

import com.yz.search.mq.TestMaxwellDataSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestMaxwellDataSender helloSender1;

    @RequestMapping("/abc")
    public Object sendMessage() {
        helloSender1.send();
        return null;
    }


}
