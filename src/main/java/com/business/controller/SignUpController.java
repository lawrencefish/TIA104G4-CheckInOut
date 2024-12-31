package com.business.controller;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.facility.model.FacilityService;
import com.facility.model.FacilityVO;
import com.googleAPI.GeocodingService;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import com.hotelFacility.model.HotelFacilityService;
import com.hotelFacility.model.HotelFacilityVO;
import com.hotelImg.model.HotelImgService;
import com.hotelImg.model.HotelImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
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
    @Autowired
    private HotelImgService hotelImgService;
    @Autowired
    private HotelFacilityService hotelFacilityService;


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
    public String createHotel(
            @Valid @ModelAttribute("hotelVO") HotelVO hotelVO,
            BindingResult bindingResult,
            @RequestParam("idFront") MultipartFile idFrontFile,
            @RequestParam("idBack") MultipartFile idBackFile,
            @RequestParam("license") MultipartFile licenseFile,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model) {
        System.out.println("有接受到");

//        // 1. 表單驗證
//        if (bindingResult.hasErrors()) {
//            System.out.println("表單驗證不通過");
//            return "business/signUp-1"; // 返回頁面
//        }

        // 2. 驗證檔案
        if (idFrontFile.isEmpty()) {
            model.addAttribute("duplicateError", "所有照片皆需上傳！");
            System.out.println("沒有照片");
            return "business/signUp-1";
        }
        if (idBackFile.isEmpty()) {
            model.addAttribute("duplicateError", "所有照片皆需上傳！");
            System.out.println("沒有照片");
            return "business/signUp-1";
        }
        if (licenseFile.isEmpty()) {
            model.addAttribute("duplicateError", "所有照片皆需上傳！");
            System.out.println("沒有照片");
            return "business/signUp-1";
        }

        // 3. 檢查唯一性約束
        if (hotelService.existsByTaxId(hotelVO.getTaxId())) {
            model.addAttribute("duplicateError", "統一編號已存在，請確認資料！");
            return "business/signUp-1";
        }
        if (hotelService.existsByName(hotelVO.getName())) { // 檢查 hotel.name 的唯一性
            model.addAttribute("duplicateError", "旅館名稱已存在，請確認資料！");
            return "business/signUp-1";
        }
        if (hotelService.existsByEmail(hotelVO.getEmail())) {
            model.addAttribute("duplicateError", "電子郵件已存在，請確認資料！");
            return "business/signUp-1";
        }
        if (hotelService.existsByPhoneNumber(hotelVO.getPhoneNumber())) {
            model.addAttribute("duplicateError", "電話號碼已存在，請確認資料！");
            return "business/signUp-1";
        }

        // 4. 處理文件上傳
        try {
            hotelVO.setIdFront(idFrontFile.getBytes());
            hotelVO.setIdBack(idBackFile.getBytes());
            hotelVO.setLicense(licenseFile.getBytes());
        } catch (IOException e) {
            model.addAttribute("duplicateError", "文件上傳過程中出錯，請重新嘗試！");
            System.out.println("文件上傳過程中出錯");
            return "business/signUp-1";
        }

        // 5. 保存資料
        try {
            hotelService.save(hotelVO);
        } catch (Exception e) {
            model.addAttribute("duplicateError", "保存資料時發生錯誤，請稍後再試！");
            System.out.println("保存資料時發生錯誤");
            return "business/signUp-1";
        }

        // 6. 添加成功訊息並跳轉
//        redirectAttributes.addFlashAttribute("successMessage", "旅館新增成功！");
        // 驗證成功 -> 存入 Session
//        HotelVO refreshedHotel = hotelService.findByTaxId(hotelVO.getTaxId())
//                .orElseThrow(() -> new IllegalStateException("保存後無法找到旅館信息！"));

        // 保存到 Session
        request.getSession().setAttribute("hotel", hotelVO);
        System.out.println("旅館新增成功");
        return "redirect:/signUp/signUp-2"; // 跳轉到下一步
    }


    @GetMapping("/signUp-2")
    public String showSignUp2(HttpServletRequest request, Model model) {
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");
        Integer hotelId = hotel.getHotelId();
        model.addAttribute("hotelId", hotelId);
        return "business/signup-2";
    }
