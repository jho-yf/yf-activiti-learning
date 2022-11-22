package cn.jho.activiti.spring;

import java.nio.charset.StandardCharsets;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * CustomBpmnModelTest
 *
 * @author JHO xu-jihong@qq.com
 */
class CustomBpmnModelTest extends AbstractTest {

    ProcessEngine engine;

    RepositoryService repositoryService;

    @BeforeEach
    void init() {
        engine = ProcessEngines.getDefaultProcessEngine();
        repositoryService = engine.getRepositoryService();
    }

    @Test
    void genBpmnModel() {
        Process process = new Process();
        process.setId("myProcess");
        process.setName("myProcessName");

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEventId");
        startEvent.setName("startEventName");
        process.addFlowElement(startEvent);

        UserTask apply = genUserTask("apply", "提交申请");
        UserTask departApproval = genUserTask("departApproval", "部门经理审批");
        UserTask managerApproval = genUserTask("managerApproval", "总经理审批");
        process.addFlowElement(apply);
        process.addFlowElement(departApproval);
        process.addFlowElement(managerApproval);

        ExclusiveGateway exclusiveGateway = genExclusiveGateway("exclusiveGateway", "exclusiveGateway");
        process.addFlowElement(exclusiveGateway);

        EndEvent end = genEndEvent("end", "end");
        process.addFlowElement(end);

        process.addFlowElement(genSequenceFlow("seqFlow1", "seqFlow1", startEvent, apply, null));
        process.addFlowElement(genSequenceFlow("seqFlow2", "seqFlow2", apply, exclusiveGateway, null));
        process.addFlowElement(
                genSequenceFlow("seqFlow3", "小于等于3天", exclusiveGateway, departApproval, "${ day <= 3 }"));
        process.addFlowElement(
                genSequenceFlow("seqFlow4", "大于3天", exclusiveGateway, managerApproval, "${ day > 3 }"));
        process.addFlowElement(genSequenceFlow("seqFlow5", "seqFlow5", departApproval, end, null));
        process.addFlowElement(genSequenceFlow("seqFlow6", "seqFlow6", managerApproval, end, null));

        BpmnModel model = new BpmnModel();
        model.addProcess(process);
        byte[] xml = new BpmnXMLConverter().convertToXML(model, StandardCharsets.UTF_8.name());
        assertNotNull(xml);
        LOGGER.info("xml:{}", new String(xml, StandardCharsets.UTF_8));

        String resource = "custom.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment().addBpmnModel(resource, model).deploy();
        assertNotNull(deployment);
        LOGGER.info("deployment:{}", deployment);
    }

    private UserTask genUserTask(String id, String name) {
        UserTask task = new UserTask();
        task.setId(id);
        task.setName(name);
        return task;
    }

    private ExclusiveGateway genExclusiveGateway(String id, String name) {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(id);
        gateway.setName(name);
        return gateway;
    }

    private EndEvent genEndEvent(String id, String name) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        endEvent.setName(name);
        return endEvent;
    }

    private SequenceFlow genSequenceFlow(String id, String name, FlowElement source, FlowElement target, String expr) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(id);
        flow.setName(name);
        flow.setSourceFlowElement(source);
        flow.setSourceRef(source.getId());
        flow.setTargetFlowElement(target);
        flow.setTargetRef(target.getId());
        flow.setConditionExpression(expr);
        return flow;
    }

}
