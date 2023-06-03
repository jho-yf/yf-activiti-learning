package cn.jho.activiti;

import java.util.List;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.ProcessLevelValidator;
import org.springframework.util.StringUtils;

/**
 * CustomUserTaskValidator
 *
 * @author JHO xu-jihong@qq.com
 */
public class CustomUserTaskValidator extends ProcessLevelValidator {

    @Override
    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        List<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
        for (UserTask userTask : userTasks) {
            if (StringUtils.isEmpty(userTask.getAssignee())) {
                String problem = "custom-missing-assignee";
                String desc = userTask.getId() + "[" + userTask.getName() + "]没有设置任务处理人";
                addError(errors, problem, process, userTask, desc);
            }
        }
    }

}
