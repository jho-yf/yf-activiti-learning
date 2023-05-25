package cn.jho.activiti.spring.engine;

import cn.jho.activiti.spring.AbstractTest;
import java.util.List;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>TriggerTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class TriggerTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "receive-task-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("receive-task-demo")
                .category("receive-task-demo")
                .key("receive-task-demo-key")
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    @Test
    void trigger() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
                .list();
        assertEquals(2, executions.size());
        assertTrue(executions.stream()
                .anyMatch(e -> "sid-bfa29648-8d38-4fc2-9656-075f87ee4b99".equals(e.getActivityId())));

        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .onlyChildExecutions().singleResult();
        assertNotNull(execution);
        assertEquals("sid-bfa29648-8d38-4fc2-9656-075f87ee4b99", execution.getActivityId());

        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .onlyChildExecutions().singleResult();
        assertNotNull(execution);
        assertEquals("sid-35f87948-c838-4533-bdc0-31d74d5b8337", execution.getActivityId());
        runtimeService.trigger(execution.getId());
        assertNull(
                runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult());
    }

}
