package cn.idea.utils.mock.test;

import cn.idea.utils.mock.enhancer.MyMockEnhancer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * 用于作为testNG的基类
 */
@Log4j2
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/applicationContext.xml", "classpath:spring/springmvc.xml"})
public class MyTestNGSpringContextTests extends AbstractTestNGSpringContextTests implements MyMockEnhancer {
    protected MockMvc mockMvc;

    @Autowired
    private void buildMyMockMvc(WebApplicationContext wac) throws Exception {
        mockMvc = initMockMvc(wac);
    }
}
