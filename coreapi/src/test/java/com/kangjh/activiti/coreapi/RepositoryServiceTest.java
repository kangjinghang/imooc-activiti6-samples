package com.kangjh.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class RepositoryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testRepository() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源0")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        LOGGER.info("deploy = {}", deploy);

        DeploymentBuilder deploymentBuilder1 = repositoryService.createDeployment();
        deploymentBuilder1.name("测试部署资源1")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");
        deploymentBuilder1.deploy();
//        Deployment deployment = repositoryService.createDeploymentQuery()
//                .deploymentId(deploy.getId())
//                .singleResult();

        List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
                .orderByDeploymenTime().asc()
                .listPage(0, 100);

        for (Deployment deployment : deploymentList) {
            LOGGER.info("deployment = {}", deployment);
        }
        LOGGER.info("deploymentList.size = {}", deploymentList.size());

//        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
//                .deploymentId(deployment.getId())
//                .listPage(0, 100);
        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc()
                .listPage(0, 100);
        for (ProcessDefinition processDefinition : processDefinitions) {
            LOGGER.info("processDefinition = {}, version = {}, key = {}, id = {}",
                    processDefinition, processDefinition.getVersion(),
                    processDefinition.getKey(), processDefinition.getId());
        }

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        LOGGER.info("processDefinition.id = {}", processDefinition.getId());

        repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        try {
            LOGGER.info("开始启动");
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
            LOGGER.info("成功启动");
        } catch (Exception e) {
            LOGGER.info("成功失败");
            LOGGER.error(e.getMessage(), e);
        }

        repositoryService.activateProcessDefinitionById(processDefinition.getId());

        LOGGER.info("开始启动");
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        LOGGER.info("成功启动");
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        LOGGER.info("processDefinition.id = {}", processDefinition.getId());

        repositoryService.addCandidateStarterUser(processDefinition.getId(), "user");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(), "groupM");

        List<IdentityLink> identityLinkList = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());

        for (IdentityLink identityLink : identityLinkList) {
            LOGGER.info("identityLink = {}", identityLink);
        }

        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), "groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(), "user");
    }

}
