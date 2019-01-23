package com.kangjh.activiti.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试
 *
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class HelloBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);

    public void sayHello() {
        LOGGER.info("--------------------");
        LOGGER.info("sayHello");
        LOGGER.info("--------------------");
    }

}
