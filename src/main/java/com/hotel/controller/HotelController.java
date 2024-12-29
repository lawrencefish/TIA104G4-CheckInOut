package com.hotel.controller;

import com.employee.model.EmployeeVO;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/byCity")
    public String getHotelByCity(@RequestParam String city, Model model) {
        List<HotelVO> hotels = hotelService.findByCity(city);
        model.addAttribute("hotels", hotels);
        return "hotelList";  // Thymeleaf頁面，顯示結果
    }

    @PostMapping("/save")
    public String saveHotel(@ModelAttribute HotelVO hotel, Model model) {
        hotelService.saveHotel(hotel);
        // ...
        return "redirect:/hotel/all";
    }

    @GetMapping("/introduce/{hotelId}")
    public String showHotelIntroduce(@PathVariable Integer hotelId, Model model) {
        // 使用服務層獲取酒店及其圖片數據
        HotelVO hotelWithImages = hotelService.getHotelWithImages(hotelId);
        model.addAttribute("hotel", hotelWithImages);
        return "business/hotelIntroduce"; // Thymeleaf模板名稱
    }

    @PostMapping("setPassword")
    public String setPassword(
            HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordCheck") String newPasswordCheck,
            RedirectAttributes redirectAttributes) {

        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");

        // 驗證輸入
        if (oldPassword == null || oldPassword.trim().isEmpty() ||
                newPassword == null || newPassword.trim().isEmpty() ||
                newPasswordCheck == null || newPasswordCheck.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "所有欄位均為必填");
            System.out.println("錯誤1");
            return "redirect:/account/accountSet"; // 返回包含彈窗的頁面
        }

        if (!newPassword.equals(newPasswordCheck)) {
            redirectAttributes.addFlashAttribute("error", "新密碼與確認密碼不一致");
            System.out.println("錯誤2");
            return "redirect:/account/accountSet";
        }

        if (!oldPassword.equals(hotel.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "舊密碼不正確");
            System.out.println("錯誤3");
            return "redirect:/account/accountSet";
        }

        // 更新密碼
        hotel.setPassword(newPassword);
        hotelService.updateHotelPassword(hotel.getHotelId(), newPassword);
        System.out.println("成功");
        request.getSession().setAttribute("hotel", hotel);

        return "redirect:/account/accountSet"; // 返回頁面以顯示成功消息
    }
}
