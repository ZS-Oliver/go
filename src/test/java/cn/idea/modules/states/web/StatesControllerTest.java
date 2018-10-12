package cn.idea.modules.states.web;

import cn.idea.modules.states.bean.StatesVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class StatesControllerTest extends MyUserTxTestNGSpringContextTests {
    private MockHttpSession session = new MockHttpSession();

    private static final String PATH = "/states";

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        session = employeeSuccess();
        StatesVo sv = new StatesVo();
        sv.setSid(6);
        sv.setCurStage((byte) 2);
        sv.setContent("???");
        sv.setNextState((byte) 2);
        sv.setNextDegree((byte) 2);
        sv.setNextStage((byte) 2);
        sv.setExceptedDate("20181005");
        mockMvc.perform(postJson(session, PATH, sv))
                .andExpect(isOKStatus());

    }

    @Test
    public void testView() throws Exception {
        session = employeeSuccess();
        mockMvc.perform(getJson(session, PATH + "/3"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        session = employeeSuccess();
        mockMvc.perform(getJson(session, PATH)
                .param("sid", "7"))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testDelete() throws Exception {
        session = employeeSuccess();
        mockMvc.perform(deleteJson(session, PATH + "/3"))
                .andExpect(isOKStatus());
    }
}
