package com.business.controller;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.facility.model.FacilityService;
import com.facility.model.FacilityVO;
import com.googleAPI.GeocodingService;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/signUp")
public class SignUpController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private GeocodingService geocodingService;


    @GetMapping("")
    public String show() {
        return "redirect:/signUp/signUp-1";
    }

    @GetMapping("/signUp-1")
    public String showSignUp1(ModelMap model) {
        HotelVO hotelVO = new HotelVO();
        model.addAttribute("hotelVO", hotelVO);
        return "business/signup-1";
    }

    @PostMapping("/signUp-1")
    public String handleSignUp1(
            @Valid HotelVO hotelVO, // 綁定表單
            BindingResult result, // 用於檢查驗證結果
            ModelMap model,
            @RequestParam("idFront") MultipartFile idFront,
            @RequestParam("idBack") MultipartFile idBack,
            @RequestParam("license") MultipartFile license,
            HttpSession session
    ) {
        // 表單驗證
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                System.out.println("Field: " + error.getField() + ", Error: " + error.getDefaultMessage());
            });
            model.addAttribute("hotelDraft", hotelVO); // 確保錯誤資料返回前端
            return "business/signup-1";
        }

        // 驗證 taxId、name、address、phoneNumber、email 的唯一性
        if (hotelService.existsByTaxId(hotelVO.getTaxId())) {
            model.addAttribute("taxIdError", "此統一編號已存在");
            return "business/signup-1";
        }
        if (hotelService.existsByName(hotelVO.getName())) {
            model.addAttribute("nameError", "此公司名稱已存在");
            return "business/signup-1";
        }
        if (hotelService.existsByAddress(hotelVO.getAddress())) {
            model.addAttribute("addressError", "此地址已存在");
            return "business/signup-1";
        }
        if (hotelService.existsByPhoneNumber(hotelVO.getPhoneNumber())) {
            model.addAttribute("phoneNumberError", "此電話號碼已存在");
            return "business/signup-1";
        }
        if (hotelService.existsByEmail(hotelVO.getEmail())) {
            model.addAttribute("emailError", "此電子信箱已存在");
            return "business/signup-1";
        }

        // 上傳照片處理
        boolean hasFileError = false;

        // 處理 idFront 文件
        if (idFront.isEmpty()) {
            System.out.println("未選擇圖片");
            model.addAttribute("idFrontError", "負責人身份證正面不可為空");
            hasFileError = true;
        } else {
            try {
                hotelVO.setIdFront(idFront.getBytes());
            } catch (IOException e) {
                model.addAttribute("idFrontError", "上傳負責人身份證正面失敗");
                hasFileError = true;
            }
        }

        // 處理 idBack 文件
        if (idBack.isEmpty()) {
            System.out.println("未選擇圖片");
            model.addAttribute("idBackError", "負責人身份證反面不可為空");
            hasFileError = true;
        } else {
            try {
                hotelVO.setIdBack(idBack.getBytes());
            } catch (IOException e) {
                model.addAttribute("idBackError", "上傳負責人身份證反面失敗");
                hasFileError = true;
            }
        }

        // 處理 license 文件
        if (license.isEmpty()) {
            System.out.println("未選擇圖片");
            model.addAttribute("licenseError", "旅館業登記證 / 民宿登記證不可為空");
            hasFileError = true;
        } else {
            try {
                hotelVO.setLicense(license.getBytes());
            } catch (IOException e) {
                model.addAttribute("licenseError", "上傳旅館業登記證失敗");
                hasFileError = true;
            }
        }

        // 如果有檔案錯誤，返回頁面
        if (hasFileError) {
            System.out.println("錯誤QQ");
            return "business/signup-1";
        }

        // 暫存 VO 至 Session
        hotelService.addHotel(hotelVO);


        return "redirect:/signUp/signUp-2";
    }



    @GetMapping("/signUp-2")
    public String showSignUp2() {
        return "business/signup-2";
    }

    @PostMapping("/signUp-2")
    public String handleSignUp2(@RequestParam("infoText") String infoText, HttpSession session) {
        HotelVO hotel = (HotelVO) session.getAttribute("hotelDraft");
        if (hotel == null) {
            return "redirect:/signUp/signUp-1";
        }

        // 保存額外信息
        hotel.setInfoText(infoText);
        session.setAttribute("hotelDraft", hotel);

        return "redirect:/signUp/signUp-3";
    }

    @GetMapping("/signUp-3")
    public String showSignUp3() {
        return "business/signup-3";
    }


    @PostMapping("/signUp-3")
    public String handleSignUp3(
            @RequestParam("employeeNumber") String employeeNumber,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("title") String title,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            HttpSession session
    ) {
        HotelVO hotel = (HotelVO) session.getAttribute("hotelDraft");
        if (hotel == null) {
            return "redirect:/signUp/signUp-1";
        }

        // 調用 GeocodingService 獲取經緯度
        try {
            String fullAddress = hotel.getCity() + hotel.getDistrict() + hotel.getAddress();
            Double[] coordinates = geocodingService.getCoordinatesFromAddress(fullAddress);
            if (coordinates != null) {
                hotel.setLatitude(coordinates[0]);
                hotel.setLongitude(coordinates[1]);
            } else {
                // 如果地址無法解析，返回錯誤提示
                session.setAttribute("errorMessage", "無法解析地址，請確認地址是否正確。");
                return "redirect:/signUp/signUp-3";
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "無法獲取經緯度，請稍後重試。");
            return "redirect:/signUp/signUp-3";
        }

        // 保存員工信息
        EmployeeVO employee = new EmployeeVO();
        employee.setEmployeeNumber(employeeNumber);
        employee.setName(name);
        employee.setPassword(password);
        employee.setTitle(title);
        employee.setPhoneNumber(phone);
        employee.setEmail(email);

        // 最後一步，保存到資料庫
        // 保存 HotelVO 和 EmployeeVO 到資料庫


        // 清除暫存資料
        session.removeAttribute("hotelDraft");












        // 清除暫存資料
        session.removeAttribute("hotelDraft");

        return "redirect:/signUp/successful";
    }

    @GetMapping("/successful")
    public String showSuccessful() {
        return "business/signupSuccessful";
    }
}
