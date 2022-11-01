package cn.jho.activiti.basic.core;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.CommandInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CustomProcessEngineConfiguration
 *
 * @author JHO xu-jihong@qq.com
 */
public class CustomProcessEngineConfiguration extends ProcessEngineConfigurationImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomProcessEngineConfiguration.class);

    @Override
    public void initBeans() {
        // 在此处扩展源码
        super.initBeans();
        boolean containsKey = beans.containsKey("jho");
        LOGGER.info("containsKey: {}", containsKey);
    }

    @Override
    public CommandInterceptor createTransactionInterceptor() {
        return null;
    }

}
