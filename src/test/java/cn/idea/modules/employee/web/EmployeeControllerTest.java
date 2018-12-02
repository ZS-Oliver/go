package cn.idea.modules.employee.web;

import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.testng.annotations.Test;

public class EmployeeControllerTest extends MyUserTxTestNGSpringContextTests {
    private MockHttpSession session = new MockHttpSession();

    private static final String ROOT_PATH = "/employee";

    @Test
<<<<<<< HEAD
    public void testCreate() throws Exception {
        session = adminLoginSuccess();
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setName("员工[测试用]");
        employeeVo.setMid(1);
        employeeVo.setPhone("12345678901");
=======
//    @Rollback(false)
    public void testCreate() throws Exception {
        session = adminLoginSuccess();
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setName("员工");
        employeeVo.setMid(7);
        employeeVo.setPhone("12345671214");
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        mockMvc.perform(postJson(session, ROOT_PATH, employeeVo))
                .andExpect(isOKStatus());

        EmployeeVo conflictWithEmployee = new EmployeeVo();
        conflictWithEmployee.setName("来了");
<<<<<<< HEAD
        conflictWithEmployee.setPhone("12345678901");
        conflictWithEmployee.setMid(1);
=======
        conflictWithEmployee.setPhone("12345671213");
        conflictWithEmployee.setMid(7);
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        mockMvc.perform(postJson(session, ROOT_PATH, conflictWithEmployee))
                .andExpect(isConflictStatus());

        EmployeeVo conflictWithMarketingManager = new EmployeeVo();
<<<<<<< HEAD
        conflictWithMarketingManager.setMid(1);
        conflictWithMarketingManager.setPhone("12345678900");
=======
        conflictWithMarketingManager.setMid(7);
        conflictWithMarketingManager.setPhone("15232786339");
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        conflictWithMarketingManager.setName("123");
        mockMvc.perform(postJson(session, ROOT_PATH, conflictWithMarketingManager))
                .andExpect(isConflictStatus());

        EmployeeVo withoutMid = new EmployeeVo();
        withoutMid.setName("12");
<<<<<<< HEAD
        withoutMid.setPhone("12345678902");
=======
        withoutMid.setPhone("12345678900");
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        mockMvc.perform(postJson(session, ROOT_PATH, withoutMid))
                .andExpect(isConflictStatus());

        session = marketingManagerLoginSuccess();
        EmployeeVo marketingMake = new EmployeeVo();
<<<<<<< HEAD
        marketingMake.setName("王五");
        marketingMake.setPhone("12345678902");
=======
        marketingMake.setName("whwu");
        marketingMake.setPhone("12345678900");
>>>>>>> a28bc316e88c3a51e6fa98a44e069e0c0c2b4ff2
        mockMvc.perform(postJson(session, ROOT_PATH, marketingMake))
                .andExpect(isOKStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        session = marketingManagerLoginSuccess();
        EmployeeVo conflictWithEmployee = new EmployeeVo();
        conflictWithEmployee.setName("来了");
        conflictWithEmployee.setPhone("12345671213");
        mockMvc.perform(putJson(session, ROOT_PATH + "/4", conflictWithEmployee))
                .andExpect(isConflictStatus());

        EmployeeVo conflictWithMarketingManager = new EmployeeVo();
        conflictWithMarketingManager.setPhone("15232786339");
        conflictWithMarketingManager.setName("123");
        mockMvc.perform(putJson(session, ROOT_PATH + "/4", conflictWithMarketingManager))
                .andExpect(isConflictStatus());

        EmployeeVo rightVo = new EmployeeVo();
        rightVo.setName("123");
        rightVo.setPhone("12345678905");
        mockMvc.perform(putJson(session, ROOT_PATH + "/4", rightVo))
                .andExpect(isOKStatus());
        mockMvc.perform(putJson(session, ROOT_PATH + "/1", rightVo))
                .andExpect(isConflictStatus());
    }

    @Test
    public void testView() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, "/employee/1"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, "/employee"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testDelete() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(deleteJson(session, ROOT_PATH + "/4"))
                .andExpect(isConflictStatus());
        mockMvc.perform(deleteJson(session, ROOT_PATH + "/5"))
                .andExpect(isOKStatus());
    }
}