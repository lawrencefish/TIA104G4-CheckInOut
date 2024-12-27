package com.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class AuthenticationFilter implements Filter {

    // 不需要登入的頁面
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/", "/index", "/login", "/signUp", "/signUp/signUp-1", "/signUp/signUp-2", "/signUp/signUp-3", "/signupSuccessful",
            "/logout", "/switch-user","/login/business", "/login/employee","/error" // 確保這些路徑不進行認證檢查
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可選初始化邏輯
//        System.out.println("login filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        String contextPath = httpRequest.getContextPath();

        // 排除靜態資源
        if (path.startsWith("/static/") || path.startsWith("/imgs/")) {
            chain.doFilter(request, response);
            return;
        }

        // 如果是排除的路徑，直接放行
        if (EXCLUDED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 檢查 session 中的 "hotel" 和 "employee"
        Object hotel = httpRequest.getSession().getAttribute("hotel");
        Object employee = httpRequest.getSession().getAttribute("employee");

        if (hotel == null) {
            // 如果未登入，跳轉到登入頁
            httpResponse.sendRedirect(contextPath + "/login");
        } else if(employee == null){
            httpResponse.sendRedirect(contextPath + "/login/employee");
        } else {
            // 如果已登入，繼續處理請求
//            System.out.println("有經過登入驗證");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // 可選銷毀邏輯
    }
}
