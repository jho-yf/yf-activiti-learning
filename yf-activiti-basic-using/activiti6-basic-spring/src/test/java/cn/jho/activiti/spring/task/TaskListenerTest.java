package cn.jho.activiti.spring.task;

import static cn.jho.activiti.listener.SetAssigneeListener.CUSTOM_ASSIGNEE;

import cn.jho.activiti.spring.AbstractTest;
import java.util.List;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>TaskListenerTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class TaskListenerTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "task-listener-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("task-listener-demo")
                .category("task-listener-demo")
                .key("task-listener-demo")
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    @Test
    void test() {
        runtimeService.startProcessInstanceById(processDefinition.getId());

        String assignee = CUSTOM_ASSIGNEE;
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
