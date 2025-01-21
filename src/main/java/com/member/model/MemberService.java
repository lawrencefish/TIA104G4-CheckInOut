package com.member.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.model.CouponService;

@Service("MemberService")
public class MemberService {

	@Autowired
	private MemberRepository repository;
	
	@Autowired
	private CouponService couponService;
	;


//	@Transactional
//	public void addMember(MemberVO memberVO) {
//	    try {
//	        repository.save(memberVO);
//	    } catch (Exception e) {
//	    	System.out.println(e);
//	        throw new RuntimeException("註冊會員資料失敗", e);
//	    }
//	}
	
	@Transactional
	public MemberVO addMember(MemberVO memberVO) {		
	    try {
	        MemberVO savedMember = repository.save(memberVO);
	        return savedMember;
	        
	    } catch (Exception e) {
	        System.out.println("註冊會員資料失敗" + e.getMessage());
	        throw new RuntimeException("註冊會員資料失敗", e);
	
	    }
	}
	
	

	@Transactional
	public void updateMember(MemberVO memberVO){
	    try {
	        repository.save(memberVO);
	    } catch (Exception e) {
	    	System.out.println(e);
	        throw new RuntimeException("更新會員資料失敗", e);
	    }
	}

	public void deleteMember(Integer memberId) {
		if (repository.existsById(memberId)) {
			repository.deleteByMemberID(memberId);
		}
	}

	public MemberVO login(String account, String password) {
		if (repository.existsByAccount(account)) {
			if (repository.findByAccount(account).getPassword().equals(password)) {
				return repository.findByAccount(account);
			}
		}
		return null;
	}

	public Boolean existsByAccount(String account) {
		return repository.existsByAccount(account);
	}

	public byte[] findAvatarByAccount(String account) {
		return repository.findByAccount(account).getAvatar();
	}
	
	public MemberVO findByAccount(String account) {
		return repository.findByAccount(account);
	}


	public MemberVO findByMemberId(Integer memberId) {
		Optional<MemberVO> optional = repository.findById(memberId);
		return optional.orElse(null);
	}

	public Map<String, String> findInfoByIdWithMap(Integer memberId) {
		if (repository.existsById(memberId)) {
			MemberVO mem = repository.findById(memberId).orElse(null);
			Map<String, String> info = new HashMap<>();
			info.put("account", mem.getAccount());
			info.put("lastName", mem.getLastName());
			info.put("firstName", mem.getFirstName());
			info.put("gender", String.valueOf(mem.getGender()));
			info.put("birthday", String.valueOf(mem.getBirthday()));
			info.put("phone", mem.getPhoneNumber());
			return info;
		}
		return null;
	}

	public List<MemberVO> getAll() {
		return repository.findAll();
	}

	public MemberVO getOneMem(Integer memberId) {
		return null;
	}

	public MemberVO findById(Integer memberId) {
		return null;
	}

}
