package cn.jho.activiti.basic.core;

import org.activiti.engine.cfg.AbstractProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CustomProcessEngineConfigurator
 *
 * @author JHO xu-jihong@qq.com
 */
public class CustomProcessEngineConfigurator extends AbstractProcessEngineConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomProcessEngineConfigurator.class);

    @Override
    public void beforeInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOGGER.info("beforeInit...");
    }

    @Override
    public void configure(ProcessEngineConfigurationImpl processEngineConfiguration) {
        LOGGER.info("configure...");
    }
}
