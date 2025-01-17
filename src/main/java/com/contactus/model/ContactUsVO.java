package com.contactus.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.admin.model.Admin;
;

@Entity
@Table(name = "contact_us")
public class ContactUsVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactUsId;

    @NotBlank
    private String name;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();
    
    @Column(nullable = false)
    private String status = "0";
    
    private String reply;
    
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
    
    @Column(nullable = false)
    private LocalDateTime processed_at = LocalDateTime.now();

	public Integer getContactUsId() {
		return contactUsId;
	}

	public void setContactUsId(Integer contactUsId) {
		this.contactUsId = contactUsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
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

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public LocalDateTime getProcessed_at() {
		return processed_at;
	}

	public void setProcessed_at(LocalDateTime processed_at) {
		this.processed_at = processed_at;
	}

}
