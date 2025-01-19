package com.admin.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.member.model.MemberRepository;
import com.member.model.MemberVO;

@Service
@Transactional
public class AdminMemberService {
	
	@Autowired
    private MemberRepository memberRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    // 獲取所有會員
    public List<MemberVO> findAllMembers() {
        return memberRepository.findAll();
    }

    // 根據ID查詢會員
    public MemberVO findById(Integer memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    // 複合搜尋功能
    public List<MemberVO> searchMembers(String keyword, Byte status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberVO> query = cb.createQuery(MemberVO.class);
        Root<MemberVO> root = query.from(MemberVO.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // 如果有關鍵字，搜尋名字、電話或Email
        if (keyword != null && !keyword.trim().isEmpty()) {
            String likePattern = "%" + keyword.trim() + "%";
            Predicate namePredicate = cb.like(root.get("name"), likePattern);
            Predicate phonePredicate = cb.like(root.get("phoneNumber"), likePattern);
            Predicate emailPredicate = cb.like(root.get("email"), likePattern);
            predicates.add(cb.or(namePredicate, phonePredicate, emailPredicate));
        }
        
        // 如果有指定狀態
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        
        return entityManager.createQuery(query).getResultList();
    }

    // 切換會員狀態
    public MemberVO toggleMemberStatus(Integer memberId) {
        MemberVO member = memberRepository.findById(memberId).orElse(null);
        if (member != null) {
            // 切換狀態 (0: 啟用, 1: 停權)
            member.setStatus(member.getStatus() == 0 ? (byte)1 : (byte)0);
            return memberRepository.save(member);
        }
        return null;
    }

    // 批量更新會員狀態
    public List<MemberVO> batchUpdateStatus(List<Integer> memberIds, Byte status) {
        List<MemberVO> updatedMembers = new ArrayList<>();
        for (Integer memberId : memberIds) {
            MemberVO member = memberRepository.findById(memberId).orElse(null);
            if (member != null) {
                member.setStatus(status);
                updatedMembers.add(memberRepository.save(member));
            }
        }
        return updatedMembers;
    }
}