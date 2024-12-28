//package com;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception ex, HttpServletRequest request, Model model) {
//        // 將錯誤訊息和請求資訊傳遞給模型
//        model.addAttribute("error", ex.getMessage());
//        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        model.addAttribute("path", request.getRequestURI());
//
//        // 返回自定義錯誤頁面
//        return "error";
//    }
//}
