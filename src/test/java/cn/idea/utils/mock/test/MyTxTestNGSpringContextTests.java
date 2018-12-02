package cn.idea.utils.mock.test;

import cn.idea.utils.mock.enhancer.MyUserMockEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * 用于作为testNG的基类，但带有事务的功能，所有的方法会默认回滚
 */
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/applicationContext.xml", "classpath:spring/springmvc.xml" })
public class MyTxTestNGSpringContextTests extends AbstractTransactionalTestNGSpringContextTests implements MyUserMockEnhancer {
    protected MockMvc mockMvc;

    @Autowired
    private void buildMyMockMvc(WebApplicationContext wac) throws Exception {
        mockMvc = initMockMvc(wac);
    }

    @Override
    public MockMvc mockMvc() {
        return mockMvc;
    }
}
