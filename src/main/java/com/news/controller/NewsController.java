package com.news.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.news.model.NewsService;
import com.news.model.NewsVO;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;
    
    @GetMapping
    public List<NewsVO> getAllNews() {
        List<NewsVO> newsList = newsService.getAllNews();
        // 轉換每個新聞項目中的圖片為 Base64
        newsList.forEach(news -> {
            if (news.getNewsImg() != null) {
                String base64Image = Base64.getEncoder().encodeToString(news.getNewsImg());
                // 加入 data URL 前綴
                String imageData = "data:image/jpeg;base64," + base64Image;
                news.setNewsImg(imageData.getBytes());
            }
        });
        return newsList;
    }

//    @GetMapping
//    public ResponseEntity<List<NewsVO>> getAllNews() {
//        List<NewsVO> newsList = newsService.getAllNews();
//        return ResponseEntity.ok(newsList);
//    }
    
//    @GetMapping
//    public List<NewsVO> getAllNews() {
//        List<NewsVO> newsList = newsService.getAllNews();
//        // 將所有圖片轉換為 Base64
//        for (NewsVO news : newsList) {
//            if (news.getNewsImg() != null) {
//                String base64Image = Base64.getEncoder().encodeToString(news.getNewsImg());
//                news.setNewsImg(base64Image.getBytes());
//            }
//        }
//        return newsList;
//    }
    
    //單一消息
//    @GetMapping("/{id}")
//    public ResponseEntity<NewsVO> getNewsById(@PathVariable Integer id) {
//        try {
//            NewsVO news = newsService.getNewsById(id);
//            if (news != null) {
//                return ResponseEntity.ok(news);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NewsVO> getNewsById(@PathVariable Integer id) {
        try {
            NewsVO news = newsService.getNewsById(id);
            if (news != null) {
                // 如果有圖片，轉換為 Base64
                if (news.getNewsImg() != null) {
                    String base64Image = Base64.getEncoder().encodeToString(news.getNewsImg());
                    news.setNewsImg(base64Image.getBytes());
                }
                return ResponseEntity.ok(news);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createNews(
            @RequestParam("newsTitle") String newsTitle,
            @RequestParam("description") String description,
            @RequestParam(value = "newsImg", required = false) MultipartFile newsImg) {
        try {
            NewsVO newsVO = new NewsVO();
            newsVO.setNewsTitle(newsTitle);
            newsVO.setDescription(description);
            newsVO.setPostTime(LocalDateTime.now());
            
            if (newsImg != null && !newsImg.isEmpty()) {
                newsVO.setNewsImg(newsImg.getBytes());
            }
            
            NewsVO savedNews = newsService.createNews(newsVO);
            return ResponseEntity.ok(savedNews);
        } catch (IOException e) {
        	return ResponseEntity.badRequest().body("圖片處理失敗");
        } catch (Exception e) {
            
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNews(
            @PathVariable Integer id,
            @RequestParam("newsTitle") String newsTitle,
            @RequestParam("description") String description,
            @RequestParam(value = "newsImg", required = false) MultipartFile newsImg) {
        try {
            NewsVO newsVO = newsService.getNewsById(id);
            if (newsVO == null) {
                return ResponseEntity.notFound().build();
            }
            
            newsVO.setNewsTitle(newsTitle);
            newsVO.setDescription(description);
            
            if (newsImg != null && !newsImg.isEmpty()) {
                newsVO.setNewsImg(newsImg.getBytes());
            }
            
            NewsVO updatedNews = newsService.updateNews(newsVO);
            return ResponseEntity.ok(updatedNews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("圖片處理失敗");
        }
    }
    
    // 刪除新聞
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Integer id) {
        try {
            boolean deleted = newsService.deleteNews(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 取得消息圖片
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getNewsImage(@PathVariable Integer id) {
        try {
            NewsVO newsVO = newsService.getNewsById(id);
            if (newsVO != null && newsVO.getNewsImg() != null) {
                return ResponseEntity.ok()
                		.header("Content-Type", newsVO.getImgType()) // 如果有儲存圖片類型的話
                        .body(newsVO.getNewsImg());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
