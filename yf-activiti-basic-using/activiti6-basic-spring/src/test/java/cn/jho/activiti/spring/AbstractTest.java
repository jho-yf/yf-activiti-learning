package cn.jho.activiti.spring;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractTest
 *
 * @author JHO xu-jihong@qq.com
 */
public abstract class AbstractTest extends Assertions {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected ProcessEngine engine;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;

    @BeforeEach
    void init() {
        engine = ProcessEngines.getDefaultProcessEngine();
        repositoryService = engine.getRepositoryService();
        runtimeService = engine.getRuntimeService();
        taskService = engine.getTaskService();
        historyService = engine.getHistoryService();
        identityService = engine.getIdentityService();
        managementService = engine.getManagementService();
        formService = engine.getFormService();
    }

    @AfterEach
    void destroy() {
        managementService.createTimerJobQuery().list().forEach(job -> managementService.deleteTimerJob(job.getId()));
        managementService.createJobQuery().list().forEach(job -> managementService.deleteJob(job.getId()));
        managementService.createDeadLetterJobQuery().list()
                .forEach(job -> managementService.deleteDeadLetterJob(job.getId()));

        repositoryService.createDeploymentQuery()
                .list()
                .forEach(deployment -> repositoryService.deleteDeployment(deployment.getId(), true));
        historyService.createHistoricProcessInstanceQuery()
                .list()
                .forEach(processInstance -> historyService.deleteHistoricProcessInstance(processInstance.getId()));
        historyService.createHistoricTaskInstanceQuery()
                .list()
                .forEach(task -> historyService.deleteHistoricTaskInstance(task.getId()));
        identityService.createUserQuery().list().forEach(user -> identityService.deleteUser(user.getId()));
        identityService.createGroupQuery().list().forEach(group -> identityService.deleteGroup(group.getId()));
    }

}
