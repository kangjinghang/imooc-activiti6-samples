package com.kangjh.activiti.dbentity;

import com.google.common.collect.Lists;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public class DBConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfigTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testDBConfig() {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();

        ManagementService managementService = processEngine.getManagementService();
        Map<String, Long> tableCount = managementService.getTableCount();
        ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());
        Collections.sort(tableNames);
        for (String tableName : tableNames) {
            LOGGER.info("table = {}", tableName);
        }
        LOGGER.info("tableNames.size = {}", tableNames.size());

    }

    @Test
    public void dropTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();

        ManagementService managementService = processEngine.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                LOGGER.info("删除表结构");
                return null;
            }
        });
    }

}
