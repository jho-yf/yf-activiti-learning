package cn.jho.activiti.basic.core;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom ProcessEngine Lifecycle Listener
 *
 * @author JHO xu-jihong@qq.com
 */
public class CustomProcessEngineLifecycleListener implements ProcessEngineLifecycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomProcessEngineLifecycleListener.class);

    @Override
    public void onProcessEngineBuilt(ProcessEngine processEngine) {
        LOGGER.info("onProcessEngineBuilt...");
    }

    @Override
    public void onProcessEngineClosed(ProcessEngine processEngine) {
        LOGGER.info("onProcessEngineClosed...");
    }
}
