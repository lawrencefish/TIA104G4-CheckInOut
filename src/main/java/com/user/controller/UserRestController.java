package com.user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotelImg.model.HotelImgVO;
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
		String url =  loginRequest.get("url");

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
	
	//檢查登入狀態
	@PostMapping("/loginCheck")
	public Map<String, String> loginCheck(HttpSession session) {
		String memberId = String.valueOf(session.getAttribute("memberId"));
		String account =  (String) session.getAttribute("account");
		String url =  (String) session.getAttribute("url");
		Map<String, String> response = new HashMap<>();
		if (memberId != null && account != null) {
			response.put("memberId", memberId != null ? memberId : null);
			response.put("account", account != null ? account : null);
		} else {
			if (url != null) {
				response.put("message", "redirect");
				response.put("url", url);
				session.setAttribute("url",null);
			}else {
			response.put("message", "not login");
			}
		}
		return response;
	}
	
	//登出
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
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(avatar);
    }

	//取得會員資訊
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
		return response;
	}

}
