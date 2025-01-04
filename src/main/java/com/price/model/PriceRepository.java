package com.price.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceVO, Integer> {
    List<PriceVO> findByRoomType_RoomTypeId(Integer roomTypeId); // 根據房型ID查詢價格

    List<PriceVO> findByRoomTypeRoomTypeIdInAndPriceType(List<Integer> roomTypeIds, Byte priceType);
}