//
//    @PostMapping("/signUp-2")
//    public String handleStepTwo(
//            @RequestParam("photos") List<MultipartFile> photos,
//            @RequestParam(value = "facilities", required = false) List<Integer> facilityIds,
//            HttpSession session,
//            HttpServletRequest request,
//            RedirectAttributes redirectAttributes) {
//        try {
//            // 從 Session 中獲取 hotelVO
//            HotelVO hotelVO = (HotelVO) session.getAttribute("hotel");
//            if (hotelVO == null) {
//                redirectAttributes.addFlashAttribute("error", "未找到旅館信息，請重新開始註冊流程！");
//                return "redirect:/signUp/signUp-1";
//            }
//
//            // 處理照片上傳並存入 HotelImgVO
//            if (photos != null && !photos.isEmpty()) {
//                for (MultipartFile photo : photos) {
//                    if (!photo.isEmpty()) {
//                        try {
//                            HotelImgVO hotelImg = new HotelImgVO();
//                            hotelImg.setHotel(hotelVO);
//                            hotelImg.setPicture(photo.getBytes());
//                            // 保存照片至資料庫
//                            hotelImgService.save(hotelImg);
//                            System.out.println("照片保存成功");
//                        } catch (IOException e) {
//                            redirectAttributes.addFlashAttribute("error", "照片保存失敗：" + e.getMessage());
//                            return "redirect:/signUp/signUp-2";
//                        }
//                    }
//                }
//            }
//
//            // 處理設施並存入 HotelFacilityVO
//            // 3. 處理設施並存入 HotelFacilityVO
//            if (facilityIds != null && !facilityIds.isEmpty()) {
//                for (Integer facilityId : facilityIds) {
//                    try {
//                        FacilityVO facility = facilityService.findById(facilityId)
//                                .orElseThrow(() -> new IllegalArgumentException("無效的設施 ID: " + facilityId));
//
//                        HotelFacilityVO hotelFacility = new HotelFacilityVO();
//                        hotelFacility.setHotel(hotelVO); // 關聯 HotelVO
//                        hotelFacility.setFacility(facility); // 關聯 FacilityVO
//
//                        // 保存設施至資料庫
//                        hotelFacilityService.save(hotelFacility);
//                        System.out.println("設施 ID: " + facilityId + " 保存成功");
//                    } catch (Exception e) {
//                        System.err.println("設施處理失敗，ID: " + facilityId + "，錯誤：" + e.getMessage());
//                        redirectAttributes.addFlashAttribute("error", "設施處理失敗，ID: " + facilityId);
//                        return "redirect:/signUp/signUp-2";
//                    }
//                }
//            }
//
//
////            redirectAttributes.addFlashAttribute("success", "照片與設施已成功儲存！");
//
//            System.out.println("成功");
//            request.getSession().setAttribute("hotel", hotelVO);
//            return "redirect:/signUp/signUp-3"; // 跳轉到下一步
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "設施更新失敗：" + e.getMessage());
//            return "redirect:/signUp/signUp-2";
//        }
//    }
//

    @GetMapping("/signUp-3")
    public String showSignUp3(ModelMap model) {
        EmployeeVO employeeVO = new EmployeeVO();
        model.addAttribute("employeeVO", employeeVO);
        return "business/signup-3";
    }


    @PostMapping("/signUp-3")
    public String handleSignUpStepThree(
            @Valid @ModelAttribute("employeeVO") EmployeeVO employeeVO,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 從 Session 中獲取 HotelVO
        HotelVO hotelVO = (HotelVO) session.getAttribute("hotel");
        if (hotelVO == null) {
            redirectAttributes.addFlashAttribute("error", "無法取得飯店資訊，請重新登入。");
            return "redirect:/signUp/signUp-1";
        }

        // 檢查表單驗證錯誤
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeVO", bindingResult);
            redirectAttributes.addFlashAttribute("employeeVO", employeeVO);
            return "business/signUp-3";
        }

        // 確認密碼是否匹配
        if (!employeeVO.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "密碼與確認密碼不一致！");
            redirectAttributes.addFlashAttribute("employeeVO", employeeVO);
            return "business/signUp-3";
        }

        // 檢查員工編號或 Email 是否在該飯店中重複
        if (employeeService.existsByEmployeeNumberAndHotel(employeeVO.getEmployeeNumber(), hotelVO.getHotelId())) {
            redirectAttributes.addFlashAttribute("error", "該員工編號在此飯店中已存在，請使用其他編號。");
            redirectAttributes.addFlashAttribute("employeeVO", employeeVO);
            return "business/signUp-3";
        }

        if (employeeService.existsByEmailAndHotel(employeeVO.getEmail(), hotelVO.getHotelId())) {
            redirectAttributes.addFlashAttribute("error", "該電子信箱在此飯店中已存在，請更換其他信箱。");
            redirectAttributes.addFlashAttribute("employeeVO", employeeVO);
            return "business/signUp-3";
        }

        // 設置 HotelVO 到 EmployeeVO 並保存
        employeeVO.setHotel(hotelVO);
        employeeService.save(employeeVO);

//        redirectAttributes.addFlashAttribute("success", "負責人帳號已成功設定！");
        return "redirect:/signUp/successful";
    }


    @GetMapping("/successful")
    public String showSuccessful(HttpSession session) {
        // 清除整個 session
        session.invalidate();
        return "business/signupSuccessful";
    }
}
