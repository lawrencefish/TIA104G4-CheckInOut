package com.employee.model;



import com.hotel.model.HotelVO;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "employee")
public class EmployeeVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    // 多對一：多個 Employee 對應同一個 Hotel
    // 這裡對應 hotel 表的外鍵 (hotel_id)
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelVO hotel;

    // employee_number VARCHAR(10) NOT NULL UNIQUE
    @NotBlank(message = "員工編號不可為空")
    @Size(max = 10, message = "員工編號最長 10 碼")
    @Column(name = "employee_number", length = 10, nullable = false, unique = true)
    private String employeeNumber;

    // name VARCHAR(50) NOT NULL
    @NotBlank(message = "員工姓名不可為空")
    @Size(max = 50, message = "姓名長度不可超過 50")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    // password VARCHAR(20) NOT NULL
    @NotBlank(message = "密碼不可為空")
    @Size(max = 20, message = "密碼長度不可超過 20")
    @Column(name = "password", length = 20, nullable = false)
    private String password;

    // phone_number VARCHAR(15) NOT NULL UNIQUE
    @NotBlank(message = "電話不可為空")
    @Size(max = 15, message = "電話長度不可超過 15")
    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    // email VARCHAR(100) NOT NULL UNIQUE
    @NotBlank(message = "Email 不可為空")
    @Email(message = "Email 格式不正確")
    @Size(max = 100, message = "Email 長度不可超過 100")
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    // title VARCHAR(50) NOT NULL
    @NotBlank(message = "職稱不可為空")
    @Size(max = 50, message = "職稱長度不可超過 50")
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    // create_date DATETIME NOT NULL
    // 建議使用 LocalDateTime (Hibernate 5+ / JPA 2.2+ 皆可)
    @Column(name = "create_date", nullable = false, insertable = false, updatable = false)
    private Timestamp createDate;

    // last_login_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    // DB 端自動帶入當前時間，可選擇 insertable=false, updatable=false
    @Column(name = "last_login_date", nullable = false, insertable = false)
    private Timestamp lastLoginDate;


    // -----------------------------------------
    // Constructor, Getters, Setters
    // -----------------------------------------
    public EmployeeVO() {
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public HotelVO getHotel() {
        return hotel;
    }

    public void setHotel(HotelVO hotel) {
        this.hotel = hotel;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
