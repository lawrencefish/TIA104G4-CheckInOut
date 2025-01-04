package com.member.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("memberService")
public class MemberService {

	@Autowired
	MemberRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	public void addMember(MemberVO memberVO) {
		repository.save(memberVO);
	}

	@Transactional
	public void updateMember(MemberVO memberVO) {
		repository.save(memberVO);
	}

	@Transactional
	public void deleteMember(Integer memberId) {
		if (repository.existsById(memberId)) {
			repository.deleteByMemberID(memberId);
		}
	}

	public MemberVO queryMember(Integer memberId) {
		Optional<MemberVO> optional = repository.findById(memberId);
		return optional.orElse(null);
	}

	public List<MemberVO> getAll() {
		return repository.findAll();
	}

}
