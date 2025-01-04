package com.news.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsVO {
    private Integer newsId;
    private String newsTitle;
    private String description;
    private byte[] newsImg;
    private LocalDateTime postTime;
    private Timestamp createTime;
}