package com.roomInventory.model;

import com.roomType.model.RoomTypeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventoryVO, Integer> {
    @Query("SELECT ri FROM RoomInventoryVO ri WHERE ri.roomType.roomTypeId IN :roomTypeIds")
    List<RoomInventoryVO> findByRoomTypeIds(@Param("roomTypeIds") List<Integer> roomTypeIds);

    @Query("SELECT ri FROM RoomInventoryVO ri WHERE ri.roomType IN :roomTypes AND ri.date BETWEEN :startDate AND :endDate")
    List<RoomInventoryVO> findByRoomTypesAndDateRange(
            @Param("roomTypes") List<RoomTypeVO> roomTypes,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM RoomInventoryVO r WHERE r.date BETWEEN :startDate AND :endDate")
    List<RoomInventoryVO> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}