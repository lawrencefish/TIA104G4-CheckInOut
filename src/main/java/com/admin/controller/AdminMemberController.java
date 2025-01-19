package com.admin.controller;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.admin.model.AdminMemberService;
import com.member.model.MemberVO;

@RestController
@RequestMapping("/adminMember")
public class AdminMemberController {

    @Autowired
    private AdminMemberService adminMemberService;

    // 獲取所有會員資料
    @GetMapping("/findAllMembers")
    public ResponseEntity<List<MemberVO>> findAllMembers() {
        List<MemberVO> members = adminMemberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    // 複合搜尋功能
    @PostMapping("/search")
    public ResponseEntity<List<MemberVO>> searchMembers(@RequestBody Map<String, String> searchCriteria) {
        String keyword = searchCriteria.get("keyword");
        String statusStr = searchCriteria.get("status");
        Byte status = null;
        
        // 只有當狀態不為空時才進行轉換
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Byte.parseByte(statusStr);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        List<MemberVO> results = adminMemberService.searchMembers(keyword, status);
        return ResponseEntity.ok(results);
    }

    // 切換會員狀態（啟用/停權）
    @PutMapping("/toggleStatus/{memberId}")
    public ResponseEntity<MemberVO> toggleMemberStatus(@PathVariable Integer memberId) {
        MemberVO updatedMember = adminMemberService.toggleMemberStatus(memberId);
        if (updatedMember != null) {
            return ResponseEntity.ok(updatedMember);
        }
        return ResponseEntity.notFound().build();
    }
    
    // 會員狀態更新
    @PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, Object> request) {
        // 處理會員狀態更新邏輯
        return ResponseEntity.ok().build();
    }

    // 根據ID獲取會員詳細資訊
    @GetMapping("/member/{memberId}")
    public ResponseEntity<MemberVO> getMemberById(@PathVariable Integer memberId) {
        MemberVO member = adminMemberService.findById(memberId);
        if (member != null) {
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.notFound().build();
    }

    // 批量更新會員狀態
//    @PutMapping("/batchUpdateStatus")
//    public ResponseEntity<List<Member>> batchUpdateStatus(
//            @RequestBody Map<String, Object> request) {
//        @SuppressWarnings("unchecked")
//        List<Integer> memberIds = (List<Integer>) request.get("memberIds");
//        Integer status = (Integer) request.get("status");
//        
//        List<Member> updatedMembers = memberService.batchUpdateStatus(memberIds, status);
//        return ResponseEntity.ok(updatedMembers);
//    }
}
