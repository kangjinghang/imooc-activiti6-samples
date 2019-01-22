package com.kangjh.activiti.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public class MyTaskListener implements TaskListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {

        String eventName = delegateTask.getEventName();
        if (StringUtils.endsWith("create", eventName)) {
            LOGGER.info("config by listener");

            delegateTask.addCandidateUsers(Arrays.asList("user1", "user2"));
            delegateTask.addCandidateGroup("group1");
            delegateTask.setVariable("key1", "value1");

            delegateTask.setDueDate(DateTime.now().plusDays(3).toDate());
        } else if (StringUtils.endsWith("complete", eventName)) {
            LOGGER.info("task complete");
        }
    }
}
