package cn.jho.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;

/**
 * <p>SetAssigneeListener</p>
 *
 * @author JHO xu-jihong@qq.com
 */
public class SetAssigneeListener implements TaskListener {

    public static final String CUSTOM_ASSIGNEE = "zhangsan";

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee(CUSTOM_ASSIGNEE);
        Context.getProcessEngineConfiguration()
                .getHistoryManager()
                .recordTaskAssigneeChange(delegateTask.getId(), CUSTOM_ASSIGNEE);
    }


}
