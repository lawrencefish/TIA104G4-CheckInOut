package com.report.pojo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddReportRequest {

    private Integer orderId;

    private String reportContent;

    private MultipartFile image;
}
