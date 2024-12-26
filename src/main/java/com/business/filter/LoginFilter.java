package com.business.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.Filter;

@WebFilter(urlPatterns = "/*") // 过滤所有请求
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化过滤器（可选）
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取当前的请求路径
        String path = httpRequest.getRequestURI();

        // 不需要验证的路径（登录页、静态资源等）
        if (path.startsWith("/login-1") || path.startsWith("/login-2") || path.startsWith("/assets")) {
            chain.doFilter(request, response);
            return;
        }

        // 从 session 中检查用户是否已登录
        Object hotelSession = httpRequest.getSession().getAttribute("hotel");
        Object employeeSession = httpRequest.getSession().getAttribute("employee");

        if (hotelSession == null && employeeSession == null) {
            // 如果都未登录，跳转到登录页面
            httpResponse.sendRedirect("/business/login-1");
            return;
        }

        if (employeeSession == null) {
            // 如果需要员工登录但没有对应 session
            httpResponse.sendRedirect("/business/login-2");
            return;
        }

        // 用户已登录，继续请求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁过滤器（可选）
    }
}
