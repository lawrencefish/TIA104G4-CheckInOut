package com.frequentReply.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
	@Table(name = "frequent_reply_list")
	public class FrequentReplyVO implements java.io.Serializable {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "frequent_reply_id")
	    private Long replyId;

	    @NotBlank(message = "標題不得為空")
	    @Size(max = 50, message = "標題長度不可超過50字")
	    @Column(name = "frequent_reply_title", nullable = false)
	    private String title;

	    @NotBlank(message = "內容不得為空")
	    @Size(max = 500, message = "內容長度不可超過500字")
	    @Column(name = "frequent_reply_text", nullable = false)
	    private String content;

	    public FrequentReplyVO() {
	    }

	    public Long getReplyId() {
	        return replyId;
	    }

	    public void setReplyId(Long replyId) {
	        this.replyId = replyId;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public String getContent() {
	        return content;
	    }

	    public void setContent(String content) {
	        this.content = content;
	    }

	    @Override
	    public String toString() {
	        return "FrequentReplyVO [replyId=" + replyId + ", title=" + title + ", content=" + content + "]";
	    }
}

