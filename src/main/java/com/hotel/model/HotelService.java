package com.hotel.model;

import com.facility.model.FacilityRepository;
import com.facility.model.FacilityVO;
import com.hotelFacility.model.HotelFacilityRepository;
import com.hotelFacility.model.HotelFacilityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelFacilityRepository hotelFacilityRepository;
    @Autowired
    private FacilityRepository facilityRepository;

    @Transactional
    public HotelVO addHotel(HotelVO hotel) {
        return hotelRepository.save(hotel);
    }

    @Transactional
    public void updateHotel(HotelVO hotel) {
        // 確保不為空的欄位被正確更新
        if (hotel != null && hotel.getHotelId() != null) {
            HotelVO existingHotel = hotelRepository.findById(hotel.getHotelId())
                    .orElseThrow(() -> new IllegalArgumentException("Hotel not found with ID: " + hotel.getHotelId()));

            // 更新字段
            existingHotel.setName(hotel.getName());
            existingHotel.setPhoneNumber(hotel.getPhoneNumber());
            existingHotel.setEmail(hotel.getEmail());
            existingHotel.setCity(hotel.getCity());
            existingHotel.setDistrict(hotel.getDistrict());
            existingHotel.setAddress(hotel.getAddress());
            existingHotel.setOwner(hotel.getOwner());

            // 更新圖片欄位（如果有提供新的圖片）
            if (hotel.getIdFront() != null) {
                existingHotel.setIdFront(hotel.getIdFront());
            }
            if (hotel.getIdBack() != null) {
                existingHotel.setIdBack(hotel.getIdBack());
            }
            if (hotel.getLicense() != null) {
                existingHotel.setLicense(hotel.getLicense());
            }

            // 保存更新
            hotelRepository.save(existingHotel);
        } else {
            throw new IllegalArgumentException("Invalid hotel data.");
        }
    }

    // 新增或更新 Hotel
    @Transactional
    public HotelVO saveHotel(HotelVO hotel) {
        // 可在這裡做一些檢查或商業邏輯
        return hotelRepository.save(hotel);
    }

    public HotelVO getHotelWithImages(Integer hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + hotelId));
    }

    // 根據城市找所有Hotel
    public List<HotelVO> findByCity(String city) {
        return hotelRepository.findByCity(city);
    }

    // 根據 ID 找單筆
    public Optional<HotelVO> findById(Integer id) {
        return hotelRepository.findById(id);
    }

    // 刪除
    public void deleteById(Integer id) {
        hotelRepository.deleteById(id);
    }

    public boolean loginCheck(String taxId, String password) {
        // 去找看是否有符合的 HotelVO
        Optional<HotelVO> hotelOpt = hotelRepository.findByTaxIdAndPassword(taxId, password);
        return hotelOpt.isPresent(); // true 表示找到，false 表示沒找到
    }

    public Optional<HotelVO> findByTaxId(String taxId) {
        return hotelRepository.findByTaxId(taxId);
    }

    public boolean existsByTaxId(String taxId) {
        return hotelRepository.existsByTaxId(taxId);
    }

    public boolean existsByName(String name) {
        return hotelRepository.existsByName(name);
    }

    public boolean existsByAddress(String address) {
        return hotelRepository.existsByAddress(address);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {

        return hotelRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean existsByEmail(String email) {
        return hotelRepository.existsByEmail(email);
    }

    @Transactional
    public void updateHotelPassword(Integer hotelId, String newPassword) {
        if (hotelId == null || newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid hotel ID or password.");
        }

        HotelVO existingHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with ID: " + hotelId));

        // 更新密碼
        existingHotel.setPassword(newPassword);

        // 保存變更
        hotelRepository.save(existingHotel);
    }

    public void updateHotelInfo(Integer hotelId, String infoText) {
        // 驗證飯店是否存在
        HotelVO hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("無效的飯店 ID"));

        // 更新 infoText
        hotel.setInfoText(infoText);
        hotelRepository.save(hotel);
    }

    public void saveHotelInfoText(HotelVO hotel) {
        // 只更新 infoText，保留其他字段不變
        hotelRepository.save(hotel);
    }

    /**
     * 更新飯店的設施與服務
     * @param hotel 當前飯店
     * @param facilityIds 設施 ID 列表
     * @param serviceIds 服務 ID 列表
     */
    public void updateFacilities(HotelVO hotel, List<Integer> facilityIds, List<Integer> serviceIds) {
        // 清空當前飯店的所有設施與服務
        hotelFacilityRepository.deleteByHotelId(hotel.getHotelId());

        // 新增設施
        if (facilityIds != null && !facilityIds.isEmpty()) {
            facilityIds.forEach(facilityId -> {
                FacilityVO facility = facilityRepository.findById(facilityId)
                        .orElseThrow(() -> new IllegalArgumentException("無效的設施 ID: " + facilityId));

                HotelFacilityVO hotelFacility = new HotelFacilityVO();
                hotelFacility.setHotel(hotel);
                hotelFacility.setFacility(facility);
                hotelFacilityRepository.save(hotelFacility);
            });
        }

        // 新增服務
        if (serviceIds != null && !serviceIds.isEmpty()) {
            serviceIds.forEach(serviceId -> {
                FacilityVO service = facilityRepository.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("無效的服務 ID: " + serviceId));

                HotelFacilityVO hotelService = new HotelFacilityVO();
                hotelService.setHotel(hotel);
                hotelService.setFacility(service);
                hotelFacilityRepository.save(hotelService);
            });
        }
    }

    public void save(HotelVO hotelVO) {
        hotelRepository.save(hotelVO);
    }

    public List<HotelVO> findAll() {
        return hotelRepository.findAll();
    }

    public byte[] getImageByType(HotelVO hotel, String type) {
        switch (type) {
            case "idFront":
                return hotel.getIdFront();
            case "idBack":
                return hotel.getIdBack();
            case "license":
                return hotel.getLicense();
            default:
                return null; // 返回空值，供控制器進一步處理
        }
    }
}
