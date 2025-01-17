package com.news.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsVO, Integer> {
	
	 // 依照發布時間降序排序取得所有新聞
    List<NewsVO> findAllByOrderByPostTimeDesc();
    
    // 依照標題模糊搜尋
    List<NewsVO> findByNewsTitleContaining(String keyword);
    
    // 檢查標題是否已存在
    boolean existsByNewsTitle(String title);
   
}