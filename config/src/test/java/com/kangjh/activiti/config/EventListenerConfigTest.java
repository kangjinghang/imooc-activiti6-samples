package com.kangjh.activiti.config;

import com.kangjh.activiti.envent.CustomEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 活动监听器配置
 *
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class EventListenerConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerConfigTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventListener.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());
        activitiRule.getRuntimeService().addEventListener(new CustomEventListener());
        activitiRule.getRuntimeService().dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));
    }


}
