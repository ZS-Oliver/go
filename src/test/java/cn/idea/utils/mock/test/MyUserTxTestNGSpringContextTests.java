package cn.idea.utils.mock.test;

import cn.idea.utils.mock.enhancer.MyUserMockEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * 用于作为testNG的基类，带有事务的功能并且提供了用户相关的操作
 * 这个带有事务的测试上下文默认会自动回滚，一般方法无需加入 {@link org.springframework.test.annotation.Rollback} 注解
 */
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/applicationContext.xml", "classpath:spring/springmvc.xml"})
public class MyUserTxTestNGSpringContextTests extends AbstractTransactionalTestNGSpringContextTests implements MyUserMockEnhancer {
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