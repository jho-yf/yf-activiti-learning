package cn.jho.activiti.basic.ch1;

import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.SpringBeanFactoryProxyMap;
import org.junit.jupiter.api.Test;

/**
 * BeanGetAndSetTest
 *
 * @author JHO xu-jihong@qq.com
 */
class CustomBeanTest extends AbstractTest {

    @Test
    void testGetCustomBean() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) engine.getProcessEngineConfiguration();

        Map<Object, Object> beans = configuration.getBeans();
        assertInstanceOf(SpringBeanFactoryProxyMap.class, beans);

        Object bean = beans.get("jho");
        assertInstanceOf(People.class, bean);

        People people = (People) bean;
        assertEquals(18, people.getAge());
        assertEquals("jho", people.getName());
    }

}
