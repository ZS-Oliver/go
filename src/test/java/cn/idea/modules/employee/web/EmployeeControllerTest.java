package cn.idea.modules.employee.web;

import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.utils.mock.test.MyUserTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class EmployeeControllerTest extends MyUserTxTestNGSpringContextTests {
    private MockHttpSession session = new MockHttpSession();

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        session = adminLoginSuccess();
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setName("员工[测试用]");
        employeeVo.setMid(7);
        employeeVo.setPhone("12345671212");
        employeeVo.setPasswd("123321");
        mockMvc.perform(postJson(session, "/employee", employeeVo))
                .andExpect(isOKStatus());
    }

    @Test
    public void testLogin() throws Exception {
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testView() throws Exception {
        mockMvc.perform(getJson(session, "/employee/1"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(getJson(session, "/employee")
                .param("name", "sn")
                .param("phone", "15232786339"))
                .andExpect(isOKStatus());
    }
}