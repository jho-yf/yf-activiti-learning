package cn.jho.activiti.spring;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractTest
 *
 * @author JHO xu-jihong@qq.com
 */
abstract class AbstractTest extends Assertions {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected ProcessEngine engine;

    protected RepositoryService repositoryService;

    @BeforeEach
    void init() {
        engine = ProcessEngines.getDefaultProcessEngine();
        repositoryService = engine.getRepositoryService();
    }

}
