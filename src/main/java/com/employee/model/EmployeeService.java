package com.employee.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
