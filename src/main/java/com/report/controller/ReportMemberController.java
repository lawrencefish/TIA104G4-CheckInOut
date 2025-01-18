package com.report.controller;

import com.hotel.model.HotelVO;
import com.report.pojo.AddReportRequest;
import com.report.service.ReportMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/report/member")
public class ReportMemberController {

    @Autowired
    private ReportMemberService reportMemberService;

    /**
     * 檢舉客戶
     */
    @PostMapping(value = "/{memberId}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String,Object>> reportMember(HttpServletRequest request,
                                                           @PathVariable Integer memberId,
                                                           AddReportRequest formDataRequest) throws IOException {
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");

        return reportMemberService.reportMember(memberId, hotel.getHotelId(), formDataRequest);
    }
}
