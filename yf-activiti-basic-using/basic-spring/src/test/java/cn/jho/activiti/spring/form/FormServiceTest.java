package cn.jho.activiti.spring.form;

import cn.jho.activiti.spring.AbstractTest;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>FormServiceTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class FormServiceTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "form-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("form-demo")
                .category("form-demo")
                .key("form-demo")
                .addClasspathResource(resource)
                .deploy();

        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
    }

    @Test
    void testGetForm() {
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        assertNotNull(startFormData);
    }

}
