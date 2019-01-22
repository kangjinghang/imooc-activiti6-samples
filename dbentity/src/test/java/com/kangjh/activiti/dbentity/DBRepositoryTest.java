package com.kangjh.activiti.dbentity;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public class DBRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBRepositoryTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testDeploy() {
        activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();
    }

    @Test
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        repositoryService.suspendProcessDefinitionById("second_process:2:7504");

        boolean suspended = repositoryService.isProcessDefinitionSuspended("second_process:2:7504");

        LOGGER.info("suspended = {}", suspended);
    }



}
