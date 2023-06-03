package cn.jho.activiti.spring.vars;

import cn.jho.activiti.model.Car;
import cn.jho.activiti.spring.AbstractTest;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>CustomVariableTypeTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class CustomVariableTypeTest extends AbstractTest {

    Deployment deployment;

    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "process-vars-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("process-vars-demo")
                .category("process-vars-demo")
                .key("process-vars-demo")
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    @Test
    void testCustomVariableType() {
        Map<String, Object> input = new HashMap<>();
        input.put("empId", "zhangsan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), input);

        Car car = new Car("car-id", "car-name");
        runtimeService.setVariable(processInstance.getId(), "jsonVar", car);
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        assertEquals(car, variables.get("jsonVar"));
    }

}
