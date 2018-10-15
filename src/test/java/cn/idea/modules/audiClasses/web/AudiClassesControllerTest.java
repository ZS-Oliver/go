package cn.idea.modules.audiClasses.web;

import cn.idea.modules.audiClasses.bean.AudiClassesVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class AudiClassesControllerTest extends MyUserTxTestNGSpringContextTests {
    private MockHttpSession session = new MockHttpSession();
    public static final String PATH = "/audiClasses";

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        session = employeeLoginSuccess();
        AudiClassesVo av = new AudiClassesVo();
        av.setAuditionTime("20181005");
        av.setName("围棋[测试]");
        av.setTotal(30);
        av.setSids("4:5:7");
        av.setSite("小东门");
        mockMvc.perform(postJson(session, PATH, av))
                .andExpect(isOKStatus());
    }


    @Test
    public void testView() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/2"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(getJson(session, PATH)
                .param("eid", "1"))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testUpdate() throws Exception {
        session = employeeLoginSuccess();
        AudiClassesVo av = new AudiClassesVo();
        av.setSids("3:7");
        mockMvc.perform(putJson(session, PATH + "/2", av))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testDelete() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(deleteJson(session, PATH + "/2"))
                .andExpect(isOKStatus());
    }
}