package com.roomTypeFacility.model;

import com.facility.model.FacilityRepository;
import com.facility.model.FacilityVO;
import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoomTypeFacilityService {

    @Autowired
    private RoomTypeFacilityRepository roomTypeFacilityRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private FacilityRepository facilityRepository;

    /**
     * 根據房型 ID 獲取指定的設施或服務
     * @param roomTypeId 房型 ID
     * @param isService 是否為服務 (0: 設施, 1: 服務)
     * @return 設施或服務列表
     */
    public List<FacilityVO> getFacilitiesOrServicesByRoomType(Integer roomTypeId, Integer isService) {
        return roomTypeFacilityRepository.findFacilitiesOrServicesByRoomTypeAndType(roomTypeId, isService);
    }

    @Transactional
    public void updateFacilitiesAndServices(Integer roomTypeId, List<Integer> facilityIds, List<Integer> serviceIds) {
        // 驗證房型是否存在
        RoomTypeVO roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("無效的房型 ID"));

        // 刪除該房型現有的設施與服務
        roomTypeFacilityRepository.deleteByRoomType(roomType);

        // 新增設施
        for (Integer facilityId : facilityIds) {
            FacilityVO facility = facilityRepository.findById(facilityId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的設施 ID"));
            RoomTypeFacilityVO roomTypeFacility = new RoomTypeFacilityVO();
            roomTypeFacility.setRoomType(roomType);
            roomTypeFacility.setFacility(facility);
            roomTypeFacilityRepository.save(roomTypeFacility);
        }

        // 新增服務
        for (Integer serviceId : serviceIds) {
            FacilityVO service = facilityRepository.findById(serviceId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的服務 ID"));
            RoomTypeFacilityVO roomTypeFacility = new RoomTypeFacilityVO();
            roomTypeFacility.setRoomType(roomType);
            roomTypeFacility.setFacility(service);
            roomTypeFacilityRepository.save(roomTypeFacility);
        }
    }
}