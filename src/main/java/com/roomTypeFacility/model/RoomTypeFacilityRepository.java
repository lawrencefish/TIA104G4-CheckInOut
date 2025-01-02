package com.roomTypeFacility.model;

import com.facility.model.FacilityVO;
import com.roomType.model.RoomTypeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeFacilityRepository extends JpaRepository<RoomTypeFacilityVO, Integer> {

    /**
     * 查詢特定房型的設施與服務
     * @param roomTypeId 房型 ID
     * @param isService 是否為服務 (0: 設施, 1: 服務)
     * @return 對應的設施或服務
     */
    @Query("SELECT f FROM RoomTypeFacilityVO rtf JOIN rtf.facility f " +
            "WHERE rtf.roomType.roomTypeId = :roomTypeId AND f.isService = :isService")
    List<FacilityVO> findFacilitiesOrServicesByRoomTypeAndType(
            @Param("roomTypeId") Integer roomTypeId,
            @Param("isService") Integer isService);

    void deleteByRoomType(RoomTypeVO roomType);
}