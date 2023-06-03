package cn.jho.activiti.spring.task;

import cn.jho.activiti.spring.AbstractTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>TaskServiceTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class TaskServiceTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "task-service-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("task-service-demo")
                .category("task-service-demo")
                .key("task-service-demo")
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    @Test
    void testFindByAssigneeAndComplete() {
        String assignee = "张三";
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", assignee);
        runtimeService.startProcessInstanceById(processDefinition.getId(), variables);

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        assertEquals(1, tasks.size());
        HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .singleResult();
        assertNull(historicTask.getEndTime());

        taskService.complete(tasks.get(0).getId());
        tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        assertEquals(0, tasks.size());
        historicTask = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .singleResult();
        assertNotNull(historicTask.getEndTime());
    }

}
