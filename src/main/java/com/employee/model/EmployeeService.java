package com.employee.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
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
    
    public EmployeeVO getEmployeeByName(String employeeName) {
        return employeeRepository.findByName(employeeName).orElse(null);
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

    public Optional<EmployeeVO> findByEmployeeNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber);
    }

    public boolean existsByHotelId(Integer hotelId) {
        return employeeRepository.existsByHotel_HotelId(hotelId);
    }


    public Optional<EmployeeVO> findByEmployeeId(Integer employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    // 定義職位等級映射，數字越小職位越大
    private final Map<String, Integer> titleHierarchy = Map.of(
            "負責人", 1,
            "總經理", 2,
            "經理", 3,
            "襄理", 4,
            "員工", 5
    );

    public void deleteEmployee(Integer employeeId, EmployeeVO currentEmployee) {
        // 獲取被刪除的員工
        EmployeeVO targetEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("員工不存在，無法刪除"));

        // 禁止刪除負責人
        if (currentEmployee.getEmployeeId() == targetEmployee.getEmployeeId()) {
            throw new RuntimeException("不可刪");
        }
        if ("負責人".equals(targetEmployee.getTitle()) && !("負責人".equals(currentEmployee.getTitle()))) {
            throw new RuntimeException("想造反阿？");
        }

        // 確保當前操作員工的職位高於被刪除員工
        int currentEmployeeRank = titleHierarchy.getOrDefault(currentEmployee.getTitle(), Integer.MAX_VALUE);
        int targetEmployeeRank = titleHierarchy.getOrDefault(targetEmployee.getTitle(), Integer.MAX_VALUE);

        if (currentEmployeeRank >= targetEmployeeRank) {
            throw new RuntimeException("不可刪除你的上司");
        }

        // 執行刪除
        employeeRepository.deleteById(employeeId);
    }
}
