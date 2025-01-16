package com.Lawrencefish.email;

import com.employee.model.EmployeeService;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private EmployeeService employeeService;
    /**
     * 發送驗證碼至註冊的 Email
     */
    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String taxId) throws MessagingException {
        boolean sent = verificationCodeService.sendVerificationCode(taxId);
        if (sent) {
            return ResponseEntity.ok("驗證碼已寄送至註冊信箱");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無法找到對應的統一編號，請確認輸入是否正確！");
        }
    }

    /**
     * 驗證驗證碼
     */
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String taxId, @RequestParam String code) {
        boolean valid = verificationCodeService.verifyCode(taxId, code);
        if (valid) {
            return ResponseEntity.ok("驗證碼正確");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("驗證碼錯誤或已過期");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String taxId, @RequestParam String newPassword) {
        // 查詢對應的 HotelVO
        Optional<HotelVO> optionalHotel = hotelService.findByTaxId(taxId);

        if (optionalHotel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("統一編號不存在！");
        }

        HotelVO hotel = optionalHotel.get();
        hotel.setPassword(newPassword); // 假設密碼是明文存儲，建議改為加密存儲

        // 保存更新後的數據
        try {
            hotelService.save(hotel);
            return ResponseEntity.ok("密碼變更成功！");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("密碼變更失敗，請稍後再試！");
        }
    }
}