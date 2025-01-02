package com.news.controller;


import com.news.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
//一個註解
@Controller
@RequestMapping("news")
public class NewsController {

    @Autowired
    private NewsService newsSvc;

    @GetMapping("addNews")
	public String addNews(ModelMap model) {
		NewsVO newsVO = new NewsVO();
		model.addAttribute("newsVO", newsVO);
		return "business/addNews";
	}

    @GetMapping("listAllNews")
    public List<NewsVO> getAllNews() {
        return newsSvc.getAllNews();
    }

    @PostMapping("getOneForUpdate")
	public String getOneForUpdate(@RequestParam("newsId") Integer newsId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		NewsVO newsVO = newsSvc.getNewsById(newsId);

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("newsVO", newsVO);
		return "business/update_news_input"; // 查詢完成後轉交update_news_input.html
	}

    @PostMapping("update")
	public String update(@Valid NewsVO newsVO, BindingResult result, ModelMap model)
			throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
	 		if (result.hasErrors()) {
			return "business/update_news_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsSvc.updateNews(newsVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		newsVO = newsSvc. getNewsById(Integer.valueOf(newsVO.getNewsId()));
		model.addAttribute("newsVO", newsVO);
		return "business/listOneNews";
	}

    @PostMapping("delete")
	public String delete(@RequestParam("newsId") Integer newsId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
    	newsSvc.deleteNews(Integer.valueOf(newsId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<NewsVO> list = newsSvc.getAllNews();
		model.addAttribute("newsListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "business/listAllNews";
	}

}
