package com.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class AuthenticationFilter implements Filter {

    // 需要登入的頁面
    private static final List<String> PROTECTED_PATHS = Arrays.asList(
            "/business", "/frontDesk", "/orders", "/report", "/account", "employee"
    );
    
    // 管理員需要登入的頁面 -byBarry
    private static final List<String> ADMIN_PROTECTED_PATHS = Arrays.asList(
    		"/admin/adminBackend", "/admin/list", "/admin/edit"
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

        // 如果是需要認證的路徑，進行 session 驗證
        if (PROTECTED_PATHS.stream().anyMatch(path::startsWith)) {
            Object hotel = httpRequest.getSession().getAttribute("hotel");
            Object employee = httpRequest.getSession().getAttribute("employee");

            if (hotel == null) {
                // 如果未登入，跳轉到登入頁
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            } else if (employee == null) {
                httpResponse.sendRedirect(contextPath + "/login/employee");
                return;
            }
        }
        
        // 管理員路徑驗證 -byBarry
        if (ADMIN_PROTECTED_PATHS.stream().anyMatch(path::startsWith)) {
        	Object admin = httpRequest.getSession().getAttribute("adminId");
        	if(admin == null ) {
        		httpResponse.sendRedirect(contextPath + "/admin/login");
        		return;
        	}
        }

        // 如果路徑不需要認證或已通過驗證，繼續處理請求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 可選銷毀邏輯
    }
}
