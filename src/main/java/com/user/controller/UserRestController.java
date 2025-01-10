package com.user.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.member.model.MemberService;
import com.member.model.MemberVO;

@RestController
@RequestMapping("/user/api")
public class UserRestController {
	@Autowired
	MemberService memServ;
	@Autowired
	private Validator validator;

	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	// 登入
	@PostMapping("/login")
	public Map<String, String> memberLogin(@RequestBody Map<String, String> loginRequest, HttpSession session) {
		String account = loginRequest.get("account");
		String password = loginRequest.get("password");
		String url = loginRequest.get("url");

		Map<String, String> response = new HashMap<>();
		if (memServ.existsByAccount(account)) {
			MemberVO member = memServ.login(account, password);
			if (member != null) {
				System.out.println("登入成功" + member.getAccount());
				session.setAttribute("memberId", member.getMemberId());
				session.setAttribute("account", member.getAccount());
				response.put("status", "success");
				response.put("message", "登入成功");
			} else {
				response.put("status", "failed");
				response.put("message", "密碼錯誤，請重新輸入");
			}
		} else {
			response.put("status", "failed");
			response.put("message", "查無此人");
		}
		return response;
	}

	// 檢查登入狀態
	@PostMapping("/loginCheck")
	public Map<String, String> loginCheck(HttpSession session) {
		String memberId = String.valueOf(session.getAttribute("memberId"));
		String account = (String) session.getAttribute("account");
		String url = (String) session.getAttribute("url");
		Map<String, String> response = new HashMap<>();
		if (memberId != null && account != null) {
			response.put("memberId", memberId != null ? memberId : null);
			response.put("account", account != null ? account : null);
		} else {
			if (url != null) {
				response.put("message", "redirect");
				response.put("url", url);
				session.setAttribute("url", null);
			} else {
				response.put("message", "not login");
			}
		}
		return response;
	}

	// 登出
	@PostMapping("/logout")
	public Map<String, String> logout(HttpSession session) {
		String memberId = String.valueOf(session.getAttribute("memberId"));
		String account = (String) session.getAttribute("account");
		Map<String, String> response = new HashMap<>();
		if (memberId != null && account != null) {
			session.invalidate();
			response.put("status", "success");
			response.put("message", "登出成功");
		} else {
			response.put("message", "登出失敗");
		}
		return response;
	}

	@GetMapping("/avatar")
	public ResponseEntity<byte[]> getAvatar(HttpSession session) {
		String account = (String) session.getAttribute("account");
		byte[] avatar = memServ.findAvatarByAccount(account);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(avatar);
	}

	// 取得會員資訊
	@ResponseBody
	@PostMapping("/memberInfo")
	public Map<String, String> getMemberInfo(HttpSession session) {
		Integer memberId = (Integer) session.getAttribute("memberId");
		String account = (String) session.getAttribute("account");
		Map<String, String> response = new HashMap<>();
		if (memberId != null && account != null) {
			return memServ.findInfoByIdWithMap(memberId);
		} else {
			response.put("message", "沒有登入");
		}
		logger.info("回傳的資料: " + response);
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/memberUpdate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> updateMember(@RequestPart("json") MemberVO memberInfo,
			@RequestPart(value = "file", required = false) MultipartFile file, HttpSession session) {

		Map<String, String> response = new HashMap<>();

		// 驗證登入狀態
		Integer memberId = (Integer) session.getAttribute("memberId");
		String account = (String) session.getAttribute("account");
		if (memberId == null || account == null) {
			response.put("message", "沒有登入");
			return response;
		}

		MemberVO member = memServ.findByMemberId(memberId);
		if (member == null) {
			response.put("message", "會員不存在");
			return response;
		}

		// 更新會員基本資訊
		member.setAccount(memberInfo.getAccount());
		member.setLastName(memberInfo.getLastName());
		member.setFirstName(memberInfo.getFirstName());
		member.setGender(memberInfo.getGender());
		member.setBirthday(memberInfo.getBirthday());
		member.setPhoneNumber(memberInfo.getPhoneNumber());
		if (memberInfo.getPassword() != null && !memberInfo.getPassword().isEmpty()) {
			member.setPassword(memberInfo.getPassword());
		}

		// 處理頭像
		try {
			if (file != null && !file.isEmpty()) {
				if (file.getContentType().startsWith("image/")) {
					byte[] newAvatar = file.getBytes();
					if (!Arrays.equals(member.getAvatar(), newAvatar)) {
						member.setAvatar(newAvatar);
					}
				} else {
					response.put("message", "文件格式不正確，必須為圖片");
					return response;
				}
			} else {
				Resource defaultfile = new ClassPathResource("static/imgs/user/defaultAvatar.png");
				byte[] defaultAvatar = FileCopyUtils.copyToByteArray(defaultfile.getInputStream());
				if (Arrays.equals(member.getAvatar(), defaultAvatar) || member.getAvatar() == null) {
					member.setAvatar(defaultAvatar);
				}
			}
		} catch (IOException e) {
			response.put("message", "文件上傳失敗：" + e.getMessage());
			return response;
		}

		System.out.println(member);

		Set<ConstraintViolation<MemberVO>> violations = validator.validate(member);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<MemberVO> violation : violations) {
				response.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			System.out.println(response);
			return response;
		}

		// 更新會員資料
		try {
			memServ.updateMember(member);
			response.put("success", "success");
			response.put("message", "更新成功");
			return response;
		} catch (Exception e) {
			response.put("message", "更新失敗：" + e.getMessage());
			return response;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/memberRegister", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> registerMember(@RequestPart("json") MemberVO memberInfo,
			@RequestPart(value = "file", required = false) MultipartFile file, HttpSession session) {

		Map<String, String> response = new HashMap<>();
		
		
		if (memServ.existsByAccount(memberInfo.getAccount())) {
			response.put("message", "email已經被註冊過");
			return response;
		}
		
		//判定年紀
        LocalDate currentDate = LocalDate.now();
        Date beAdult = Date.valueOf(currentDate.minusYears(18));
        if(memberInfo.getBirthday().compareTo(beAdult) > 0 ) {
			response.put("message", "年滿18歲才可以註冊！");
			return response;
        }

		// 處理頭像
		try {
			if (file != null && !file.isEmpty()) {
				if (file.getContentType().startsWith("image/")) {
					byte[] newAvatar = file.getBytes();
					memberInfo.setAvatar(newAvatar);
				} else {
					response.put("message", "文件格式不正確，必須為圖片");
					return response;
				}
			} else {
				Resource defaultfile = new ClassPathResource("static/imgs/user/defaultAvatar.png");
				byte[] defaultAvatar = FileCopyUtils.copyToByteArray(defaultfile.getInputStream());
				memberInfo.setAvatar(defaultAvatar);
			}
		} catch (IOException e) {
			response.put("message", "文件上傳失敗：" + e.getMessage());
			return response;
		}

		System.out.println(memberInfo);

		Set<ConstraintViolation<MemberVO>> violations = validator.validate(memberInfo);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<MemberVO> violation : violations) {
				response.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			System.out.println(response);
			return response;
		}
		
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		memberInfo.setStatus((byte) 1);
		memberInfo.setCreateTime(timestamp);
		
		// 更新會員資料
		try {
			memServ.addMember(memberInfo);
			response.put("success", "success");
			response.put("message", "註冊成功");
			return response;
		} catch (Exception e) {
			response.put("message", "註冊失敗：" + e.getMessage());
			return response;
		}
	}

}