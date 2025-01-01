package com.admin.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("AdminService")
public class AdminService {
	
	@Autowired
	private AdminRepository repository;
	
//	@Autowired
//	private SessionFactory sessionFactory;
	
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
	
	// 新增管理員
	public Admin insert (Admin admin) {
		return repository.save(admin);
	}
	
	// 更新管理員
	public Admin update(Admin admin) {
		return repository.save(admin);
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

