package com.kangjh.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public class DBRuntimeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBRuntimeTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testRuntime() {
        activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");

        ProcessInstance second_approve = activitiRule.getRuntimeService().startProcessInstanceByKey("second_process", variables);

    }

    @Test
    public void testSetOwner() {

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("second_process").singleResult();
        taskService.setOwner(task.getId(), "user1");

    }

    @Test
    public void testMessage() {

        activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("my-process-message.bpmn20.xml")
                .deploy();

    }

    @Test
    public void testMessageReceived() {

        activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("my-process-message-received.bpmn20.xml")
                .deploy();

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

    }

    @Test
    public void testJob() throws InterruptedException {

        activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("my-process-job.bpmn20.xml")
                .deploy();

        TimeUnit.SECONDS.sleep(30);
    }


}
