package com.news.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "news")
public class NewsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id", updatable = false)
	private Integer newsId;
	
	@NotBlank(message = "標題不可為空")    
	@Column(name = "news_title", length = 50, nullable = false, unique = true)
	private String newsTitle;
	
	@NotBlank(message = "內容不可為空")    
	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "post_time")
	private Timestamp postTime;
	
	@Column(name = "create_time", nullable = false, insertable = false, updatable = false)
	private Timestamp createTime;
	
	@Lob
    @Column(name = "news_pic")
    private byte[] newsPic;

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
	
	
	public byte[] getNewsPic() {
        return newsPic;
    }

    public void setNewsPic(byte[] idFront) {
        this.newsPic = idFront;
    }
	
	
}



//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//@Column(name = "news_id")
//private Integer newsId;
//
//@Column(name = "news_title", nullable = false, length = 200)
//private String newsTitle;
//
//@Column(name = "description", nullable = false, columnDefinition = "TEXT")
//private String description;
//
//@Column(name = "post_time", nullable = false)
//private LocalDateTime postTime;
//
//@Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//private LocalDateTime createTime;
//
//// Getters and Setters
//public Integer getNewsId() {
//    return newsId;
//}
//
//public void setNewsId(Integer newsId) {
//    this.newsId = newsId;
//}
//
//public String getNewsTitle() {
//    return newsTitle;
//}
//
//public void setNewsTitle(String newsTitle) {
//    this.newsTitle = newsTitle;
//}
//
//public String getDescription() {
//    return description;
//}
//
//public void setDescription(String description) {
//    this.description = description;
//}
//
//public LocalDateTime getPostTime() {
//    return postTime;
//}
//
//public void setPostTime(LocalDateTime postTime) {
//    this.postTime = postTime;
//}
//
//public LocalDateTime getCreateTime() {
//    return createTime;
//}
//
//public void setCreateTime(LocalDateTime createTime) {
//    this.createTime = createTime;
//}
//}