package com.news.model;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsVO, Integer> {

	Optional<NewsVO> findById(Integer newsId);

	Optional<NewsVO> findByNewsTitle(String newsTitle);

}
