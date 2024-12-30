package com.facility.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    public List<FacilityVO> getHotelFacilities(Integer facilityType, Integer isService) {
        return facilityRepository.findFacilitiesByTypeAndService(facilityType, isService);
    }

    public Optional<FacilityVO> findById(Integer facilityId) {
        return facilityRepository.findById(facilityId);
    }
}