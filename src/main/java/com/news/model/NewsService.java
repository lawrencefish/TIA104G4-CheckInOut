package com.news.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {
	
	@Autowired
	private NewsRepository newsRepository;
	
	public List<NewsVO> getAllNews(){
		return newsRepository.findAll();
	}
	
	public Optional<NewsVO> getNewsById(Integer id){
		return newsRepository.findById(id);
	}
	
	public NewsVO saveNews(NewsVO news) {
		return newsRepository.save(news);
	}
	
	public NewsVO updatedNews(Integer id, NewsVO updatedNews) {
		return newsRepository.findById(id).map(news -> {
			news.setNewsTitle(updatedNews.getNewsTitle());
			news.setDescription(updatedNews.getDescription());
			news.setNewsImg(updatedNews.getNewsImg());
			news.setPostTime(updatedNews.getPostTime());
			return newsRepository.save(news);
		}).orElseThrow(() -> new RuntimeException("找不到"));
	}
	
	public void deleteNews(Integer id) {
		newsRepository.deleteById(id);
	}
	
	
	

}
