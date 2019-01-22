package com.kangjh.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public class DBHistoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBHistoryTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testHistory() {
        activitiRule.getRepositoryService().createDeployment()
                .name("测试部署")
                .addClasspathResource("my-process.bpmn20.xml")
                .deploy();

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);

        activitiRule.getRuntimeService().setVariable(processInstance.getId(), "key1", "value1_1");

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        taskService.setOwner(task.getId(), "user1");

        taskService.createAttachment("url", task.getId(),
                task.getProcessInstanceId(), "name",
                "desc", "/url/test.png");

        taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note 1");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note 2");


        FormService formService = activitiRule.getFormService();

        Map<String, String> properties = Maps.newHashMap();
        properties.put("key2", "value2_1");
        properties.put("key3", "value3_1");
        formService.submitTaskFormData(task.getId(), properties);

    }

}
