package cn.idea.modules.source.web;

import cn.idea.modules.source.bean.SourceVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class SourceControllerTest extends MyUserTxTestNGSpringContextTests {

    private MockHttpSession session = new MockHttpSession();

    private static final String PATH = "/source";

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        session = adminLoginSuccess();
        SourceVo sv = new SourceVo();
        sv.setType((byte) 1);
        sv.setSource("门河中心小学");
        mockMvc.perform(postJson(session, PATH, sv))
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(getJson(session, PATH)
                .param("type", "0"))
                .andExpect(isOKStatus());

        mockMvc.perform(getJson(session, PATH)
                .param("type", "0")
                .param("source", "金海岸花园:盐场家园"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testView() throws Exception {
        session = adminLoginSuccess();
<<<<<<< HEAD
        mockMvc.perform(getJson(session, PATH + "/6"))
=======
        mockMvc.perform(getJson(session, PATH + "/2"))
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testUpdate() throws Exception {
        session = adminLoginSuccess();
        SourceVo sv = new SourceVo();
        sv.setSource("赣榆高级中学");
        mockMvc.perform(putJson(session, PATH + "/3", sv))
                .andExpect(isOKStatus());

        sv.setType((byte) 3);
        mockMvc.perform(putJson(session, PATH + "/3", sv))
                .andExpect(isConflictStatus());

    }
<<<<<<< HEAD

    @Test
    public void testStatuses() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/states"))
                .andExpect(isOKStatus());
    }
=======
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
}
