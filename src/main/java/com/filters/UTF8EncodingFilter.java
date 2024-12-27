package com.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

public class UTF8EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可選初始化邏輯
//        System.out.println("UTF8EncodingFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 設置請求和響應的編碼為 UTF-8
//        System.out.println("UTF8EncodingFilter doFilter");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 繼續執行其他過濾器或請求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 可選銷毀邏輯
//        System.out.println("UTF8EncodingFilter destroy");
    }
}