package cn.jho.activiti.guide;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author JHO xujihong@bingosoft.net
 */
class ProcessEngineTest extends Assertions {

    @Test
    void testGetDefaultProcessEngine() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        assertNotNull(engine);
    }

}
