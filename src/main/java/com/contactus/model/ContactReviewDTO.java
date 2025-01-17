package com.contactus.model;

public class ContactReviewDTO {
	
	
	    private Integer contact_us_id;
	    private String status;
	    private String reply;
	    private Integer admin_id;
	    
		public Integer getContact_us_id() {
			return contact_us_id;
		}
		public void setContact_us_id(Integer contact_us_id) {
			this.contact_us_id = contact_us_id;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getReply() {
			return reply;
		}
		public void setReply(String reply) {
			this.reply = reply;
		}
		public Integer getAdmin_id() {
			return admin_id;
		}
		public void setAdmin_id(Integer admin_id) {
			this.admin_id = admin_id;
		}
	    
	    

}
