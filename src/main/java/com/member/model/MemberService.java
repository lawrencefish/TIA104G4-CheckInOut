package com.member.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("MemberService")
public class MemberService {

	@Autowired
	private MemberRepository repository;

	@Transactional
	public void addMember(MemberVO memberVO) {
		repository.save(memberVO);
	}

	@Transactional
	public void updateMember(MemberVO memberVO) {
		repository.save(memberVO);
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

	public MemberVO findByMemberId(Integer memberId) {
		Optional<MemberVO> optional = repository.findById(memberId);
		return optional.orElse(null);
	}

	public List<MemberVO> getAll() {
		return repository.findAll();
	}

}
