package com.admin.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="admin")
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ():有四種不同生成策略"strategy" 效能好 常用 每次插入新紀錄才會生成ID
	@Column(name = "admin_id")
	private Integer adminId;
	
	@Column(name = "create_time", nullable = false, updatable = false) // nullable=false不能為空值 updatable=false不能被更新
	@CreationTimestamp
	private Timestamp createTime;
	
	@Column(name = "admin_account", length = 20, nullable = false, unique = true) // unique = true唯一&不能重複
	private String adminAccount;
	
	@Column(name = "admin_password", length = 20, nullable = false)
	private String adminPassword;
	
	@Column(name = "phone_number", length = 10, nullable = false)
	private String phoneNumber;
	
	@Column(name = "email", length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(name = "status", nullable = false)
	private Byte status; 
	
	@Column(name = "permissions", nullable = false)
	private Byte permissions; 
	
	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(Integer adminId, Timestamp createTime, String adminAccount, String adminPassword, String phoneNumber,
			String email, Byte status, Byte permissions) {
		super();
		this.adminId = adminId;
		this.createTime = createTime;
		this.adminAccount = adminAccount;
		this.adminPassword = adminPassword;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = status;
		this.permissions = permissions;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getAdminAccount() {
		return adminAccount;
	}

	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getPermissions() {
		return permissions;
	}

	public void setPermissions(Byte permissions) {
		this.permissions = permissions;
	}
	
	

}
