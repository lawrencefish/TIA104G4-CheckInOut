package com.hotelImg.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelImgService {
    @Autowired
    private HotelImgRepository hotelImgRepository;

    public List<HotelImgVO> getImagesByHotelId(Integer hotelId) {
        return hotelImgRepository.findByHotel_HotelId(hotelId);
    }

    public HotelImgVO getImageById(Integer imageId) {
        return hotelImgRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
    }
}
