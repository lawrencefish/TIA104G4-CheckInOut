package com.news.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.news.model.NewsService;
import com.news.model.NewsVO;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public List<NewsVO> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public Optional<NewsVO> getNewsById(@PathVariable Integer id) {
        return newsService.getNewsById(id);
    }

    @PostMapping
    public NewsVO createNews(@RequestBody NewsVO news) {
        return newsService.saveNews(news);
    }

    @PutMapping("/{id}")
    public NewsVO updatedNews(@PathVariable Integer id, @RequestBody NewsVO news) {
        return newsService.updatedNews(id, news);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Integer id) {
        newsService.deleteNews(id);
    }
}


