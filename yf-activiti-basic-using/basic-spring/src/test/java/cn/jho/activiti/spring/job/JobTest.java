package cn.jho.activiti.spring.job;

import cn.jho.activiti.spring.AbstractTest;
import java.util.Date;
import java.util.List;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.junit.jupiter.api.Test;

/**
 * <p>JobTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class JobTest extends AbstractTest {

    @Test
    void testActivateProcessDefinitionsOn() {
        String resource = "custom-bpmn-model.bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name("myprocess")
                .category("mycategory")
                .key("custom-bpmn-key")
                .addClasspathResource(resource)
                .activateProcessDefinitionsOn(new Date(new Date().getTime() + 5 * 1000))
                .deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        List<Job> jobs = managementService.createTimerJobQuery().list();
        assertEquals(1, jobs.size());
        Job job = jobs.get(0);
        assertEquals("activate-processdefinition", job.getJobHandlerType());
        assertEquals(processDefinition.getId(), job.getProcessDefinitionId());
    }

}
