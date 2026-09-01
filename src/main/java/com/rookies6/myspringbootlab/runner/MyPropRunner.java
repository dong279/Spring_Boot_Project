package com.rookies6.myspringbootlab.runner;

import com.rookies6.myspringbootlab.property.MyPropProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyPropRunner implements ApplicationRunner {
    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    private Environment environment;

    @Autowired
    private MyPropProperties properties;

    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println(applicationName);
        logger.debug("application Name: " + applicationName);
        logger.info("name: " + properties.getUsername());
        logger.info("port: " + properties.getPort());
    }
}
