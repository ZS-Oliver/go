package cn.idea.modules.audiClasses.web;

<<<<<<< HEAD
import cn.idea.modules.audiClasses.bean.AudiClassesVo;
=======
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;
<<<<<<< HEAD
=======
import cn.idea.modules.audiClasses.bean.AudiClassesVo;
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2

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
<<<<<<< HEAD
        av.setSids("1:2");
=======
        av.setSids("4:5:7");
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
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
<<<<<<< HEAD
                .param("total","30"))
=======
                .param("eid", "1"))
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
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