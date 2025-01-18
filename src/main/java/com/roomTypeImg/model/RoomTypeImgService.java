package com.roomTypeImg.model;

import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Service
public class RoomTypeImgService {

    @Autowired
    private RoomTypeImgRepository roomTypeImgRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    // 獲取某房型的所有圖片
    public List<RoomTypeImgVO> findImagesByRoomTypeId(Integer roomTypeId) {
        return roomTypeImgRepository.findByRoomType_RoomTypeId(roomTypeId);
    }

    // 獲取指定 ID 的圖片
    public RoomTypeImgVO findById(Integer imageId) {
        return roomTypeImgRepository.findById(imageId).orElse(null);
    }

    // 上傳圖片
    // 上傳圖片時，從資料庫中獲取 RoomTypeVO
    public List<RoomTypeImgVO> uploadImages(List<MultipartFile> photos, Integer roomTypeId) {
        List<RoomTypeImgVO> savedImages = new ArrayList<>();

        // 從資料庫中查找 RoomTypeVO
        RoomTypeVO roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("無效的 RoomTypeId: " + roomTypeId));

        for (MultipartFile photo : photos) {
            try {
                RoomTypeImgVO roomTypeImg = new RoomTypeImgVO();
                roomTypeImg.setRoomType(roomType); // 設置關聯的 RoomType
                roomTypeImg.setPicture(photo.getBytes()); // 保存圖片數據
                savedImages.add(roomTypeImgRepository.save(roomTypeImg));
            } catch (IOException e) {
                throw new RuntimeException("圖片處理失敗：" + e.getMessage());
            }
        }
        return savedImages;
    }

    // 刪除圖片
    public void deleteImage(Integer imageId) {
        roomTypeImgRepository.deleteById(imageId);
    }

    // 批量刪除圖片
    public void deleteImages(List<Integer> imageIds) {
        for (Integer imageId : imageIds) {
            if (!roomTypeImgRepository.existsById(imageId)) {
                System.out.println("圖片 ID 不存在，跳過: " + imageId);
                continue; // 如果圖片不存在，跳過這個 ID
            }
            roomTypeImgRepository.deleteById(imageId); // 刪除圖片
        }
    }
    
    public Integer countByRoomTypeId(Integer roomTypeId){
    	return roomTypeImgRepository.countByRoomTypeRoomTypeId(roomTypeId);
    }
    
    // By Barry
    @Autowired
    private EntityManager entityManager;
    
    public byte[] getFirstImageByRoomTypeId(Integer roomTypeId) {
        try {
            RoomTypeImgVO firstImage = entityManager.createQuery(
                "SELECT ri FROM RoomTypeImgVO ri WHERE ri.roomType.roomTypeId = :roomTypeId ORDER BY ri.roomTypeImgId ASC",
                RoomTypeImgVO.class)
                .setParameter("roomTypeId", roomTypeId)
                .setMaxResults(1)
                .getSingleResult();
            
            return Optional.ofNullable(firstImage)
                .map(RoomTypeImgVO::getPicture)
                .orElse(null);
        } catch (NoResultException e) {
            return null;
        }
    }
    //--------------------
}