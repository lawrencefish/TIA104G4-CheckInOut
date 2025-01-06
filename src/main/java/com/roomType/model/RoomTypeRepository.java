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

}