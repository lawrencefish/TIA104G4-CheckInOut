package com.order.dto;

public class AvgRatingsAndCommentDTO {

	private Double avgRating;
	private Long commentCount;

    public AvgRatingsAndCommentDTO(Long commentCount, Double avgRating) {
        this.commentCount = commentCount;
        this.avgRating = avgRating;
    }

	public Double getAvgRating() {
		return avgRating;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setAvgRating(Double avgRating) {
		this.avgRating = avgRating;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}


}
