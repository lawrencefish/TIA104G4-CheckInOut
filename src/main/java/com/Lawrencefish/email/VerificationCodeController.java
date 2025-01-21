package com.Lawrencefish.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/verification")
public class VerificationCodeController {

    @Value("${spring.mail.username}")
    private String checkIOEmailName;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VerificationCodeService verificationCodeService;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @PostMapping("/send")
    public String sendVerificationCode(@RequestParam String email) {
        String code = verificationCodeService.generateCode(); // 使用服務生成驗證碼
//        System.out.println(code);
        verificationCodes.put(email, code); // 存儲驗證碼
        try {
            sendEmail(email, code); // 發送驗證碼到郵箱
            return "驗證碼已發送至 " + email;
        } catch (MessagingException e) {
            e.printStackTrace();
            return "驗證碼發送失敗，請稍後再試！";
        }
    }

    @GetMapping("/check")
    public boolean checkVerificationCode(@RequestParam String email, @RequestParam String code) {
        return code.equals(verificationCodes.get(email)); // 驗證碼是否匹配
    }

    @PostMapping("/member/send")
    public String sendMemberRegisterVerificationCode(@RequestParam String email) {
//        System.out.println(code);
        try {
        	verificationCodeService.sendMemberVerificationCode(email);        
            return "驗證碼已發送至 " + email;
        } catch (MessagingException e) {
            e.printStackTrace();
            return "驗證碼發送失敗，請稍後再試！";
        }
    }

	@PostMapping("/register/check")
	public ResponseEntity<Map<String, Object>> eamilCheck(@RequestParam Map<String, String> check, HttpSession session){
		Map<String, Object> response = new HashMap<>();
		String code = check.get("code");
		String email = check.get("email");
		System.out.println(check);
		if (verificationCodeService.verifyCodeForMemberService(email,code)) {
			session.setAttribute(email, "checked");
			response.put("message", "email驗證成功");
			response.put("verify", "ok");
		}else {
			response.put("message", "email驗證失敗，請重新輸入或重新寄送");
		}
		return ResponseEntity.ok(response);
	}

    

    private void sendEmail(String email, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("checkIOEmailName");
        helper.setTo(email);
        helper.setSubject("CheckInOut 業者註冊驗證碼");
        helper.setText("您的驗證碼是：" + code, true);

//        mailSender.send(message);
        //        mailSender.send(message);
//        new Thread(()->mailSender.send(message)).start();
        new Thread(()->mailSender.send(message)).start();
    }
}