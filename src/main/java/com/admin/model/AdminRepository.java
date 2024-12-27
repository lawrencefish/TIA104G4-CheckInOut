package com.admin.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    // 基本的 CRUD 操作都會自動實現
    // 可以加入自定義的查詢方法
}
