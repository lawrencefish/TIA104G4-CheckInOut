package com.hotel.model;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<HotelVO, Integer> {
    List<HotelVO> findByCity(String city);
    // 若需要自訂查詢，可在此新增 method
    // e.g., List<HotelVO> findByCity(String city);

    // 假設 只有 taxId 與 password 兩個欄位要核對
    // 回傳 Optional<HotelVO>，若無符合則回傳空
    Optional<HotelVO> findByTaxIdAndPassword(String taxId, String password);

    Optional<HotelVO> findByTaxId(String taxId);

    boolean existsByTaxId(String taxId);

    boolean existsByName(String name);

    boolean existsByAddress(String address);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    // 使用 @EntityGraph 加載 hotelImgs 集合
    @EntityGraph(attributePaths = "hotelImgs") // 明確加載 hotelImgs 集合
    Optional<HotelVO> findById(Integer hotelId);
    
    // 管理員審核查詢用 -By Barry
    List<HotelVO> findByStatus(Integer status);
}
