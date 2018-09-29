package com.yz.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ESApplication {
    private static final Logger logger = LogManager.getLogger(ESApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ESApplication.class, args);
    }
}
