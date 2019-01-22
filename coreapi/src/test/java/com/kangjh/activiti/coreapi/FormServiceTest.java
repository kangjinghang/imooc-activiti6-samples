package com.kangjh.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class FormServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-form.bpmn20.xml"})
    public void testFormService() {

        FormService formService = activitiRule.getFormService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        LOGGER.info("startFormKey = {}", startFormKey);

        List<FormProperty> formProperties = formService.getStartFormData(processDefinition.getId()).getFormProperties();
        for (FormProperty formProperty : formProperties) {
            LOGGER.info("formProperty = {}", ToStringBuilder.reflectionToString(formProperty, ToStringStyle.JSON_STYLE));
        }

        Map<String, String> properties = Maps.newHashMap();
        properties.put("message", "my test message");

        formService.submitStartFormData(processDefinition.getId(), properties);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> taskFormDataFormProperties = taskFormData.getFormProperties();
        for (FormProperty property : taskFormDataFormProperties) {
            LOGGER.info("property = {}", ToStringBuilder.reflectionToString(property, ToStringStyle.JSON_STYLE));
        }

        Map<String, String> properties1 = Maps.newHashMap();
        properties1.put("yesORno", "yes");
        formService.submitTaskFormData(task.getId(), properties1);

        Task task1 = activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("task1 = {}", task1);

    }


}
