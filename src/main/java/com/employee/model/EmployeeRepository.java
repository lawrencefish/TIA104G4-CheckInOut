package com.employee.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeVO, Integer> {

    // 假設 employee_number 是唯一
    Optional<EmployeeVO> findByEmployeeNumber(String employeeNumber);

    Optional<EmployeeVO> findByEmployeeNumberAndHotel_HotelId(String employeeNumber, Integer hotelId);
}
