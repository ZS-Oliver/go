package cn.idea.modules.security.web;

import cn.idea.modules.common.consts.UserConst;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.security.vo.PwdPair;
import cn.idea.modules.security.vo.UserVo;
import cn.idea.utils.kits.HashKit;
import cn.idea.utils.mock.test.MyTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.testng.annotations.Test;

public class UserControllerTest extends MyTxTestNGSpringContextTests {
    private static final String ROOT_PATH = "/user";
    MockHttpSession session;

    @Test
    public void testLogin() throws Exception {
        MockHttpSession session4Login = new MockHttpSession();
        UserVo uv = new UserVo();
        uv.setPhone("15232786339");
        uv.setPwd(UserConst.DEFAULT_PWD);
        mockMvc.perform(postJson(session4Login, ROOT_PATH, uv))
                .andExpect(isOKStatus());
    }

    @Test
    public void testLoginForTest() throws Exception {
        adminLoginSuccess();
    }

    @Test
    public void testLogout() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(deleteJson(session, ROOT_PATH))
                .andExpect(isOKStatus());
    }

    @Test
    public void testInfo() throws Exception {
        session = marketingManagerLoginSuccess();
        mockMvc.perform(getJson(session, ROOT_PATH))
                .andExpect(isOKStatus());
        session = employeeLoginSuccess();
        mockMvc.perform(getJson(session, ROOT_PATH))
                .andExpect(isOKStatus());
        session = adminLoginSuccess();
        mockMvc.perform(getJson(session, ROOT_PATH))
                .andExpect(isOKStatus());
    }

    @Test
    public void testUpdateInfo() throws Exception {
        session = marketingManagerLoginSuccess();
        MarketingManagerVo rightMv = new MarketingManagerVo();
        rightMv.setPhone("15232786339");
        rightMv.setSchool("重庆大学");

        mockMvc.perform(putJson(session, ROOT_PATH + "/marketingManager", rightMv))
                .andExpect(isOKStatus());

        MarketingManagerVo badMv = new MarketingManagerVo();
        badMv.setName("lisi");
        badMv.setPhone("13001421225");

        mockMvc.perform(putJson(session, ROOT_PATH + "/marketingManager", badMv))
                .andExpect(isConflictStatus());
    }

    @Test
    public void testUpdateInfo4Employee() throws Exception {
        session = employeeLoginSuccess();
        EmployeeVo rightVo = new EmployeeVo();
        rightVo.setPhone("13011110000");
        rightVo.setName("lisi");
        mockMvc.perform(putJson(session, ROOT_PATH + "/employee", rightVo))
                .andExpect(isOKStatus());

        EmployeeVo badVo = new EmployeeVo();
        badVo.setName("lisi");
        badVo.setPhone("15232786339");
        mockMvc.perform(putJson(session, ROOT_PATH + "/employee", badVo))
                .andExpect(isConflictStatus());
    }

    @Test
    public void testUpdatePwd() throws Exception {
        PwdPair pwdPair = new PwdPair();
        pwdPair.setOldPwd(UserConst.DEFAULT_PWD);
        pwdPair.setNewPwd(HashKit.md5("87654321"));
        session = marketingManagerLoginSuccess();
        mockMvc.perform(putJson(session, ROOT_PATH + "/pwd", pwdPair))
                .andExpect(isOKStatus());

        session = employeeLoginSuccess();
        pwdPair.setOldPwd(HashKit.md5("12345677"));
        mockMvc.perform(putJson(session, ROOT_PATH + "/pwd", pwdPair))
                .andExpect(isConflictStatus());
    }

    @Test
    public void testResetMarketingManagerPwd() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(putJson(session, ROOT_PATH + "/marketingManager/pwd/reset/7"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testResetEmployeePwd() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(putJson(session, ROOT_PATH + "/employee/pwd/reset/1"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testAcquireAuth() throws Exception {
        session = employeeLoginSuccess();
        mockMvc.perform(getJson(session, ROOT_PATH + "/auth"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testUnauthc() throws Exception {
    }
}