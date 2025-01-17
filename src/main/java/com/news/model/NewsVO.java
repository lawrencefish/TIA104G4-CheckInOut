package com.news.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "news")
public class NewsVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer newsId;

    @Column(name = "news_title", nullable = false, length = 200)
    private String newsTitle;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(name = "news_img")
    private byte[] newsImg;

    @Column(name = "post_time", nullable = false)
    private LocalDateTime postTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getNewsImg() {
		return newsImg;
	}

	public void setNewsImg(byte[] newsImg) {
		this.newsImg = newsImg;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getImgType() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
    