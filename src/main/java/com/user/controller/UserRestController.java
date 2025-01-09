package com.user.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
	@ResponseBody // 明確標註這是回傳 JSON
	public Map<String, String> updateMember(@RequestPart("json") Map<String, String> memberInfo,
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
		member.setAccount(memberInfo.get("account"));
		member.setLastName(memberInfo.get("lastName"));
		member.setFirstName(memberInfo.get("firstName"));
		member.setGender(memberInfo.get("gender"));
		member.setBirthday(Date.valueOf(memberInfo.get("birthday")));
		member.setPhoneNumber(memberInfo.get("phoneNumber"));
		if (memberInfo.get("password") != null && !memberInfo.get("password").isEmpty()) {
			member.setPassword(memberInfo.get("password"));
		}
		System.out.println(memberInfo);
		// 處理文件上傳
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
			}
		} catch (IOException e) {
			response.put("message", "文件上傳失敗：" + e.getMessage());
			return response;
		}

		// 更新會員資料
		try {
			memServ.updateMember(member);
			response.put("message", "更新成功");
		    HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			logger.info("回傳的資料: " + response);
			return response;
		} catch (Exception e) {
			response.put("message", "更新失敗：" + e.getMessage());
			return response;
		}
	}
}