package cn.jho.activiti;

import java.util.UUID;
import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * CustomIdGenerator
 *
 * @author JHO xu-jihong@qq.com
 */
public class CustomIdGenerator implements IdGenerator {

    public static final String ID_PREFIX = "jho-";

    @Override
    public String getNextId() {
        return ID_PREFIX + UUID.randomUUID();
    }
}
