package cn.jho.activiti.basic.ch1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

/**
 * CustomEngineTest
 *
 * @author JHO xu-jihong@qq.com
 */
class CustomEngineTest extends AbstractTest {

    @Test
    void testCustomInMen() {
        ProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
        ProcessEngine engine = configuration.buildProcessEngine();
        assertNotNull(engine);
        LOGGER.info("StandaloneInMemProcessEngineConfiguration构建的流程引擎：{}", engine);
    }

    @Test
    void testCustomEngine() {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/yf-activiti-basic-using");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("litemall123456");
        configuration.setDatabaseSchemaUpdate("true");
        ProcessEngine engine = configuration.buildProcessEngine();
        assertNotNull(engine);
        LOGGER.info("StandaloneProcessEngineConfiguration构建的流程引擎：{}", engine);
    }

    @Test
    void testCustomEngineByStream() throws IOException {
        File file = ResourceUtils.getFile("classpath:activiti.cfg.xml");
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(
                Files.newInputStream(file.toPath()));
        ProcessEngine engine = configuration.buildProcessEngine();
        assertNotNull(engine);
        LOGGER.info("使用文件流构建的流程引擎：{}", engine);
    }

    @Test
    void testCustomEngineByResource() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(
                "activiti.cfg.xml");
        ProcessEngine engine = configuration.buildProcessEngine();
        assertNotNull(engine);
        LOGGER.info("从Resource资源构建的流程引擎：{}", engine);
    }

}
