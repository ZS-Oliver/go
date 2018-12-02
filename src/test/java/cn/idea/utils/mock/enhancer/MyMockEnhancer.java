package cn.idea.utils.mock.enhancer;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 简化了常用的一些操作
 */
public interface MyMockEnhancer {
    /**
     * 提供了MockMvc初始化的抽取
     */
    default MockMvc initMockMvc(WebApplicationContext wac) {
        // 参照：http://blog.csdn.net/a35038438/article/details/50216673
        SecurityManager securityManager = (SecurityManager) wac.getBean("securityManager");
        SecurityUtils.setSecurityManager(securityManager);
        return MockMvcBuilders.webAppContextSetup(wac)
                   .alwaysDo(MockMvcResultHandlers.print())
                   .alwaysDo(result -> {
                       Class<MockHttpServletRequest> clazz = MockHttpServletRequest.class;
                       Field content = clazz.getDeclaredField("content");
                       content.setAccessible(true);
                       byte[] bytes = (byte[]) content.get(result.getRequest());

                       if (bytes == null || bytes.length == 0) {
                           return;
                       }

                       String s = new String(bytes, StandardCharsets.UTF_8);
                       System.out.println("=====> RequestBody:" + s);
                   })
                   .build();
    }

    /**
     * 为一般的POST请求加入为JSON的contentType及accept
     *
     * @param path 请求路径
     */
    default MockHttpServletRequestBuilder postJson(String path, Object json) {
        return MockMvcRequestBuilders.post(path).content(JSON.toJSONString(json)).contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * 为一般的POST请求加入为JSON的contentType及accept
     *
     * @param path    请求路径
     * @param session 采用的session
     * @param obj     要发送的对象（不需要转为JSON）
     */
    default MockHttpServletRequestBuilder postJson(MockHttpSession session, String path, Object obj) {
        return MockMvcRequestBuilders.post(path).content(JSON.toJSONString(obj))
                   .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
                   .session(session);
    }

    /**
     * 为一般的POST请求加入为JSON的accept
     *
     * @param path    请求路径
     * @param session 采用的session
     */
    default MockHttpServletRequestBuilder postJson(MockHttpSession session, String path) {
        return MockMvcRequestBuilders.post(path)
                   .accept(MediaType.APPLICATION_JSON_UTF8)
                   .session(session);
    }

    /**
     * 为一般的PUT请求加入为JSON的contentType及accept
     *
     * @param path    请求路径
     * @param session 采用的session
     * @param obj     要发送的对象（不需要转为JSON）
     */
    default MockHttpServletRequestBuilder putJson(MockHttpSession session, String path, Object obj) {
        return MockMvcRequestBuilders.put(path).content(JSON.toJSONString(obj))
                   .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
                   .session(session);
    }

    /**
     * 为一般的PUT请求加入为JSON的accept
     *
     * @param path    请求路径
     * @param session 采用的session
     */
    default MockHttpServletRequestBuilder putJson(MockHttpSession session, String path) {
        return MockMvcRequestBuilders.put(path)
                   .accept(MediaType.APPLICATION_JSON_UTF8)
                   .session(session);
    }

    /**
     * 为一般的GET请求加入为JSON的accept
     *
     * @param path 请求路径
     */
    default MockHttpServletRequestBuilder getJson(String path, Object... uriVars) {
        return MockMvcRequestBuilders.get(path, uriVars).accept(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * 为一般的GET请求加入为JSON的accept(带有session)
     *
     * @param path 请求路径
     */
    default MockHttpServletRequestBuilder getJson(MockHttpSession session, String path, Object... uriVars) {
        return MockMvcRequestBuilders.get(path, uriVars).accept(MediaType.APPLICATION_JSON_UTF8).session(session);
    }

    /**
     * 为一般的DELETE请求加入session
     *
     * @param path 请求路径
     */
    default MockHttpServletRequestBuilder deleteJson(MockHttpSession session, String path, Object... uriVars) {
        return MockMvcRequestBuilders.delete(path, uriVars).session(session).accept(MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * 用于判断返回值为单一字符串的JSON
     */
    default ResultMatcher singleStr(String str) {
        return content().string(Strings.dquote(str));
    }

    /**
     * 用于判断返回值和期望值的JSON宽松匹配
     */
    default ResultMatcher jsonMatch(Object obj) {
        return content().json(JSON.toJSONString(obj));
    }

    /**
     * 返回的CODE为CONFLICT，为标准的业务错误状态
     */
    default ResultMatcher isConflictStatus() {
        return status().isConflict();
    }

    /**
     * 返回的CODE为OK，为成功状态
     */
    default ResultMatcher isOKStatus() {
        return status().isOk();
    }

    /**
     * 返回的CODE为BAD_REQUEST，为标准参数校验问题
     */
    default ResultMatcher isBadRequestStatus() {
        return status().isBadRequest();
    }
}
