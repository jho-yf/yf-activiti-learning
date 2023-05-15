package cn.jho.activiti.spring;

import cn.jho.activiti.cmd.DeleteHistoricProcessInstanceCmd;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>RuntimeServiceTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class RuntimeServiceTest extends AbstractTest {

    Deployment deployment;

    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "custom-bpmn-model.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("custom-bpmn-key")
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    @Test
    void testStartProcessInstance() {
        // 启动流程实例
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);
        assertNotNull(processInstance);
        LOGGER.info("processInstance: {}", processInstance);
        LOGGER.info("processInstance id: {}", processInstance.getId());
        LOGGER.info("processInstance activityId: {}", processInstance.getActivityId());

        // 查询所有任务
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            LOGGER.info("task: {}", task);
        }

        // 查询指定办理人任务
        tasks = taskService.createTaskQuery().taskAssignee("张三").list();
        assertFalse(tasks.isEmpty());
        tasks = taskService.createTaskQuery().taskAssignee("whatever").list();
        assertTrue(tasks.isEmpty());

        // 完成待办任务
        taskService.complete(taskService.createTaskQuery().taskAssignee("张三").singleResult().getId());
        assertNotNull(runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult());
        taskService.complete(taskService.createTaskQuery().taskAssignee("李四").singleResult().getId());
        assertNull(runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult());
    }

    @Test
    void testDeleteProcessInstance() {
        // 启动流程实例
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        String deleteReason = "no reason";
        runtimeService.deleteProcessInstance(processInstance.getId(), deleteReason);
        assertNull(runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        assertNotNull(historicProcessInstance);
        assertNotNull(historicProcessInstance.getEndTime());
        assertEquals(deleteReason, historicProcessInstance.getDeleteReason());
    }

    @Test
    void testDeleteProcessInstanceCascade() {
        // 启动流程实例
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        // 执行自定义命令
        managementService.executeCommand(
                new DeleteHistoricProcessInstanceCmd(processInstance.getId(), "no reason", false));

        assertNull(runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult());
    }

    @Test
    void testGetActiveActivitiIds() {
        // 启动流程实例
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 1);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstance.getId());
        assertNotNull(activeActivityIds);
        assertEquals(1, activeActivityIds.size());
        assertEquals("sid-abff028b-e798-48f8-a93a-ae8deb55bc73", activeActivityIds.get(0));
    }


}
