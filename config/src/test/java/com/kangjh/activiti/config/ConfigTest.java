package com.kangjh.activiti.config;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-21
 */
public class ConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void testConfig1() {

        //ProcessEngineConfiguration
        /*
         *   public static ProcessEngineConfiguration createProcessEngineConfigurationFromResource(String resource, String beanName) {
         *     return BeansConfigurationHelper.parseProcessEngineConfigurationFromResource(resource, beanName);
         *   }
         */


        //BeansConfigurationHelper
        /*
         *   public static ProcessEngineConfiguration parseProcessEngineConfiguration(Resource springResource, String beanName) {
         *     DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
         *     XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
         *     xmlBeanDefinitionReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
         *     xmlBeanDefinitionReader.loadBeanDefinitions(springResource);
         *     ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) beanFactory.getBean(beanName);
         *     processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(beanFactory));
         *     return processEngineConfiguration;
         *   }
         */
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();

        LOGGER.info("configuration = {}", configuration);
    }

    @Test
    public void testConfig2() {
        //ProcessEngineConfiguration
        /*
         *   public static ProcessEngineConfiguration createStandaloneProcessEngineConfiguration() {
         *     return new StandaloneProcessEngineConfiguration();
         *   }
         */
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        LOGGER.info("configuration = {}", configuration);
    }

}
