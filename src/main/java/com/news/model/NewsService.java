package com.news.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<NewsVO> getAllNews() {
        return newsRepository.findAll();
    }

    public NewsVO getNewsById(Integer newsId) {
		Optional<NewsVO> optional = newsRepository.findById(newsId);
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

    public Optional<NewsVO> getNewsByTitle(String newsTitle) {
        return newsRepository.findByNewsTitle(newsTitle);
    }
    
    public void addNews(NewsVO newsVO) {
    	newsRepository.save(newsVO);
    }

    public void updateNews(NewsVO newsVO) {
        newsRepository.save(newsVO);
    }

    public void deleteNews(Integer newsId) {
		if (newsRepository.existsById(newsId))
			newsRepository.deleteById(newsId);
	}

}
