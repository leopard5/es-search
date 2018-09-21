package com.yz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.amqp.core.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ESApplication implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(ESApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ESApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
    }
}
