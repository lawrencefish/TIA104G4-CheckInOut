package com.room.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomVO, Integer> {
    List<RoomVO> findByRoomType_RoomTypeId(Integer roomTypeId);

    /**
     * 根據房型 ID 查詢所有可用的房間。
     *
     * @param roomTypeId 房型 ID
     * @return 該房型的可用房間列表
     */
    @Query("SELECT r FROM RoomVO r WHERE r.roomType.roomTypeId = :roomTypeId AND r.status = 0")
    List<RoomVO> findAvailableRoomsByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);

    Optional<RoomVO> findByOrderDetailId(Integer orderDetailId);
}