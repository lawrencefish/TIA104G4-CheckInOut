package com.report.service;

import com.order.model.OrderRepository;
import com.order.model.OrderVO;
import com.report.model.Reports;
import com.report.pojo.AddReportRequest;
import com.report.repository.ReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportMemberService {

    @Autowired
    private ReportsRepository reportsRepository;
    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<Map<String, Object>> reportMember(Integer memberId, Integer hotelId, AddReportRequest request) throws IOException {
        Integer orderId = request.getOrderId();
        if (orderId != null) {
            long count = orderRepository.countByOrderIdAndMemberId(orderId, memberId);
            if(count < 1) {
                Map<String, Object> result = new HashMap<>();
                result.put("message", "訂單編號不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        }

        Reports reports = new Reports();
        reports.setHotelId(hotelId);
        reports.setMemberId(memberId);
        reports.setOrderId(request.getOrderId());
        reports.setReportContent(request.getReportContent());
        if(request.getImage() != null) {
            reports.setPicture(request.getImage().getBytes());
        }

        reportsRepository.save(reports);

        return ResponseEntity.ok(new HashMap<>());
    }
}
