package cn.jho.activiti.spring.vars;

import cn.jho.activiti.model.Person;
import cn.jho.activiti.spring.AbstractTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>ProcessVariablesTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class ProcessVariablesTest extends AbstractTest {

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
    void testStartWithVars() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("empId", "zhangsan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);

        Map<String, Object> variablesQuery = runtimeService.getVariables(processInstance.getId());
        assertEquals(variablesQuery, variables);

        Task task = taskService.createTaskQuery()
                .taskAssignee((String) variables.get("empId"))
                .singleResult();
        assertNotNull(task);
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        String msg = "zhangsan leave...";
        Person person = new Person("person_id", "zhangsan");
        taskService.setVariable(task.getId(), "msg", msg);
        taskService.setVariable(task.getId(), "person", new Person("person_id", "zhangsan"));
        variablesQuery = runtimeService.getVariables(processInstance.getId());
        assertEquals(msg, variablesQuery.get("msg"));
        assertEquals(person, variablesQuery.get("person"));

        assertEquals(taskService.getVariable(task.getId(), "msg"), variablesQuery.get("msg"));
        assertEquals(taskService.getVariable(task.getId(), "person"), variablesQuery.get("person"));

        List<HistoricVariableInstance> historicVars = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .list();
        assertNotNull(historicVars);
        Map<String, HistoricVariableInstance> map = historicVars.stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getVariableName, Function.identity()));
        assertEquals("string", map.get("msg").getVariableTypeName());
        assertEquals("string", map.get("empId").getVariableTypeName());
        assertEquals("serializable", map.get("person").getVariableTypeName());

        // 完成任务
        String taskId = task.getId();
        assertThrows(ActivitiException.class, () -> {
            taskService.complete(taskId);
        }, "Unknown property used in expression: ${mgrId}");

        // 携带变量完成任务
        Map<String, Object> taskCompletionVars = new HashMap<>();
        taskCompletionVars.put("mgrId", "lisi");
        taskService.complete(task.getId(), taskCompletionVars);
    }


}
