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
    int RoomInventoryDTO = 0;

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

    //搜尋庫存、旅館、房型、地點
    @Query("SELECT new com.roomInventory.model.RoomInventoryDTO(" +
    	       "ri.inventoryId, ri.date, ri.availableQuantity, " +
    	       "h.hotelId, h.name, h.city, h.district, h.address, " +
    	       "h.latitude, h.longitude, " +
    	       "rt.roomTypeId, rt.maxPerson, rt.breakfast) " +
    	       "FROM RoomInventoryVO ri " +
    	       "JOIN ri.roomType rt " +
    	       "JOIN rt.hotel h " +
    	       "WHERE ri.date BETWEEN :startDate AND :endDate " +
    	       "AND h.latitude BETWEEN :latitude - :radius AND :latitude + :radius " +
    	       "AND h.longitude BETWEEN :longitude - :radius AND :longitude + :radius")
    	List<RoomInventoryDTO> findAvailableRooms(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate,
    	    @Param("latitude") Double latitude,
    	    @Param("longitude") Double longitude,
    	    @Param("radius") Double radius
    	);
    
    //搜尋庫存、房型
    @Query("SELECT new com.roomInventory.model.HotelRoomInventoryDTO(" +
    	       "ri.inventoryId, ri.date, ri.availableQuantity, " +
    	       "h.hotelId, h.name," +                               // 在此處添加逗號
    	       "rt.roomTypeId, rt.roomName, rt.maxPerson, rt.breakfast) " +
    	       "FROM RoomInventoryVO ri " +
    	       "JOIN ri.roomType rt " +
    	       "JOIN rt.hotel h " +
    	       "WHERE h.hotelId = :hotelId")
    	List<HotelRoomInventoryDTO> findAvailableRoomsFromHotel(
    	    @Param("hotelId") Integer hotelId
    	);

    //取得旅館、特定房型、特定日期
    @Query("SELECT new com.roomInventory.model.HotelRoomInventoryDTO(" +
    	       "ri.inventoryId, ri.date, ri.availableQuantity, " +
    	       "h.hotelId, h.name," +                               // 在此處添加逗號
    	       "rt.roomTypeId, rt.roomName, rt.maxPerson, rt.breakfast) " +
    	       "FROM RoomInventoryVO ri " +
    	       "JOIN ri.roomType rt " +
    	       "JOIN rt.hotel h " +
    	       "WHERE ri.date between :startDate and :endDate AND rt.roomTypeId = :roomTypeId")
    	List<HotelRoomInventoryDTO> findRoomsFromDateAndRoomTypeId(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate,
    	    @Param("roomTypeId") Integer roomTypeId
    	);

    
    //從ID找庫存
    @Query("SELECT ri FROM RoomInventoryVO ri WHERE ri.roomType.roomTypeId = :roomTypeId AND ri.date = :date")
    RoomInventoryVO findByRoomTypeIdAndDate(
        @Param("roomTypeId") int roomTypeId,
        @Param("date") LocalDate date
    );
    
    RoomInventoryVO findByRoomTypeRoomTypeId(Integer roomTypeId);
    
    //取得每日庫存量
    @Query(value = "SELECT date, COUNT(*) FROM room_inventory GROUP BY date", nativeQuery = true)
    List<Object[]> countRoomsByDate();

}