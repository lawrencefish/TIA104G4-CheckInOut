package com.admin.model;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service("AdminService")
public class AdminService {
	
	@Autowired
	private AdminRepository repository;
	
//	@Autowired
//	private AdminLogRepository logRepository;
	
//	@Autowired
//	private SessionFactory sessionFactory;
	
	// 搜尋方法
    public List<Admin> searchAdmins(String keyword, Byte status, Byte permissions){
        
    	if (keyword == null) {
    		// 如果只有狀態或權限的篩選
            if (status != null && permissions == null) {
                return repository.findByStatus(status);
            } else if (permissions != null && status == null) {
                return repository.findByPermissions(permissions);
            } else if (status != null && permissions != null) {
                return repository.findByStatusAndPermissions(status, permissions);
            } else {
                return repository.findAll();
            }
        }
    	
    	// keyword 不為 null，處理搜索邏輯
        String trimmedKeyword = keyword.trim();
        if (trimmedKeyword.isEmpty()) {
            return repository.findAll();
        }
        
        // 有關鍵字的搜尋
        if (status != null && permissions == null) {
            return repository.findByStatusAndKeyword(status, trimmedKeyword);
        } else if (permissions != null && status == null) {
            return repository.findByPermissionsAndKeyword(permissions, trimmedKeyword);
        } else if (status != null && permissions != null) {
            return repository.searchAdmins(trimmedKeyword, status, permissions);
        } else {
            return repository.findByKeyword(trimmedKeyword);
        }
    }
	// 日誌紀錄
//	public void logAction(AdminLogDTO logDTO) {
//		AdminLog log = new AdminLog();
//		log.setAdminId(logDTO.getAdmin());
//		log.setAction(logDTO.getAction());
//		log.setActionTime(new Date());
//		logRepository.save(log);
//	}
	
	public Admin adminLogin(String email, String password) {
		System.out.println("嘗試使用信箱登入:" + email);
		
		// 從資料庫查詢管理員帳號
		Admin admin = repository.findByEmail(email);
		if (admin != null) {
			System.out.println("尋找管理員:" + admin.getEmail());
			if (admin.getAdminPassword().equals(password)) {
				System.out.println("密碼正確");
				return admin; // 帳密正確 回傳管理員資料				
			}
			System.out.println("查無密碼");
		} else {
			System.out.println("查無管理員信箱:" + email);
		}
		return null; // 登入失敗
	}
	
//	public List<Admin> searchAdmins(String keyword, String status, String permissions){
//		List<Admin> admins = repository.findAll();
//		
//		return admins.stream().filter(admin -> 
//					(keyword.isEmpty() || 
//					admin.getAdminAccount().contains(keyword) ||
//					admin.getEmail().contains(keyword) ||
//					admin.getPhoneNumber().contains(keyword)) &&
//					(status.equals("all") ||
//					admin.getStatus().toString().equals(status)) &&
//					(permissions.equals("all") ||
//					admin.getPermissions().toString().equals(permissions)))
//					.collect(Collectors.toList());
//	}
	
	// 新增管理員
	public Admin insert (Admin admin) {
		return repository.save(admin);
	}
	
	// 更新管理員
	public Admin update(Admin admin) {
        try {
            // 確保必要欄位不為 null
            Objects.requireNonNull(admin.getAdminId(), "Admin ID must not be null");
            Objects.requireNonNull(admin.getAdminAccount(), "Admin account must not be null");
            
            // 驗證 email 格式
            if (admin.getEmail() != null && !admin.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            
            // 驗證電話號碼格式
            if (admin.getPhoneNumber() != null && !admin.getPhoneNumber().matches("^\\d{10}$")) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
            
            return repository.save(admin);
        } catch (Exception e) {
            throw new RuntimeException("Error updating admin: " + e.getMessage(), e);
        }
    }
	
	// 刪除管理員
	public void delete(Integer adminId) {
		if(repository.existsById(adminId))
			repository.deleteById(adminId);
	}
	
	// 查詢單一管理員
	public Admin getById(Integer adminId) {
		Optional<Admin> optional = repository.findById(adminId);
		return optional.orElse(null);
	}
	
	// 查詢所有管理員
	public List<Admin> getAll(int currentPage){
		return repository.findAll();
	}
	
	// 複合查詢
//	public List<Admin> getByCompositeQuery(Map<String, String[]> map){
//		return HibernateUtil_CompositeQuery_Admin.getAllC(map, sessionFactory.openSession());
//	}
}

