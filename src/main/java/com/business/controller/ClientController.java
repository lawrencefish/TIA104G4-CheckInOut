package com.business.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.order.model.OrderService;
import com.order.model.OrderVO;
import com.comment.service.CommentService;
import com.hotel.model.HotelVO;
import com.member.model.*;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
    private OrderService orderService;
	
	@Autowired
    private CommentService commentService;

    @GetMapping("")
    public String showClient() {
        return "redirect:/client/allClient";
    }

    @GetMapping("/allClient")
    public String showAllClients(Model model, @SessionAttribute("hotel") HotelVO hotel) {
        List<MemberVO> clients = orderService.findClientsByHotel(hotel.getName());
        model.addAttribute("clients", clients);
        return "business/allClient";
    }

    @GetMapping("/search")
    public String searchClients(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String clientMail,
            @RequestParam(required = false) String clientPhone,
            Model model) {

        // 搜尋客戶
        List<MemberVO> clients = orderService.searchClients(clientId, clientName, clientMail, clientPhone);

        // 將搜尋條件和結果添加到模型中
        model.addAttribute("clients", clients);
        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", clientName);
        model.addAttribute("clientMail", clientMail);
        model.addAttribute("clientPhone", clientPhone);

        return "business/allClient"; 
    }
    
    @GetMapping("/clientDetail/{memberId}")
    public String showClientDetail(@PathVariable Integer memberId, Model model, HttpSession session) {
        // 從 orderService 獲取客戶資料
        MemberVO client = orderService.getMemberId(memberId);

        // 處理頭像資料
        String avatar;
        if (client.getAvatar() != null && client.getAvatar().length > 0) {
            // 將 byte[] 轉換為 Base64 字符串
            avatar = "data:image/png;base64," + Base64.getEncoder().encodeToString(client.getAvatar());
        } else {
            // 使用默認圖片
            avatar = "/imgs/user/defaultAvatar.png";
        }

        // 從 session 獲取當前飯店的 hotelId
        HotelVO hotel = (HotelVO) session.getAttribute("hotel");
        if (hotel == null) {
            throw new IllegalStateException("Hotel not found in session.");
        }
        Integer hotelId = hotel.getHotelId();

        // 使用 hotelId 和 memberId 查詢評論內容
        String commentContent = commentService.getCommentContentByHotelAndMember(hotelId, memberId);

        // 添加模型屬性
        model.addAttribute("client", client);
        model.addAttribute("avatar", avatar);
        model.addAttribute("commentContent", commentContent);

        return "business/clientDetail";
    }
    
    @PostMapping("/update")
    public String updateClient(@ModelAttribute MemberVO updatedClient) {
        orderService.updateMember(updatedClient);
        return "redirect:/client/clientDetail/" + updatedClient.getMemberId(); // 重定向到詳細頁
    }
    
    @GetMapping("/commentClient/{memberId}")
    public String commentClient(@PathVariable Integer memberId, Model model) {
    	MemberVO client = orderService.getMemberId(memberId);
        model.addAttribute("client", client);
        return "business/commentClient";
    }
    
    @GetMapping("/reportClient/{memberId}")
    public String reportClient(@PathVariable Integer memberId, Model model) {
    	MemberVO client = orderService.getMemberId(memberId);
        model.addAttribute("client", client);
        return "business/reportClient";
    }
    
}
