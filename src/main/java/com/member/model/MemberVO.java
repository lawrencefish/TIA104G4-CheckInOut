package com.member.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.creditcard.model.CreditcardVO;
import com.order.model.OrderVO;
import com.orderDetail.model.OrderDetailVO;

@Entity
@Table(name = "member")
public class MemberVO implements java.io.Serializable {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;

	@Column(name = "account")
	@NotBlank(message = "Email 不可為空")
	@Email(message = "Email 格式不正確")
	private String account;
	@Column(name = "password")
	@NotEmpty(message = "請輸入密碼!")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*+\\-=\\*]{8,12}$",message = "請輸入8到12字的密碼，必須包含英文數字")
	private String password;
	@Column(name = "last_name")
	@NotEmpty(message = "請輸入姓")
	@Pattern(regexp = "^[\\u4e00-\\u9FFFa-zA-Z]{1,20}$",message = "姓名僅限中英文")
	private String lastName;
	@Column(name = "first_name")
	@NotEmpty(message = "請輸入姓")
	@Pattern(regexp = "^[\\u4e00-\\u9FFFa-zA-Z]{1,20}$",message = "姓名僅限中英文")
	private String firstName;
	@Lob
    @Size(max = 2 * 1024 * 1024, message = "圖片大小不能超過 2MB") // 限制圖片大小
	@Column(name = "avatar")
	private byte[] avatar;
	@Column(name = "birthday")
	@Past(message = "日期必須是在今日(不含)之前")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	@Column(name = "phone_number")
	@NotNull(message = "請輸入電話")
	private String phoneNumber;
	@Column(name = "gender")
	@NotNull(message = "請輸入性別")
	private String gender;
	@Column(name = "status")
	private Byte status;
	@Column(name = "create_time")
	private Timestamp createTime;
	
	// 連接到信用卡，一對多
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CreditcardVO> creditcard;
    //連接到訂單，一對多
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderVO> order;
    
	public MemberVO() {

	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "MemberVO [memberID=" + memberId + ", account=" + account + ", password=" + password + ", lastName="
				+ lastName + ", firstName=" + firstName + ", avatar=" + Arrays.toString(avatar) + ", birthday="
				+ birthday + ", phoneNumber=" + phoneNumber + ", gender=" + gender + ", status=" + status
				+ ", createTime=" + createTime + "]";
	}
	
}
