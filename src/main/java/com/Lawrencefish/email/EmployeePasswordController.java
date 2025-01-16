package com.Lawrencefish.email;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee/forgot-password")
public class EmployeePasswordController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String employeeNumber) throws MessagingException {
        boolean isSent = verificationCodeService.createAndSendVerificationCodeForEmployee(employeeNumber);
        if (isSent) {
            return ResponseEntity.ok("驗證碼已寄送至員工的電子郵件！");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該員工的電子郵件！");
    }



    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmployeeCode(
            @RequestBody Map<String, String> payload) {
        String employeeNumber = payload.get("employeeNumber");
        String code = payload.get("verificationCode");

        if (employeeNumber == null || code == null) {
            return ResponseEntity.badRequest().body("請提供完整的資訊！");
        }

        boolean isValid = verificationCodeService.verifyCode(employeeNumber, code);
        if (!isValid) {
            return ResponseEntity.badRequest().body("驗證碼錯誤！");
        }

        return ResponseEntity.ok("驗證成功");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String employeeNumber = payload.get("employeeNumber");
        String newPassword = payload.get("newPassword");

        // 確保所有字段都被填寫
        if (employeeNumber == null || newPassword == null) {
            return ResponseEntity.badRequest().body("請提供完整的資訊！");
        }

        // 查詢員工是否存在
        Optional<EmployeeVO> employeeOptional = employeeService.findByEmployeeNumber(employeeNumber);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("員工編號不存在！");
        }

        // 更新員工密碼
        EmployeeVO employee = employeeOptional.get();
        employee.setPassword(newPassword); // 密碼需加密存儲
        employeeService.save(employee);

        return ResponseEntity.ok("密碼重設成功！");
    }
}
