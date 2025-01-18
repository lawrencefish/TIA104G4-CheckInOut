package com.admin.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.model.HotelRepository;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import com.hotelImg.model.HotelImgRepository;
import com.hotelImg.model.HotelImgVO;

@Service("AdminHotelService")
public class AdminHotelService {
	
	
	private final HotelRepository hotelRepository; 
	
	@Autowired
    private HotelImgRepository hotelImgRepository;
	
	public AdminHotelService(HotelRepository hotelRepository) {
	        this.hotelRepository = hotelRepository;
	    }
	 
	public List<HotelVO> findAllHotels(){
		return hotelRepository.findAll();
	}
	
	// 根據狀態查找飯店
    public List<HotelVO> findByStatus(Integer status) {
        return hotelRepository.findByStatus(status);
    }

    // 更新審核狀態
    @Transactional
    public void updateStatus(Integer hotelId, Integer status) {
        HotelVO hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));
        
        hotel.setStatus(status);
        hotel.setReviewTime(new Timestamp(System.currentTimeMillis()));
        hotelRepository.save(hotel);
    }

	public List<HotelVO> findAll() {
		// TODO Auto-generated method stub
//		return null;
		return hotelRepository.findAll();
	}

	public Optional<HotelVO> findById(Integer id) {
		
		if (id == null) {
            return Optional.empty();
        }
        try {
            return hotelRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
	}
	
	public String getStatusDescription(byte status) {
        switch(status) {
            case 0: return "待審核";
            case 1: return "審核通過";
            case 2: return "審核未通過";
            case 3: return "帳號停權";
            default: return "未知狀態";
        }
	}

	// 根據飯店 ID 查找所有圖片
    public List<HotelImgVO> findImagesByHotelId(Integer hotelId) {
        Optional<HotelVO> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isEmpty()) {
            return new ArrayList<>();
        }
        return hotelOpt.get().getHotelImgs() != null ? 
               hotelOpt.get().getHotelImgs() : 
               new ArrayList<>();
    }

	// 根據圖片 ID 查找圖片
	public Optional<HotelImgVO> findImageById(Integer imageId) {
		 if (imageId == null) {
            return Optional.empty();
        }
        return hotelImgRepository.findById(imageId);
	}    
}
