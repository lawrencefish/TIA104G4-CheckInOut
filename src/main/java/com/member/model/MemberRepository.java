package com.member.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

	@Transactional
	@Modifying
	@Query(value="delete from member where member_id = ?1",nativeQuery = true)
	void deleteByMemberID(Integer memberId);
	
	Boolean existsByAccount(String account);
	
	MemberVO findByAccount(String account);
	
	boolean existsById(Integer memberId);

}
