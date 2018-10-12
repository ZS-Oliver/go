package cn.idea.utils.mock.enhancer;

import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.security.vo.UserVo;
import cn.idea.utils.mock.test.Users;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

/**
 * 用户相关操作的增强
 */
public interface MyUserMockEnhancer extends MyMockEnhancer {
    String USER_ROOT_PATH = "/user";
    String USER_TEMP_LOGIN_PATH = USER_ROOT_PATH + "/temp";

    /**
     * 拿到实现类的MockMvc
     */
    MockMvc mockMvc();

    // 通用的退出
    default void commonLogout(MockHttpSession session) throws Exception {
        mockMvc().perform(deleteJson(session, USER_ROOT_PATH));
        assertEmptySession(session);
    }

    // 模拟承运商请求登录成功
//    @CanIgnoreReturnValue
//    default MockHttpSession carrierLoginSuccess() {
//        UserVo carrier = Users.genCarrier(); // 承运商的登录信息
//        MockHttpSession carrierSession = new MockHttpSession(); // 承运商会话
//
//        try {
//            mockMvc().perform(postJson(carrierSession, USER_TEMP_LOGIN_PATH, carrier))
//                .andExpect(isOKStatus());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 校验session
//        assertEquals(carrierSession.getAttribute(SessionConst.UID), 7);
//        assertEquals(carrierSession.getAttribute(SessionConst.AUTH), Role.CARRIER.getCode());
//
//        return carrierSession;
//    }

    // 模拟管理员请求登录成功
    @CanIgnoreReturnValue
    default MockHttpSession adminLoginSuccess() {
        MockHttpSession adminSession = new MockHttpSession(); // 管理员会话

        try {
            mockMvc().perform(postJson(adminSession, USER_TEMP_LOGIN_PATH, Users.genAdmin()))
                    .andExpect(isOKStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 校验session
        assertEquals(adminSession.getAttribute(SessionConst.UID), SessionConst.ADMIN_UID);
        assertEquals(adminSession.getAttribute(SessionConst.AUTH), Role.ADMIN.getCode());

        return adminSession;
    }

    // 模拟员工登录成功
    @CanIgnoreReturnValue
    default MockHttpSession employeeSuccess() {
        UserVo employee = Users.genEmployee();
        MockHttpSession employeeSession = new MockHttpSession();

        try {
            mockMvc().perform(postJson(employeeSession, USER_TEMP_LOGIN_PATH, employee))
                    .andExpect(isOKStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(employeeSession.getAttribute(SessionConst.UID), 1);
        assertEquals(employeeSession.getAttribute(SessionConst.AUTH), Role.EMPLOYEE.getCode());

        return employeeSession;
    }

    // 判断session中没有存有用户信息
    default void assertEmptySession(MockHttpSession session) {
        assertNull(session.getAttribute(SessionConst.UID));
        assertNull(session.getAttribute(SessionConst.AUTH));
    }
}
