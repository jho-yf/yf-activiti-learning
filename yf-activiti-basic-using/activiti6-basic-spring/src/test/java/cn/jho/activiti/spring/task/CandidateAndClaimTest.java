package cn.jho.activiti.spring.task;

import cn.jho.activiti.spring.AbstractTest;
import java.util.List;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

/**
 * <p>CandidateUserTest</p>
 *
 * @author JHO xu-jihong@qq.com
 */
class CandidateAndClaimTest extends AbstractTest {

    Deployment deployment;
    ProcessDefinition processDefinition;

    @Test
    void testCandidateUser() {
        deploy("candidate-user-demo.bpmn20.xml", "candidate-user-demo", "candidate-user-demo",
                "candidate-user-demo");

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
        deploy("candidate-user-demo.bpmn20.xml", "candidate-user-demo", "candidate-user-demo",
                "candidate-user-demo");

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

    @Test
    void testCandidateGroups() {
        deploy("candidate-group-demo.bpmn20.xml",
                "candidate-group-demo",
                "candidate-group-demo", "candidate-group-demo");

        String groupId = "deptMgr";
        String userId1 = "zhangsan";
        String userId2 = "lisi";
        String userId3 = "wangwu";

        // 创建组
        GroupEntityImpl group = new GroupEntityImpl();
        group.setId(groupId);
        group.setName("部门经理");
        group.setRevision(0);
        identityService.saveGroup(group);

        identityService.saveUser(createUserEntity(userId1, "san", "zhang"));
        identityService.saveUser(createUserEntity(userId2, "si", "li"));
        identityService.saveUser(createUserEntity(userId3, "wu", "wang"));

        identityService.createMembership(userId1, groupId);
        identityService.createMembership(userId2, groupId);
        identityService.createMembership(userId3, groupId);

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        Task task = taskService.createTaskQuery().taskCandidateUser(userId1).singleResult();
        assertNotNull(task);
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        task = taskService.createTaskQuery().taskCandidateGroup(groupId).singleResult();
        assertNotNull(task);
        assertEquals(processInstance.getId(), task.getProcessInstanceId());
    }

    void deploy(String resource, String deploymentName, String category, String key) {
        deployment = repositoryService.createDeployment()
                .name(deploymentName)
                .category(category)
                .key(key)
                .addClasspathResource(resource)
                .deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
    }

    UserEntity createUserEntity(String id, String firstName, String lastName) {
        UserEntityImpl user = new UserEntityImpl();
        user.setId(id);
        user.setFirstName(firstName);
        user.setFirstName(lastName);
        user.setRevision(0);
        return user;
    }

}
