package com.kangjh.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
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
public class HistoryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg_history.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testHistoryService() {

        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        Map<String, Object> transientVariables = Maps.newHashMap();
        transientVariables.put("tKey1", "tValue1");

        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("my-process")
                .variables(variables)
                .transientVariables(transientVariables).start();

        activitiRule.getRuntimeService().setVariable(processInstance.getId(), "key1", "value1_1");

        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

//        activitiRule.getTaskService().complete(task.getId(),variables);

        Map<String, String> properties = Maps.newHashMap();
        properties.put("fKey1", "fValue1");
        properties.put("key2", "value2_2");
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);

        List<HistoricProcessInstance> historicProcessInstances = activitiRule.getHistoryService().createHistoricProcessInstanceQuery().listPage(0, 100);
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            LOGGER.info("historicProcessInstance = {}", ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE));
        }

        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("historicActivityInstance = {}", ToStringBuilder.reflectionToString(historicActivityInstance, ToStringStyle.JSON_STYLE));
        }

        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().listPage(0, 10);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            LOGGER.info("historicTaskInstance = {}", ToStringBuilder.reflectionToString(historicTaskInstance, ToStringStyle.JSON_STYLE));
        }

        List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService().createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("historicVariableInstance = {}", historicVariableInstance);
        }

        List<HistoricDetail> historicDetails = activitiRule.getHistoryService().createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            LOGGER.info("historicDetail = {}", historicDetail);
        }

        ProcessInstanceHistoryLog processInstanceHistoryLog = activitiRule.getHistoryService()
                .createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeVariables()
                .includeFormProperties()
                .includeComments()
                .includeTasks()
                .includeActivities()
                .includeVariableUpdates().singleResult();

        List<HistoricData> historicDataList = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicData : historicDataList) {
            LOGGER.info("historicData = {}", historicData);
        }

        activitiRule.getHistoryService().deleteHistoricProcessInstance(processInstance.getId());

        HistoricProcessInstance historicProcessInstance = activitiRule.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
        LOGGER.info("historicProcessInstance = {}", historicProcessInstance);

    }


}
