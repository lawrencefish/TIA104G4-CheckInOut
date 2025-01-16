package com.Lawrencefish.email;


import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeService {
    @Value("${spring.mail.username}")
    private String checkIOEmailName;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmployeeService employeeService;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // 生成 0-9 的隨機數
        }
        return code.toString();
    }

    public boolean sendVerificationCode(String taxId) throws MessagingException {
        Optional<HotelVO> hotelOpt = hotelService.findByTaxId(taxId);
        if (hotelOpt.isPresent()) {
            String email = hotelOpt.get().getEmail();
            String code = generateCode();
            verificationCodes.put(taxId, code);
            sendEmail(email, code);
            return true;
        }
        return false;
    }

    // 新增方法，用於員工的驗證碼寄送
    public boolean createAndSendVerificationCodeForEmployee(String employeeNumber) throws MessagingException {
        Optional<EmployeeVO> employeeOpt = employeeService.findByEmployeeNumber(employeeNumber);
        if (employeeOpt.isPresent()) {
            String email = employeeOpt.get().getEmail();
            String code = generateCode(); // 生成隨機驗證碼
            verificationCodes.put(employeeNumber, code);
            sendEmail(email, code); // 寄送郵件
            return true;
        }
        return false; // 如果找不到員工，返回 false
    }

    public boolean verifyCode(String taxId, String code) {
        return code.equals(verificationCodes.get(taxId));
    }

    private void sendEmail(String to, String code) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("驗證碼");
//        message.setText("您的驗證碼是：" + code);
//        mailSender.send(message);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("checkIOEmailName");
        helper.setTo(to);
        helper.setSubject("驗證碼");
        helper.setText("您的驗證碼是：" + code, true);
        new Thread(()->mailSender.send(message)).start();
    }


//    private String generateCode() {
//        return String.valueOf(new Random().nextInt(900000) + 100000); // 產生 6 位數隨機驗證碼
//    }
}