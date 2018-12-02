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
        session = employeeLoginSuccess();
        StatesVo sv = new StatesVo();
<<<<<<< HEAD
        sv.setSid(1);
=======
        sv.setSid(6);
        sv.setCurStage((byte) 2);
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        sv.setContent("???");
        sv.setNextState((byte) 2);
        sv.setNextDegree((byte) 2);
        sv.setNextStage((byte) 2);
<<<<<<< HEAD
        sv.setExpectedDate("20181005");
=======
        sv.setExceptedDate("20181005");
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        mockMvc.perform(postJson(session, PATH, sv))
                .andExpect(isOKStatus());

    }

    @Test
    public void testView() throws Exception {
        session = employeeLoginSuccess();
<<<<<<< HEAD
        mockMvc.perform(getJson(session, PATH + "/1"))
=======
        mockMvc.perform(getJson(session, PATH + "/3"))
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        session = employeeLoginSuccess();
        mockMvc.perform(getJson(session, PATH)
                .param("sid", "7"))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testDelete() throws Exception {
        session = employeeLoginSuccess();
        mockMvc.perform(deleteJson(session, PATH + "/3"))
                .andExpect(isOKStatus());
    }
<<<<<<< HEAD

    @Test
    public void testStudentStates() throws Exception {
        session = employeeLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/student/7"))
                .andExpect(isOKStatus());
    }
=======
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
}
