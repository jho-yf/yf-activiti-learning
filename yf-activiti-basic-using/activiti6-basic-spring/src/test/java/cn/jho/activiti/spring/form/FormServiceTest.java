package cn.jho.activiti.spring.form;

import cn.jho.activiti.spring.AbstractTest;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    void testSubmitStartFormData() {
        Map<String, String> properties = new HashMap<>();
        properties.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        properties.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);

        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        assertSame(2, variables.size());
        assertNotNull(variables.get("startDate"));
        assertNotNull(variables.get("endDate"));
    }

    @Test
    void testGetTaskFormData() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        assertSame(0, taskFormData.getFormProperties().size());

        taskService.complete(task.getId());
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        taskFormData = formService.getTaskFormData(task.getId());
        assertSame(4, taskFormData.getFormProperties().size());
        taskFormData.getFormProperties().forEach(props -> {
            LOGGER.info("##############################");
            LOGGER.info("id={}", props.getId());
            LOGGER.info("name={}", props.getName());
            LOGGER.info("typeName={}", props.getType().getName());
            LOGGER.info("type={}", props.getType().getInformation("datePattern"));
            LOGGER.info("value={}", props.getValue());
        });

        Map<String, String> properties = new HashMap<>();
        properties.put("approve", "3");

        // 保存表单数据
        formService.saveFormData(task.getId(), properties);
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        assertSame("3", variables.get("approve"));

        // 保存表单数据并完成任务
        formService.submitTaskFormData(task.getId(), properties);
    }

}
