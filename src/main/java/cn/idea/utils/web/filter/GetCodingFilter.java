package cn.idea.utils.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * GET请求参数转为utf8编码
 */
public class GetCodingFilter implements Filter {
    private String charset = "UTF-8";

    public void init(FilterConfig filterConfig) throws ServletException {
        String charset = filterConfig.getInitParameter("charset");
        if (charset != null && !charset.isEmpty()) {
            this.charset = charset;
        }
    }

    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equalsIgnoreCase("GET")) {
            if (!(req instanceof GetRequest)) {
                // 可以通过Guava中的Utf8来进行判断
                req = new GetRequest(req, charset);//处理get请求编码
            }
        }
        chain.doFilter(req, servletResponse);
    }

    public void destroy() {

    }

    /**
     * GET请求参数处理
     */
    public static class GetRequest extends HttpServletRequestWrapper {
        private HttpServletRequest request;
        private Charset originalCharset = Charset.forName("ISO-8859-1");
        private Charset convertCharset;

        public GetRequest(HttpServletRequest request, String charset) {
            super(request);
            this.request = request;
            this.convertCharset = Charset.forName(charset);
        }

        public String getParameter(String name) {
            // 获取参数
            String value = request.getParameter(name);
            return (value == null ? null : convert(value));
        }

        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> map = request.getParameterMap();
            if (map == null) return null;
            // 遍历map，对每个值进行编码处理
            for (String key : map.keySet()) {
                String[] values = map.get(key);
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = convert(values[i]);
                    }
                }
            }
            // 处理后返回
            return map;
        }

        public String[] getParameterValues(String name) {
            String[] values = request.getParameterValues(name);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = convert(values[i]);
                }
            }
            return values;
        }

        // 将字符格式转换
        private String convert(String value) {
            return new String(value.getBytes(originalCharset), convertCharset);
        }
    }
}
