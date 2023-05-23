package cn.jho.activiti.spring;

import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * <p>CandidateUserTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class CandidateAndClaimTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @BeforeEach
    void initProcess() {
        String resource = "candidate-user-demo.bpmn20.xml";

        deployment = repositoryService.createDeployment()
                .name("candidate-user-demo")
                .category("candidate-user-demo")
                .key("candidate-user-demo")
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    @Test
    void testCandidate() {
        runtimeService.startProcessInstanceById(processDefinition.getId());

        // 根据任务代办人查询任务
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        assertNull(task);

        // 根据任务候选人查询任务
        task = taskService.createTaskQuery().taskCandidateUser("zhangsan").singleResult();
        assertNotNull(task);
        task = taskService.createTaskQuery().taskCandidateUser("lisi").singleResult();
        assertNotNull(task);
        task = taskService.createTaskQuery().taskCandidateUser("wangwu").singleResult();
        assertNotNull(task);

        // 根据任务ID查询关联的用户
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
        assertSame(3, identityLinks.size());
        for (IdentityLink identityLink : identityLinks) {
            LOGGER.info("用户ID:{}", identityLink.getUserId());
            LOGGER.info("任务ID:{}", identityLink.getTaskId());
            LOGGER.info("Type:{}", identityLink.getType());
            LOGGER.info("ProcessDefinitionId:{}", identityLink.getProcessDefinitionId());
            LOGGER.info("ProcessInstanceId:{}", identityLink.getProcessInstanceId());
        }

        List<HistoricIdentityLink> historicIdentityLinks = historyService
                .getHistoricIdentityLinksForTask(task.getId());
        assertSame(3, historicIdentityLinks.size());
        for (HistoricIdentityLink identityLink : historicIdentityLinks) {
            LOGGER.info("用户ID:{}", identityLink.getUserId());
            LOGGER.info("任务ID:{}", identityLink.getTaskId());
            LOGGER.info("Type:{}", identityLink.getType());
            LOGGER.info("ProcessInstanceId:{}", identityLink.getProcessInstanceId());
        }
    }

    @Test
    void testClaim() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(task);

        String taskId = task.getId();

        // 拾取任务
        taskService.claim(taskId, "zhangsan");
        task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        assertNotNull(task);
        task = taskService.createTaskQuery().taskCandidateUser("lisi").singleResult();
        assertNull(task);

        taskService.complete(taskId);
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertNull(task);
    }

}
