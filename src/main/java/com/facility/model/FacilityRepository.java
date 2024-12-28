package com.facility.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<FacilityVO, Integer> {

    // 查詢特定類型與設施/服務
    @Query("SELECT f FROM FacilityVO f WHERE f.facilityType = :facilityType AND f.isService = :isService")
    List<FacilityVO> findFacilitiesByTypeAndService(@Param("facilityType") Integer facilityType, @Param("isService") Integer isService);

}
