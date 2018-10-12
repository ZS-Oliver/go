package cn.idea.modules.marketingManager.web;

import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.utils.mock.test.MyTxTestNGSpringContextTests;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MarketingManagerControllerTest extends MyTxTestNGSpringContextTests {

    private MockHttpSession session;

    private static final String PATH = "/marketingManager";

    @BeforeMethod
    public void setup() {
        session = adminLoginSuccess();
    }

    @Test
    @Rollback(false)
    public void testCreate() throws Exception {
        MarketingManagerVo mv = new MarketingManagerVo();
        mv.setName("市场部经理[测试用]");
        mv.setPhone("13001421225");
        mv.setSchool("东北大学");
        mockMvc.perform(postJson(session, PATH, mv))
                .andExpect(isOKStatus());

    }

    @Test
    public void testList() throws Exception {
        session = adminLoginSuccess();
        mockMvc.perform(getJson(session, PATH)
                .param("name", "11"))
                .andExpect(isConflictStatus());
    }

    @Test
    public void testView() throws Exception {
        mockMvc.perform(getJson(session, PATH + "/7"))
                .andExpect(isOKStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        MarketingManagerVo mv = new MarketingManagerVo();
        mv.setName("张硕");
        mv.setPhone("11111111111");
        mockMvc.perform(putJson(session, PATH + "/7", mv))
                .andExpect(isOKStatus());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(deleteJson(session, PATH + "/7"))
                .andExpect(isOKStatus());
    }
}