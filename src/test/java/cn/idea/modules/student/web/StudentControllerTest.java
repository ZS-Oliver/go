package cn.idea.modules.student.web;

import cn.idea.modules.student.bean.StudentVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class StudentControllerTest extends MyUserTxTestNGSpringContextTests {
    private MockHttpSession session = new MockHttpSession();

    private static final String PATH = "/student";

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        session = employeeSuccess();
        StudentVo sv = new StudentVo();
        sv.setName("璇儿子");
        sv.setParentName("张三");
        sv.setPhone("12313312319");
        sv.setAlternateNumber("12323232323");
        sv.setWechat("hello,world");
        sv.setAddr("江苏省连云港市赣榆区");
        sv.setSourceId(3);
        sv.setState((byte) 1);
        sv.setDegree((byte) 1);
        sv.setSalesStage((byte) 2);
//        sv.setExpectedDate("20181004");
        mockMvc.perform(postJson(session, PATH, sv))
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        session = employeeSuccess();
        mockMvc.perform(getJson(session, PATH)
                .param("phone", "99999999999"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testView() throws Exception {
        session = employeeSuccess();
        mockMvc.perform(getJson(session, PATH + "/6"))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testUpdate() throws Exception {
        session = employeeSuccess();
        StudentVo sv = new StudentVo();
        sv.setName("阿璇");
        sv.setEid(2);
        mockMvc.perform(putJson(session, PATH + "/5", sv))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testDelete() throws Exception {
        session = employeeSuccess();
        mockMvc.perform((deleteJson(session, PATH + "/5")))
                .andExpect(isOKStatus());
    }

    @Test
    public void testViewList() throws Exception {
        session = employeeSuccess();
        mockMvc.perform(getJson(session, PATH + "/date/{date}", "20181004"))
                .andExpect(isOKStatus());
    }
}
