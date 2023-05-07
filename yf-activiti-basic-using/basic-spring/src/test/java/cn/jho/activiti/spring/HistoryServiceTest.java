package cn.jho.activiti.spring;

import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>HistoryServiceTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class HistoryServiceTest extends AbstractTest {

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
    void testQueryProcessInstance() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        // 启动流程实例
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);
        assertNotNull(processInstance);

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(historicProcessInstance);
        assertNull(historicProcessInstance.getEndTime());

        taskService.complete(taskService.createTaskQuery().taskAssignee("张三").singleResult().getId());
        taskService.complete(taskService.createTaskQuery().taskAssignee("李四").singleResult().getId());

        historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(historicProcessInstance);
        assertNotNull(historicProcessInstance.getEndTime());
    }

}
