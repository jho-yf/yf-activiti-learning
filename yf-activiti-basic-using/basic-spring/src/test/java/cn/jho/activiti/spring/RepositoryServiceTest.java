package cn.jho.activiti.spring;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * RepositoryServiceTest
 *
 * @author JHO xu-jihong@qq.com
 */
class RepositoryServiceTest extends AbstractTest {

    ProcessEngine engine;

    RepositoryService repositoryService;

    @BeforeEach
    void init() {
        engine = ProcessEngines.getDefaultProcessEngine();
        repositoryService = engine.getRepositoryService();
    }

    @Test
    void testCreateDeployment() {
        DeploymentBuilder builder = repositoryService.createDeployment();
        assertNotNull(builder);
        LOGGER.info("builder:{}", builder);
    }

    @Test
    void testDeployWithClasspathResource() {
        String resource = "trivial.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("mykey")
                .addClasspathResource(resource)
                .deploy();
        assertNotNull(deployment);
        LOGGER.info("deployment: {}", deployment);
        LOGGER.info("deployment id: {}", deployment.getId());
    }

    @Test
    void testDeployWithText() {
        String text = IoUtil.readFileAsString("trivial.bpmn20.xml");

        // resourceName 必须以 .bpmn20.xml 或 .bpmn 结尾，否则无法将资源部署到流程定义中
        String resourceName = "myresourcename.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .key("mykey")
                .category("mycategory")
                .addString(resourceName, text)
                .deploy();
        assertNotNull(deployment);
        LOGGER.info("deployment: {}", deployment);
        LOGGER.info("deployment id: {}", deployment.getId());
    }

    @Test
    void testDeployByInputStream() {
        InputStream inputStream = RepositoryServiceTest.class.getClassLoader()
                .getResourceAsStream("trivial.bpmn20.xml");
        String resourceName = "myresourcename.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .key("mykey")
                .category("mycategory")
                .addInputStream(resourceName, inputStream)
                .deploy();

        assertNotNull(deployment);
        LOGGER.info("deployment: {}", deployment);
        LOGGER.info("deployment id: {}", deployment.getId());
    }

    @Test
    void testDeployByBytes() {
        String resourceName = "trivial.bpmn20.xml";
        String text = IoUtil.readFileAsString(resourceName);
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .key("mykey")
                .category("mycategory")
                .addBytes(resourceName, text.getBytes(StandardCharsets.UTF_8))
                .deploy();

        assertNotNull(deployment);
        LOGGER.info("deployment: {}", deployment);
        LOGGER.info("deployment id: {}", deployment.getId());
    }

    @Test
    void testDeployByZip() {
        String resourceName = "trivial.bpmn20.zip";
        InputStream in = RepositoryServiceTest.class.getClassLoader().getResourceAsStream(resourceName);
        assertNotNull(in);
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .key("mykey")
                .category("mycategory")
                .addZipInputStream(zipInputStream)
                .deploy();

        assertNotNull(deployment);
        LOGGER.info("deployment: {}", deployment);
        LOGGER.info("deployment id: {}", deployment.getId());
    }

}
