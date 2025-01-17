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
	
	// 通用搜尋：根據關鍵字、狀態和權限
    @Query("SELECT a FROM Admin a WHERE " +
           "(:keyword IS NULL OR a.adminAccount LIKE %:keyword% OR a.email LIKE %:keyword% OR a.phoneNumber LIKE %:keyword%) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:permissions IS NULL OR a.permissions = :permissions)")
    List<Admin> searchAdmins(
        @Param("keyword") String keyword,
        @Param("status") Byte status,
        @Param("permissions") Byte permissions
    );

    // 根據狀態與關鍵字查詢
    @Query("SELECT a FROM Admin a WHERE (:status IS NULL OR a.status = :status) " +
           "AND (a.adminAccount LIKE %:keyword% OR a.email LIKE %:keyword%)")
    List<Admin> findByStatusAndKeyword(
        @Param("status") Byte status,
        @Param("keyword") String keyword
    );

    // 根據權限與關鍵字查詢
    @Query("SELECT a FROM Admin a WHERE (:permissions IS NULL OR a.permissions = :permissions) " +
           "AND (a.adminAccount LIKE %:keyword% OR a.email LIKE %:keyword%)")
    List<Admin> findByPermissionsAndKeyword(
        @Param("permissions") Byte permissions,
        @Param("keyword") String keyword
    );

    // 僅根據關鍵字查詢
    @Query("SELECT a FROM Admin a WHERE " +
           "a.adminAccount LIKE %:keyword% OR a.email LIKE %:keyword% OR a.phoneNumber LIKE %:keyword%")
    List<Admin> findByKeyword(@Param("keyword") String keyword);
}

	// 基本的 CRUD 操作都會自動實現
	// 可以加入自定義的查詢方法
//@Repository
//public interface AdminLogRepository extends JpaRepository<AdminLog, Integer>{
//	List<AdminLog> findByAdmin(Integer adminId);
//}
