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

    /*
    <startEvent id="sid-d8bfd134-9f78-4236-8b48-095681e1e171">
        <extensionElements>
            <activiti:formProperty id="business_id" name="业务id" type="string" readable="false"/>
            <activiti:formProperty id="startDate" name="请假开始时间" type="date" datePattern="yyyy-MM-dd" required="true"/>
            <activiti:formProperty id="endDate" name="请假结束时间" type="date" datePattern="yyyy-MM-dd" required="true"/>
            <activiti:formProperty id="reason" name="请假原因" type="string" required="true"/>
        </extensionElements>
    </startEvent>
     */
    @Test
    void testGetStartForm() {
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        assertNotNull(startFormData);
        assertSame(3, startFormData.getFormProperties().size());
        startFormData.getFormProperties().forEach(props -> {
            LOGGER.info("##############################");
            LOGGER.info("id={}", props.getId());
            LOGGER.info("name={}", props.getName());
            LOGGER.info("typeName={}", props.getType().getName());
            LOGGER.info("type={}", props.getType().getInformation("datePattern"));
            LOGGER.info("value={}", props.getValue());
        });
    }

}
