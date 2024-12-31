package com.roomTypeImg.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeImgRepository extends JpaRepository<RoomTypeImgVO, Integer> {

    // 根據 roomTypeId 查詢所有相關圖片
    List<RoomTypeImgVO> findByRoomType_RoomTypeId(Integer roomTypeId);
}