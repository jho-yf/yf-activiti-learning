package cn.jho.activiti.spring;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.jupiter.api.Test;

/**
 * SpringTest
 *
 * @author JHO xu-jihong@qq.com
 */
class SpringTest extends AbstractTest {

    @Test
    void testGetDefaultProcessEngine() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        LOGGER.info("engine:{}", engine);
        assertNotNull(engine);
        RepositoryService repositoryService = engine.getRepositoryService();
        LOGGER.info("repositoryService:{}", repositoryService);
        assertNotNull(repositoryService);
    }

}
