package com.hotel.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    // 新增或更新 Hotel
    public HotelVO saveHotel(HotelVO hotel) {
        // 可在這裡做一些檢查或商業邏輯
        return hotelRepository.save(hotel);
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
}
