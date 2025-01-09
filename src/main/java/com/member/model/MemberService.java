package com.member.model;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("MemberService")
public class MemberService {

	@Autowired
	private MemberRepository repository;

	@Transactional
	public void addMember(MemberVO memberVO) {
		repository.save(memberVO);
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

}
