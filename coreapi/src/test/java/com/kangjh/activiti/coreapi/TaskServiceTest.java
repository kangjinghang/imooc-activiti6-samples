package com.kangjh.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class TaskServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        runtimeService.startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().singleResult();

        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.setVariable(task.getId(), "key1", "value1");
        //作用域为当前task
        taskService.setVariableLocal(task.getId(), "localKey1", "localValue1");

        //3种获取流程变量的方式
        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());

        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());

        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());

        LOGGER.info("taskServiceVariables = {}", taskServiceVariables);
        LOGGER.info("taskServiceVariablesLocal = {}", taskServiceVariablesLocal);
        LOGGER.info("processVariables = {}", processVariables);

        Map<String, Object> vars = Maps.newHashMap();
        vars.put("cKey1", "cValue1");
        taskService.complete(task.getId(), vars);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("task1 = {}", task1);
    }


    //claim发现task不是当前传入的人，抛出异常
    //setAssignee强制替换某人的权限

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        runtimeService.startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().singleResult();

        LOGGER.info("task = {}", task);
        LOGGER.info("task.description = {}", task.getDescription());

        //通常是任务发起人
        taskService.setOwner(task.getId(), "user1");
        //任务待办人,不建议使用
//        taskService.setAssignee(task.getId(),"admin");

        //在候选人列表里且未指定待办人的任务
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("admin").taskUnassigned().list();
        for (Task task1 : taskList) {
            try {
                taskService.claim(task1.getId(), "admin");
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
            }
        }

        //type=assignee和type=owner并不是从数据库获取的,而是从command命令解析出来的待办人和拥有人
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            LOGGER.info("identityLink = {}", identityLink);
        }

        List<Task> adminTasks = taskService.createTaskQuery()
                .taskAssignee("admin")
                .listPage(0, 100);
        for (Task adminTask : adminTasks) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("cKey1", "cValue1");
            taskService.complete(adminTask.getId(), vars);
        }

        adminTasks = taskService.createTaskQuery().taskAssignee("admin").list();
        LOGGER.info("是否已经没有了 = {}", CollectionUtils.isEmpty(adminTasks));

    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskAttachment() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        runtimeService.startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().singleResult();

        taskService.createAttachment("url", task.getId(),
                task.getProcessInstanceId(), "name",
                "desc", "/url/test.png");

        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            LOGGER.info("taskAttachment = {}", ToStringBuilder.reflectionToString(taskAttachment, ToStringStyle.JSON_STYLE));
        }
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskComment() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message !!!");
        runtimeService.startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().singleResult();
        taskService.setAssignee(task.getId(),"admin");
        taskService.setOwner(task.getId(),"user1");

        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note 2");

        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            LOGGER.info("taskComment = {}", ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
        }

        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            LOGGER.info("taskEvent = {}", ToStringBuilder.reflectionToString(taskEvent, ToStringStyle.JSON_STYLE));
        }
    }

}
