package com.configs;

import com.filters.UTF8EncodingFilter;
import com.filters.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UTF8EncodingFilter> utf8EncodingFilter() {
        FilterRegistrationBean<UTF8EncodingFilter> registrationBean = new FilterRegistrationBean<>();

        // 註冊自定義的 UTF8EncodingFilter
        registrationBean.setFilter(new UTF8EncodingFilter());
        // 設置適用的 URL 路徑，這裡是所有路徑
        registrationBean.addUrlPatterns("/*");
        // 設置 Filter 的執行順序，數字越小優先執行
        registrationBean.setOrder(1);
        // 設置 Filter 名稱（可選）
        registrationBean.setName("UTF8EncodingFilter");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        // 註冊自定義的 AuthenticationFilter
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/*"); // 適用於所有路徑
        registrationBean.setName("AuthenticationFilter");
        registrationBean.setOrder(2); // Filter 執行順序，數字越小越優先

        return registrationBean;
    }
}