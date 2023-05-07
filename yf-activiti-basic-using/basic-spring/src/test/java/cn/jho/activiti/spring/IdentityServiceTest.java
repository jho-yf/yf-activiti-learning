package cn.jho.activiti.spring;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>IdentityServiceTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class IdentityServiceTest extends AbstractTest {

    Deployment deployment;

    @BeforeEach
    void initProcess() {
        String resource = "custom-bpmn-model.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("custom-bpmn-key")
                .addClasspathResource(resource)
                .deploy();
    }

    @Test
    void startProcessInstanceWithIdentity() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        String userId = "jho";

        // 启动流程实例
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);
        assertEquals(userId, processInstance.getStartUserId());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
        assertEquals(userId, historicProcessInstance.getStartUserId());
    }

    @Test
    void startProcessInstanceWithAuthentication() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        String userId = "jho";

        // 启动流程实例
        Authentication.setAuthenticatedUserId(userId);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);
        assertEquals(userId, processInstance.getStartUserId());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
        assertEquals(userId, historicProcessInstance.getStartUserId());
    }

}
