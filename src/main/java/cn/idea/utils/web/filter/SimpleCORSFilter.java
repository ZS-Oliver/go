package cn.idea.utils.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决跨域请求问题
 */
public class SimpleCORSFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        String origin = ((HttpServletRequest) req).getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:63342, chrome-extension://aicmkgpgakddgnaphhhpliifpcfhicfo");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 解决问题：https://www.jianshu.com/p/1ab7dfaf65ae
        if (((HttpServletRequest) req).getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
