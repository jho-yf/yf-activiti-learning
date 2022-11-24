package cn.jho.activiti.spring;

import java.util.List;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ProDefTest
 *
 * @author JHO xu-jihong@qq.com
 */
class ProDefTest extends AbstractTest {


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
    void queryOrderByVersion() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .list();

        assertSame(3, list.size());

        for (int i = 0; i < list.size() - 1; i++) {
            assertTrue(list.get(i).getVersion() < list.get(i + 1).getVersion());
        }
    }

    @Test
    void deleteDeployment() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

        assertTrue(list.size() >= 3);
        for (ProcessDefinition definition : list) {
            repositoryService.deleteDeployment(definition.getDeploymentId());
        }
        assertSame(0, repositoryService.createProcessDefinitionQuery().list().size());
    }

    @Test
    void createNativeDeploymentQuery() {
        List<Deployment> deployments = repositoryService.createNativeDeploymentQuery()
                .sql("select name_ from act_re_deployment")
                .list();
        assertNotNull(deployments);
        assertTrue(deployments.size() >= 3);
        assertTrue(deployments.stream().allMatch(d -> d.getName() != null));
        assertTrue(deployments.stream().allMatch(d -> d.getId() == null));
    }

}
