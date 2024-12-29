package com.hotel.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Transactional
    public HotelVO addHotel(HotelVO hotel) {
        return hotelRepository.save(hotel);
    }

    @Transactional
    public void updateHotel(HotelVO hotel) {
        // 確保不為空的欄位被正確更新
        if (hotel != null && hotel.getHotelId() != null) { // 假設主鍵為 id
            HotelVO existingHotel = hotelRepository.findById(hotel.getHotelId())
                    .orElseThrow(() -> new IllegalArgumentException("Hotel not found with ID: " + hotel.getHotelId()));

            // 更新字段
            existingHotel.setName(hotel.getName());
            existingHotel.setPhoneNumber(hotel.getPhoneNumber());
            existingHotel.setEmail(hotel.getEmail());
            existingHotel.setCity(hotel.getCity());
            existingHotel.setDistrict(hotel.getDistrict());
            existingHotel.setAddress(hotel.getAddress());

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
}
