package com.hotelImg.model;

import com.hotel.model.HotelRepository;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Service
public class HotelImgService {
    @Autowired
    private HotelImgRepository hotelImgRepository;
    @Autowired
    private HotelRepository hotelRepository;

    public List<HotelImgVO> getImagesByHotelId(Integer hotelId) {
        return hotelImgRepository.findByHotel_HotelId(hotelId);
    }

    public HotelImgVO getImageById(Integer imageId) {
        return hotelImgRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
    }
    
    // 上傳圖片
    public List<HotelImgVO> uploadImages(List<MultipartFile> photos, Integer hotelId) throws IOException {
        // 確認飯店是否存在
        HotelVO hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("飯店 ID 無效"));

        List<HotelImgVO> savedImages = new ArrayList<>();

        for (MultipartFile photo : photos) {
            HotelImgVO hotelImg = new HotelImgVO();
            hotelImg.setHotel(hotel); // 設置對應飯店
            hotelImg.setPicture(photo.getBytes()); // 儲存圖片 BLOB
            savedImages.add(hotelImgRepository.save(hotelImg)); // 儲存到資料庫
        }

        return savedImages;
    }

    public void deleteImage(Integer imageId) {
        if (!hotelImgRepository.existsById(imageId)) {
            throw new IllegalArgumentException("圖片 ID 無效");
        }
        hotelImgRepository.deleteById(imageId);
    }

    public void save(HotelImgVO hotelImgVO) {
        if (hotelImgVO == null) {
            throw new IllegalArgumentException("HotelImgVO cannot be null");
        }
        hotelImgRepository.save(hotelImgVO);
    }
    //找個數
    public Integer countByHotelId(Integer hotelId) {
    	return hotelImgRepository.countByHotelHotelId(hotelId);
    }
    
    // By Barry
    @Autowired
    private EntityManager entityManager;
    
    public byte[] getFirstImageByHotelId(Integer hotelId) {
        try {
            HotelImgVO firstImage = entityManager.createQuery(
                "SELECT hi FROM HotelImgVO hi WHERE hi.hotel.hotelId = :hotelId ORDER BY hi.hotelImgId ASC",
                HotelImgVO.class)
                .setParameter("hotelId", hotelId)
                .setMaxResults(1)
                .getSingleResult();
            
            return Optional.ofNullable(firstImage)
                .map(HotelImgVO::getPicture)
                .orElse(null);
        } catch (NoResultException e) {
            return null;
        }
    }
}
