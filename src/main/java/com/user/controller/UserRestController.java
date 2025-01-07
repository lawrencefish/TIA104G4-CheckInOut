package com.user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.member.model.MemberService;
import com.member.model.MemberVO;

@RestController
@RequestMapping("/user/api")
public class UserRestController {
	@Autowired
	MemberService memServ;

	// 登入
	@PostMapping("/login")
	public Map<String, String> memberLogin(@RequestBody Map<String, String> loginRequest, HttpSession session) {
		String account = loginRequest.get("account");
		String password = loginRequest.get("password");

		Map<String, String> response = new HashMap<>();
		if (memServ.existsByAccount(account)) {
			MemberVO member = memServ.login(account, password);
			if (member != null) {
				System.out.println("登入成功" + member.getAccount());
				// 登入成功 管理員ID存入session
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

	@PostMapping("/loginCheck")
	public Map<String, String> loginCheck(HttpSession session) {
		String memberId = String.valueOf(session.getAttribute("memberId"));
		String account = (String) session.getAttribute("account");
		Map<String, String> response = new HashMap<>();
		if (memberId != null && account != null) {
			response.put("memberId", memberId != null ? memberId : null);
			response.put("account", account != null ? account : null);
		} else {
			response.put("message", "not login");
		}
		return response;
	}

	@PostMapping("/logout")
	public Map<String, String> logout(HttpSession session) {
		String memberId = String.valueOf(session.getAttribute("memberId"));
		String account = (String) session.getAttribute("account");
		Map<String, String> response = new HashMap<>();
		if (memberId != null && account != null) {
			session.setAttribute("memberId", null);
			session.setAttribute("account", null);
			response.put("status", "success");
			response.put("message", "登出成功");
		} else {
			response.put("message", "登出失敗");
		}
		return response;
	}

}
