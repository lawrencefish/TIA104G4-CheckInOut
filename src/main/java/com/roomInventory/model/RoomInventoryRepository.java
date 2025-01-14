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


    @Query("SELECT r FROM RoomInventoryVO r WHERE r.date = :date")
    List<RoomInventoryVO> findByDate(@Param("date") LocalDate date);

    //搜尋庫存、旅館、房型、房價
    @Query("""
    	    SELECT new com.roomInventory.model.RoomInventoryDTO(
    	        ri.inventoryId, ri.date, ri.availableQuantity, 
    	        p.priceId, p.startDate, p.endDate, p.priceType, p.breakfastPrice, p.price, 
    	        h.hotelId, h.name, h.city, h.district, h.address, h.latitude, h.longitude, 
    	        rt.roomTypeId, rt.roomName, rt.maxPerson, rt.breakfast
    	    )
    	    FROM RoomInventoryVO ri
    	    JOIN ri.roomType rt
    	    JOIN rt.prices p
    	    JOIN rt.hotel h
    	    WHERE ri.date BETWEEN :startDate AND :endDate
    	      AND h.latitude BETWEEN :latitudeCenter - 0.25 AND :latitudeCenter + 0.25
    	      AND h.longitude BETWEEN :longitudeCenter - 0.25 AND :longitudeCenter + 0.25
    	      AND (
    	        (p.startDate IS NOT NULL AND p.endDate IS NOT NULL AND ri.date BETWEEN p.startDate AND p.endDate AND p.priceType = 3)
    	        OR
    	        (p.startDate IS NULL AND p.endDate IS NULL AND FUNCTION('DAYOFWEEK', ri.date) IN (1, 7) AND p.priceType = 2)
    	        OR
    	        (p.startDate IS NULL AND p.endDate IS NULL AND FUNCTION('DAYOFWEEK', ri.date) NOT IN (1, 7) AND p.priceType = 1)
    	      )
    	    ORDER BY rt.roomTypeId ASC
    	""")
    	List<RoomInventoryDTO> findRoomInventoryByTypeHotelAndPrice(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate,
    	    @Param("latitudeCenter") double latitudeCenter,
    	    @Param("longitudeCenter") double longitudeCenter
    	);    
}