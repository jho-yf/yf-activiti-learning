package cn.jho.activiti.spring;

import cn.jho.activiti.CustomIdGenerator;
import java.util.List;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * IdGeneratorTest
 *
 * @author JHO xu-jihong@qq.com
 */
class CustomIdGeneratorTest extends AbstractTest {

    @BeforeEach
    void initProcess() {
        String resource = "trivial.bpmn20.xml";

        repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("mykey")
                .addClasspathResource(resource)
                .deploy();

        repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("mykey")
                .addClasspathResource(resource)
                .deploy();

        repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("mykey")
                .addClasspathResource(resource)
                .deploy();
    }

    @Test
    void testCustomIdGenerator() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        assertTrue(
                list.stream().allMatch(procDef -> procDef.getDeploymentId().startsWith(CustomIdGenerator.ID_PREFIX)));
    }

}
