package cn.jho.activiti.basic.ch1;

import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineInfo;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

/**
 * ProcessEnginesTest
 *
 * @author JHO xu-jihong@qq.com
 * @date 2022-10-18 07:23
 */
class ProcessEnginesTest extends AbstractTest {

    @Test
    void testGetProcessEngine() {
        // 获取默认流程引擎
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        LOGGER.info("流程引擎: {}", engine);
        LOGGER.info("流程引擎Class对象: {}", engine.getClass());

        // 根据名称获取默认流程引擎
        ProcessEngine defaultEngine = ProcessEngines.getProcessEngine("default");
        assertEquals(engine, defaultEngine);

        // 根据名称获取流程引擎信息
        ProcessEngineInfo engineInfo = ProcessEngines.getProcessEngineInfo("default");
        LOGGER.info("流程引擎信息类: {}", engineInfo);
        LOGGER.info("获取流程引擎初始化中的异常信息: {}", engineInfo.getException());
        LOGGER.info("获取流程引擎名称: {}", engineInfo.getName());
        LOGGER.info("流程引擎配置资源信息: {}", engineInfo.getResourceUrl());

        Map<String, ProcessEngine> processEngines = ProcessEngines.getProcessEngines();
        assertEquals(1, processEngines.size());
    }

}
