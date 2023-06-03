package cn.jho.activiti.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * <p>DeleteHistoricProcessInstanceCmd</p>
 *
 * @author JHO xu-jihong@qq.com
 */
public class DeleteHistoricProcessInstanceCmd implements Command<Void> {

    private final String processInstanceId;
    private final String deleteReason;
    private final boolean cascade;

    public DeleteHistoricProcessInstanceCmd(String processInstanceId, String deleteReason, boolean cascade) {
        this.processInstanceId = processInstanceId;
        this.deleteReason = deleteReason;
        this.cascade = cascade;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        commandContext.getExecutionEntityManager().deleteProcessInstance(processInstanceId, deleteReason, cascade);
        return null;
    }
}
