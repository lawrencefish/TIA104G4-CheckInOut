package com.roomType.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeVO, Integer> {
    List<RoomTypeVO> findByHotel_HotelId(Integer hotelId);

    @Query("SELECT rt FROM RoomTypeVO rt LEFT JOIN FETCH rt.rooms WHERE rt.roomTypeId = :roomTypeId")
    Optional<RoomTypeVO> findByIdWithRooms(@Param("roomTypeId") Integer roomTypeId);
    
 // 房型審核用 -By Barry
    @Query("SELECT r FROM RoomTypeVO r JOIN FETCH r.hotel h WHERE h.hotelId = :hotelId")
	List<RoomTypeVO> findByHotel(Integer hotelId);
	
	@Query("SELECT r FROM RoomTypeVO r WHERE r.status = :status")
	List<RoomTypeVO> findByStatus(Byte status);
	
    @Query("SELECT DISTINCT r FROM RoomTypeVO r LEFT JOIN FETCH r.hotel")
    List<RoomTypeVO> findAllWithHotel();

}