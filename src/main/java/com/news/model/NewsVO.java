package com.news.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "news")
public class NewsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id", updatable = false)
	private Integer newsId;
	
	@Column(name = "news_title")
	private String newsTitle;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "post_time")
	private Timestamp postTime;
	
	@Column(name = "create_time")
	private Timestamp createTime;

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



	public Timestamp getPostTime() {
		return postTime;
	}



	public void setPostTime(Timestamp postTime) {
		this.postTime = postTime;
	}



	public Timestamp getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	@Override
	public String toString() {
		return "News [news_id=" + newsId + ", news_title=" + newsTitle + ", "
				+ "description=" + description + ", post_time=" + postTime + ", create_time=" + createTime
				;
	}
	
	
}