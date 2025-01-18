package com.admin.model;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByEmail(String email);
	
	 // 根據狀態查詢
    List<Admin> findByStatus(Byte status);
    
    // 根據權限查詢
    List<Admin> findByPermissions(Byte permissions);
    
    // 根據狀態和權限查詢
    List<Admin> findByStatusAndPermissions(Byte status, Byte permissions);
    
    // 根據關鍵字查詢 (可以搜索帳號、Email、電話)
    @Query("SELECT a FROM Admin a WHERE a.adminAccount LIKE %:keyword% " +
           "OR a.email LIKE %:keyword% " +
           "OR a.phoneNumber LIKE %:keyword%")
    List<Admin> findByKeyword(@Param("keyword") String keyword);
    
    // 根據狀態和關鍵字查詢
    @Query("SELECT a FROM Admin a WHERE (a.adminAccount LIKE %:keyword% " +
           "OR a.email LIKE %:keyword% " +
           "OR a.phoneNumber LIKE %:keyword%) " +
           "AND a.status = :status")
    List<Admin> findByStatusAndKeyword(@Param("status") Byte status, 
                                     @Param("keyword") String keyword);
    
    // 根據權限和關鍵字查詢
    @Query("SELECT a FROM Admin a WHERE (a.adminAccount LIKE %:keyword% " +
           "OR a.email LIKE %:keyword% " +
           "OR a.phoneNumber LIKE %:keyword%) " +
           "AND a.permissions = :permissions")
    List<Admin> findByPermissionsAndKeyword(@Param("permissions") Byte permissions, 
                                          @Param("keyword") String keyword);
    
    // 根據狀態、權限和關鍵字查詢
    @Query("SELECT a FROM Admin a WHERE (a.adminAccount LIKE %:keyword% " +
           "OR a.email LIKE %:keyword% " +
           "OR a.phoneNumber LIKE %:keyword%) " +
           "AND a.status = :status " +
           "AND a.permissions = :permissions")
    List<Admin> searchAdmins(@Param("keyword") String keyword,
                            @Param("status") Byte status,
                            @Param("permissions") Byte permissions);
}

	// 基本的 CRUD 操作都會自動實現
	// 可以加入自定義的查詢方法
//@Repository
//public interface AdminLogRepository extends JpaRepository<AdminLog, Integer>{
//	List<AdminLog> findByAdmin(Integer adminId);
//}
