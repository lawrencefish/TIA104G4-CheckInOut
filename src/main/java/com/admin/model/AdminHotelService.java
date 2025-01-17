package com.admin.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.model.HotelRepository;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;

@Service("AdminHotelService")
public class AdminHotelService {
	
	private final HotelRepository hotelRepository; 
	
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
		// TODO Auto-generated method stub
		return null;
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
}
