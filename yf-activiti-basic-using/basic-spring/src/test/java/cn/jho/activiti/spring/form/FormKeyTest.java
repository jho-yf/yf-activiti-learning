package cn.jho.activiti.spring.form;

import cn.jho.activiti.spring.AbstractTest;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.jho.activiti.form.CustomFormEngine.MINE_FORM_ENGINE_NAME;

/**
 * 外置表单
 *
 * @author JHO xu-jihong@qq.com
 */
class FormKeyTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "form/form-key-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("form-key-demo")
                .category("form-key-demo")
                .key("form-key-demo")
                .addClasspathResource(resource)
                .addClasspathResource("form/applyForm.html")
                .addClasspathResource("form/approveForm.html")
                .deploy();

        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
    }

    /*
        <startEvent id="sid-8cab9192-f9a0-49cc-ad22-af7c835cc795" activiti:formKey="form/applyForm.html"/>
     */
    @Test
    void testGetStartFormKey() {
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        assertEquals("form/applyForm.html", startFormKey);
    }

    /*
    <userTask id="sid-a23d158e-026d-4815-b08f-1a7336932846" name="【经理审批】" activiti:formKey="form/approveForm.html"/>
     */
    @Test
    void testGetTaskFormKey() {
        String taskFormKey = formService.getTaskFormKey(processDefinition.getId(), "sid-a23d158e-026d-4815-b08f-1a7336932846");
        assertEquals("form/approveForm.html", taskFormKey);

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        assertNotNull(taskFormData);
        assertEquals("form/approveForm.html", taskFormData.getFormKey());
        assertEquals(task.getId(), taskFormData.getTask().getId());
    }

    @Test
    void getFormKeyByBpmnModel() {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        Process process = bpmnModel.getProcesses().get(0);
        StartEvent startEvent = (StartEvent) process.getInitialFlowElement();
        assertEquals("form/applyForm.html", startEvent.getFormKey());

        List<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
        assertTrue(userTasks.stream().anyMatch(task -> task.getFormKey().equals("form/approveForm.html")));
    }

    @Test
    void testGetRenderedForm() throws IOException {
        Object renderedStartForm = formService.getRenderedStartForm(processDefinition.getId());
        File file = ResourceUtils.getFile("classpath:form/applyForm.html");
        assertEquals(new String(Files.readAllBytes(file.toPath())), renderedStartForm);

        Map<String, Object> vars = new HashMap<>();
        vars.put("startDate", new Date());
        vars.put("endDate", new Date());
        vars.put("reason", "no reason");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId(), vars);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
        file = ResourceUtils.getFile("classpath:form/approveForm.html");
        assertNotEquals(new String(Files.readAllBytes(file.toPath())), renderedTaskForm);
    }

    @Test
    void testCustomFormEngine() throws IOException {
        Map<String, Object> vars = new HashMap<>();
        vars.put("startDate", new Date());
        vars.put("endDate", new Date());
        vars.put("reason", "no reason");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId(), vars);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        Object renderedTaskForm = formService.getRenderedTaskForm(task.getId(), MINE_FORM_ENGINE_NAME);
        File file = ResourceUtils.getFile("classpath:form/approveForm.html");
        assertNotEquals(new String(Files.readAllBytes(file.toPath())), renderedTaskForm);
    }

}
