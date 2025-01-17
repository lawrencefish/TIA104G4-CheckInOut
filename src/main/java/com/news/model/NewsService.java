package com.news.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewsService {
	
	@Autowired
	private NewsRepository newsRepository;
	
	// 取得所有消息列表
	public List<NewsVO> getAllNews(){
		return newsRepository.findAllByOrderByPostTimeDesc();
	}
	
	  // 根據ID取得單一消息
	public NewsVO getNewsById(Integer id){
		return newsRepository.findById(id)
		.orElseThrow(() -> new IllegalArgumentException("找不到指定的新聞"));
	}
	
	// 新增消息
    public NewsVO createNews(NewsVO newsItem) {
    	if (StringUtils.isEmpty(newsItem.getNewsTitle())) {
            throw new IllegalArgumentException("新聞標題不能為空");
        }
        
        // 檢查描述是否為空
        if (StringUtils.isEmpty(newsItem.getDescription())) {
            throw new IllegalArgumentException("新聞內容不能為空");
        }
        
        // 檢查標題是否重複
        if (newsRepository.existsByNewsTitle(newsItem.getNewsTitle())) {
            throw new IllegalArgumentException("新聞標題已存在");
        }
        
        // 設置發布時間
        if (newsItem.getPostTime() == null) {
            newsItem.setPostTime(LocalDateTime.now());
        }
        
        return newsRepository.save(newsItem);
    }
	
    public NewsVO updateNews(NewsVO newsItem) {
        // 確認消息是否存在
    	NewsVO existingNews = newsRepository.findById(newsItem.getNewsId())
                .orElseThrow(() -> new IllegalArgumentException("找不到指定的新聞"));
            
            // 檢查標題是否為空
            if (StringUtils.isEmpty(newsItem.getNewsTitle())) {
                throw new IllegalArgumentException("新聞標題不能為空");
            }
            
            // 檢查描述是否為空
            if (StringUtils.isEmpty(newsItem.getDescription())) {
                throw new IllegalArgumentException("新聞內容不能為空");
            }
            
            // 如果標題變更，檢查是否重複
            if (!existingNews.getNewsTitle().equals(newsItem.getNewsTitle()) &&
                newsRepository.existsByNewsTitle(newsItem.getNewsTitle())) {
                throw new IllegalArgumentException("新聞標題已存在");
            }
            
            // 保持原有的發布時間
            newsItem.setPostTime(existingNews.getPostTime());
            
            return newsRepository.save(newsItem);
        }
   
	//刪除
	public boolean deleteNews(Integer id) {
		if (!newsRepository.existsById(id)) {
            throw new IllegalArgumentException("找不到指定的新聞");
        }
        newsRepository.deleteById(id);
        return true;
    }
	
	// 搜尋消息
    public List<NewsVO> searchNews(String keyword) {
        return newsRepository.findByNewsTitleContaining(keyword);
    }
	
	
	

}
