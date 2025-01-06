package com.faq.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<FaqVO,Integer> {

}
