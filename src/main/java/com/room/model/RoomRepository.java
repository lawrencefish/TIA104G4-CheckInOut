package com.room.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomVO, Integer> {
    List<RoomVO> findByRoomType_RoomTypeId(Integer roomTypeId);
}