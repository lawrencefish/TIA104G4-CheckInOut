package com.employee.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeVO loginCheck(String employeeNumber, String password) {
        Optional<EmployeeVO> opt = employeeRepository.findByEmployeeNumber(employeeNumber);
        if (opt.isEmpty()) {
            return null; // 找不到該員工編號
        }
        EmployeeVO emp = opt.get();
        if (!emp.getPassword().equals(password)) {
            return null; // 密碼錯
        }
        return emp; // 驗證成功
    }

    public Optional<EmployeeVO> findByEmployeeNumberAndHotel_HotelId(String employeeNumber, Integer hotelId) {
        return employeeRepository.findByEmployeeNumberAndHotel_HotelId(employeeNumber, hotelId);
    }

    public void updateEmployee(EmployeeVO employee) {
        employeeRepository.save(employee); // 使用 JPA 保存更新後的資料
    }

    public List<EmployeeVO> getEmployeesByHotelId(Integer hotelId) {
        return employeeRepository.findByHotel_HotelId(hotelId);
    }

    public EmployeeVO getEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public void update(EmployeeVO employee) {
        employeeRepository.save(employee); // 假設使用 JPA Repository 的 save 方法
    }

    public EmployeeVO updateLastLogin(EmployeeVO employee) {
        // 更新 lastLoginDate
        employee.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
        return employeeRepository.save(employee); // 保存到數據庫
    }

    public boolean existsByEmployeeNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber).isPresent();
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return employeeRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void addEmployee(EmployeeVO employeeVO) {
        employeeRepository.save(employeeVO);
    }

    public boolean existsByHotelIdAndEmployeeNumber(Integer hotelId, String employeeNumber) {
        return employeeRepository.existsByHotel_HotelIdAndEmployeeNumber(hotelId, employeeNumber);
    }

    public boolean existsByEmployeeNumberAndHotel(String employeeNumber, Integer hotelId) {
        return employeeRepository.existsByEmployeeNumberAndHotel_HotelId(employeeNumber, hotelId);
    }

    public boolean existsByEmailAndHotel(String email, Integer hotelId) {
        return employeeRepository.existsByEmailAndHotel_HotelId(email, hotelId);
    }

    public void save(EmployeeVO employee) {
        employeeRepository.save(employee);
    }
}
