package com.employee.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeVO, Integer> {

    // 假設 employee_number 是唯一
    Optional<EmployeeVO> findByEmployeeNumber(String employeeNumber);

    Optional<EmployeeVO> findByEmployeeNumberAndHotel_HotelId(String employeeNumber, Integer hotelId);

    List<EmployeeVO> findByHotel_HotelId(Integer hotelId);

    @Modifying
    @Transactional
    @Query("UPDATE EmployeeVO e SET e.password = :password WHERE e.employeeId = :employeeId")
    int updatePasswordByEmployeeId(@Param("employeeId") Integer employeeId, @Param("password") String password);


    Optional<EmployeeVO> findByPhoneNumber(String phoneNumber);
    
    Optional<EmployeeVO> findByName(String employeeName);

    Optional<EmployeeVO> findByEmail(String email);

    boolean existsByHotel_HotelIdAndEmployeeNumber(Integer hotelId, String employeeNumber);

    boolean existsByEmployeeNumberAndHotel_HotelId(String employeeNumber, Integer hotelId);

    boolean existsByEmailAndHotel_HotelId(String email, Integer hotelId);
}
