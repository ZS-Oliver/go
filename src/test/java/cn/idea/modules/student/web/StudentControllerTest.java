package cn.idea.modules.student.web;

import cn.idea.modules.student.bean.ImportanceEnum;
import cn.idea.modules.student.bean.SalesStageEnum;
import cn.idea.modules.student.bean.StatusEnum;
import cn.idea.modules.student.bean.StudentVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class StudentControllerTest extends MyUserTxTestNGSpringContextTests {
    private MockHttpSession session;

    private static final String PATH = "/student";

    @Test
    public void create() throws Exception {
        session = adminLoginSuccess();
        StudentVo sv = new StudentVo();
        sv.setName("雷军");
        sv.setEid(20);
        sv.setSex("aa");
        sv.setSourceId(1);
        sv.setPhone("13000000322");
        sv.setSalesStage((byte) 1);
        sv.setDegree((byte) 1);
        sv.setState((byte) 1);
        sv.setAge(null);
        sv.setExpectedDate("20181101");
        mockMvc.perform(postJson(session, PATH, sv))
                .andExpect(isOKStatus());
    }

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        // 员工创建
        session = employeeLoginSuccess();
        StudentVo sv4Em = genStudent("12345678910");
        mockMvc.perform(postJson(session, PATH, sv4Em))
                .andExpect(isOKStatus());

        // 员工创建，未指定期望时间
        StudentVo sv4Em2 = genStudent("12222222222");
        sv4Em2.setExpectedDate(null);
        mockMvc.perform(postJson(session, PATH, sv4Em2))
                .andExpect(isConflictStatus());

        // 市场经理创建，未指定eid
        session = marketingManagerLoginSuccess();
        StudentVo sv4Mk = genStudent("12345678900");
        mockMvc.perform(postJson(session, PATH, sv4Mk))
                .andExpect(isConflictStatus());

        StudentVo sv4Mk2 = genStudent("12345678911");
        sv4Mk2.setEid(1);
        mockMvc.perform(postJson(session, PATH, sv4Mk2))
                .andExpect(isOKStatus());

    }

    private StudentVo genStudent(String phone) {
        StudentVo sv = new StudentVo();
        sv.setName("王哈哈");
        sv.setState(StatusEnum.INVALID.getCode());
        sv.setDegree(ImportanceEnum.NORMAL.getCode());
        sv.setSalesStage(SalesStageEnum.FOLLOW.getCode());
        sv.setExpectedDate("20180909");
        sv.setPhone(phone);
        return sv;
    }

    @Test
    public void testList() throws Exception {
//        session = employeeLoginSuccess();
//        mockMvc.perform(getJson(session, PATH)
//                .param("parentName", "张三"))
//                .andExpect(isOKStatus());

        session = adminLoginSuccess();
//        mockMvc.perform(getJson(session, PATH)
//                .param("stateStr", StatusEnum.VIP.getCode() + ""))
//                .andExpect(isOKStatus());
//        mockMvc.perform(getJson(session, PATH)
//                .param("stateStr", StatusEnum.VALID.getCode() + ""))
//                .andExpect(isConflictStatus());
        mockMvc.perform(getJson(session, PATH))
                .andExpect(isOKStatus());

    }

    @Test
    public void testView() throws Exception {
        session = employeeLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/1"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        session = employeeLoginSuccess();
        StudentVo sv = new StudentVo();
        sv.setNickname("阿璇");
        mockMvc.perform(putJson(session, PATH + "/5", sv))
                .andExpect(isOKStatus());

        sv.setPhone("12323232323");
        mockMvc.perform(putJson(session, PATH + "/5", sv))
                .andExpect(isConflictStatus());
    }

    @Test
    @Rollback(false)
    public void testDelete() throws Exception {
        session = employeeLoginSuccess();
        mockMvc.perform((deleteJson(session, PATH + "/5")))
                .andExpect(isOKStatus());
    }

    @Test
    public void testUpdateEid() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(putJson(session, PATH + "/eid/change")
                .param("idStr", "7")
                .param("eid", "5")).andExpect(isOKStatus());
    }

    @Test
    public void testImportance() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/importance"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testSales() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/sales"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testStatus() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/status"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testCheckPhoneUsed() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, PATH + "/phone")
                .param("phone", "12345678910"))
                .andExpect(isOKStatus());
        mockMvc.perform(getJson(session, PATH + "/phone")
                .param("phone", "13456780000"))
                .andExpect(isOKStatus());
        mockMvc.perform(getJson(session, PATH + "/phone")
                .param("phone", ""))
                .andExpect(isOKStatus());
    }
}
