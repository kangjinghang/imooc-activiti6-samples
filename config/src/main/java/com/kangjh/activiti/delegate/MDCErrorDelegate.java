package com.kangjh.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class MDCErrorDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCErrorDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("run MDCErrorDelegate");
        throw new RuntimeException("this is a test");
    }

}
