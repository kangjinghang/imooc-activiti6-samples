package com.kangjh.activiti.bpmn2;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public class UserTaskTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-usertask.bpmn20.xml"})
    public void testUserTask() {

        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        LOGGER.info("find by user1 task = {}", task);

        task = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        LOGGER.info("find by user2 task = {}", task);

        task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        LOGGER.info("find by group1 task = {}", task);

        taskService.claim(task.getId(), "user2");
//        taskService.setAssignee(task.getId(), "user2");
        LOGGER.info("claim task.id = {} by user2", task.getId());

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        LOGGER.info("find by user1 task = {}", task);

        task = taskService.createTaskQuery().taskCandidateOrAssigned("user2").singleResult();
        LOGGER.info("find by user2 task = {}", task);
    }

    @Test
    @Deployment(resources = {"my-process-usertask2.bpmn20.xml"})
    public void testUserTask2() {

        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        LOGGER.info("find by user1 task = {}", task);

        taskService.complete(task.getId());
    }
}
