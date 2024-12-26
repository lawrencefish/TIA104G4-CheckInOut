//package com.business.filter;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@WebFilter(urlPatterns = "/*") // 过滤所有请求
//public class LoginFilter implements Filter {
//
//    // 定义不需要过滤的白名单路径
//    private static final List<String> WHITELIST_PATHS = Arrays.asList(
//            "/business/login-1", // 商家登录页
//            "/business/login-2", // 员工登录页
//            "/assets/", // 静态资源
//            "/public/" // 公共开放 API
//    );
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // 初始化过滤器（可选）
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        // 防止緩存
//        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//        httpResponse.setHeader("Pragma", "no-cache");
//        httpResponse.setDateHeader("Expires", 0);
//
//        // 获取当前的请求路径
//        String path = httpRequest.getRequestURI();
//
//        // 检查是否属于白名单路径
//        if (isWhitelisted(path)) {
//            chain.doFilter(request, response); // 放行
//            return;
//        }
//
//        // 检查 session 中的登录状态
//        Object hotelSession = httpRequest.getSession().getAttribute("hotel");
//        Object employeeSession = httpRequest.getSession().getAttribute("employee");
//
//        if (hotelSession == null && employeeSession == null) {
//            // 如果都未登录，跳转到商家登录页面
//            httpResponse.sendRedirect("/business/login-1");
//            return;
//        }
//
//        if (employeeSession == null) {
//            // 如果需要员工登录但没有对应 session
//            httpResponse.sendRedirect("/business/login-2");
//            return;
//        }
//
//        // 用户已登录，继续请求
//        chain.doFilter(request, response);
//    }
//
//    private boolean isWhitelisted(String path) {
//        // 检查路径是否在白名单中
//        return WHITELIST_PATHS.stream().anyMatch(path::startsWith);
//    }
//
//    @Override
//    public void destroy() {
//        // 销毁过滤器（可选）
//    }
//}
