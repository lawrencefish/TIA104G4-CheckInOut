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

