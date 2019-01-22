package com.kangjh.activiti.coreapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class IdentityServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testIdentityService() {
        IdentityService identityService = activitiRule.getIdentityService();

        User user1 = identityService.newUser("user1");
        user1.setEmail("user1@126.com");
        User user2 = identityService.newUser("user2");
        user2.setEmail("user2@163.com");

        identityService.saveUser(user1);
        //IdentityServiceImpl.commandExecutor.execute(new SaveUserCmd(user)); --> SaveUserCmd.execute(CommandContext commandContext)
        //-->AbstractEntityManager.insert(EntityImpl entity, boolean fireCreateEvent) --> MybatisUserDataManager.void insert(EntityImpl entity)
        //-->DbSqlSession.void insert(Entity entity) --> DbSqlSession.void flush()
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("group2");
        identityService.saveGroup(group2);

        identityService.createMembership("user1", "group1");
        identityService.createMembership("user2", "group1");
        identityService.createMembership("user1", "group2");

        User user11 = identityService.createUserQuery().userId("user1").singleResult();
        user11.setLastName("tim");
        identityService.saveUser(user11);

        List<User> userList = identityService.createUserQuery().memberOfGroup("group1").listPage(0, 100);
        for (User user : userList) {
            LOGGER.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }

        List<Group> groupList = identityService.createGroupQuery().groupMember("user1").listPage(0, 100);
        for (Group group : groupList) {
            LOGGER.info("group = {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
        }

    }


}
