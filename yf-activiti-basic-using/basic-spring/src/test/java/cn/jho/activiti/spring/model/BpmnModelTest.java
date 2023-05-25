package cn.jho.activiti.spring.model;

import cn.jho.activiti.spring.AbstractTest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DiagramElement;
import org.activiti.engine.repository.DiagramLayout;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;

/**
 * BpmnModelTest
 *
 * @author JHO xu-jihong@qq.com
 */
class BpmnModelTest extends AbstractTest {

    @Test
    void testBpmnXMLConverter() {
        InputStream inputStream = BpmnModelTest.class.getClassLoader().getResourceAsStream("trivial.bpmn20.xml");

        BpmnXMLConverter converter = new BpmnXMLConverter();

        // xml to bpmn
        BpmnModel bpmnModel = converter.convertToBpmnModel(new InputStreamSource(inputStream), true, false,
                StandardCharsets.UTF_8.name());
        assertNotNull(bpmnModel);
        LOGGER.info("bpmn model: {}", bpmnModel);
        LOGGER.info("processes: {}", bpmnModel.getProcesses());

        // bpmn to xml
        byte[] bytes = converter.convertToXML(bpmnModel, StandardCharsets.UTF_8.name());
        String xml = new String(bytes);
        LOGGER.info("xml: {}", xml);
    }

    @Test
    void testGetBpmnModel() {
        String resource = "trivial.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("mykey")
                .addClasspathResource(resource)
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        assertNotNull(bpmnModel);
        byte[] bytes = new BpmnXMLConverter().convertToXML(bpmnModel, StandardCharsets.UTF_8.name());
        LOGGER.info("bpmn model: {}", new String(bytes, StandardCharsets.UTF_8));
    }


    /**
     * 获取元素以及其相应的坐标信息
     */
    @Test
    void testGetProcessDiagramLayout() {
        String resource = "process-with-di.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("process-with-di")
                .category("process-with-di")
                .key("process-with-di")
                .addClasspathResource(resource)
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        DiagramLayout layout = repositoryService.getProcessDiagramLayout(processDefinition.getId());
        Map<String, DiagramElement> elements = layout.getElements();
        assertNotNull(elements);

        for (Entry<String, DiagramElement> entry : elements.entrySet()) {
            LOGGER.info("[key]:{}, [value]:{}", entry.getKey(), entry.getValue());
        }
    }

    @Test
    void testGetElement() {
        String resource = "process-with-di.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("process-with-di")
                .category("process-with-di")
                .key("process-with-di")
                .addClasspathResource(resource)
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        assertNotNull(bpmnModel);
        for (Process process : bpmnModel.getProcesses()) {
            List<SequenceFlow> sequenceFlowList = process.findFlowElementsOfType(SequenceFlow.class);
            for (SequenceFlow flow : sequenceFlowList) {
                LOGGER.info("flowId: {}, sourceRef: {}, targetRef: {}", flow.getId(), flow.getSourceRef(),
                        flow.getSourceRef());
            }

            List<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
            for (UserTask task : userTaskList) {
                LOGGER.info("id: {}, task: {}", task.getId(), task);
            }
        }
    }

}
