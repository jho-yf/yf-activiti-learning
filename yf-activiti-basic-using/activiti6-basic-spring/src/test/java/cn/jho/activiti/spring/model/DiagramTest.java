package cn.jho.activiti.spring.model;

import cn.jho.activiti.spring.AbstractTest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.ProcessDiagramGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

/**
 * DiagramTest
 *
 * @author JHO xu-jihong@qq.com
 */
class DiagramTest extends AbstractTest {

    private static final String DEPLOYMENT_KEY = "custom-bpmn-key";

    private static final String pngResource = "custom-bpmn-model.png";

    @BeforeEach
    void initProcess() {
        String resource = "custom-bpmn-model.bpmn20.xml";

        repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key(DEPLOYMENT_KEY)
                .addClasspathResource(resource)
                .addClasspathResource(pngResource)
                .deploy();
    }

    @Test
    void viewImage() throws IOException {
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentKey(DEPLOYMENT_KEY).singleResult();
        List<String> resourceNames = repositoryService.getDeploymentResourceNames(deployment.getId());

        String imageName = null;
        for (String resourceName : resourceNames) {
            if (resourceName.contains(".png")) {
                imageName = resourceName;
            }
        }

        LOGGER.info("imageName={}", imageName);
        assertNotNull(imageName);
        assertEquals(pngResource, imageName);

        InputStream in = repositoryService.getResourceAsStream(deployment.getId(), imageName);
        FileCopyUtils.copy(in, Files.newOutputStream(new File("./target/" + imageName).toPath()));
    }

    @Test
    void genDiagramByBpmnModel() {
        ProcessEngineConfiguration configuration = engine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
        assertNotNull(diagramGenerator);
        LOGGER.info("diagramGenerator={}", diagramGenerator);

        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("custom-bpmn-model")
                .latestVersion()
                .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDef.getId());

        assertDoesNotThrow(() -> {
            InputStream is = diagramGenerator.generateDiagram(bpmnModel,
                    "png",
                    Arrays.asList("sid-3c326472-d844-48fd-a968-ffbe25d0c681",
                            "sid-b39d9182-c69e-4572-839f-eb1f1767f029"),
                    Collections.singletonList("sid-3587aa0f-dafb-4f79-b491-4ce3a3fa1eba"),
                    "宋体",
                    "宋体",
                    "宋体", null, 1.0);
            FileCopyUtils.copy(is, Files.newOutputStream(new File("./target/custom-gen-image.png").toPath()));
        });
    }

}
