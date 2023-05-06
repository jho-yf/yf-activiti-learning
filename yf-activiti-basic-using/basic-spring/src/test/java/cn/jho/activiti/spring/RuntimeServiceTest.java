package cn.jho.activiti.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    void testStartProcessInstance() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

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
    }

}
