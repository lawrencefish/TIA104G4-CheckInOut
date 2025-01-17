package com.business.controller;

import java.util.List;

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
import com.hotel.model.HotelVO;
import com.member.model.*;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
    private OrderService orderService;

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
    public String showClientDetail(@PathVariable Integer memberId, Model model) {
        MemberVO client = orderService.getMemberId(memberId);
        model.addAttribute("client", client);
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
