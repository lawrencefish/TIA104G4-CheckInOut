package com.roomType.model;

import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelService hotelService; // 用於驗證 hotel 是否存在

    public List<RoomTypeVO> findByHotelId(Integer hotelId) {
        return roomTypeRepository.findByHotel_HotelId(hotelId);
    }

    @Transactional
    public void updateRoomType(Integer roomTypeId, RoomTypeVO roomTypeVO) {
        // 使用自定义查询加载房型和关联的房间
        Optional<RoomTypeVO> existingRoomTypeOptional = roomTypeRepository.findByIdWithRooms(roomTypeId);
        if (existingRoomTypeOptional.isEmpty()) {
            throw new IllegalArgumentException("找不到指定的房型 ID：" + roomTypeId);
        }

        RoomTypeVO existingRoomType = existingRoomTypeOptional.get();

        // 更新房型基本信息
        existingRoomType.setRoomName(roomTypeVO.getRoomName());
        existingRoomType.setMaxPerson(roomTypeVO.getMaxPerson());
        existingRoomType.setRoomNum(roomTypeVO.getRoomNum());
        existingRoomType.setBreakfast(roomTypeVO.getBreakfast());
        existingRoomType.setStatus(roomTypeVO.getStatus());
        roomTypeRepository.save(existingRoomType);

        // 获取新设置的房间数量
        int newRoomNum = roomTypeVO.getRoomNum();
        List<RoomVO> currentRooms = existingRoomType.getRooms();
        int currentRoomCount = currentRooms.size();

        if (newRoomNum > currentRoomCount) {
            // 增加房间
            for (int i = currentRoomCount + 1; i <= newRoomNum; i++) {
                RoomVO newRoom = new RoomVO();
                newRoom.setRoomType(existingRoomType);
                newRoom.setNumber(i); // 房间编号按序号增加
                newRoom.setStatus((byte) 0); // 默认状态为可用
                roomRepository.save(newRoom);
            }
        } else if (newRoomNum < currentRoomCount) {
            // 删除多余的房间，保留 room_id 最小的那些房间
            int roomsToDeleteCount = currentRoomCount - newRoomNum;
            List<RoomVO> roomsToDelete = currentRooms.stream()
                    .sorted(Comparator.comparing(RoomVO::getRoomId)) // 按 room_id 升序排序
                    .skip(newRoomNum) // 跳过前 newRoomNum 个房间（保留这些房间）
                    .toList();

            // 从房型中移除被删除的房间，确保持久化关系正确
            roomsToDelete.forEach(room -> {
                existingRoomType.getRooms().remove(room);
                roomRepository.delete(room);
            });
        }
    }




    // 新增房型
    // 新增房型
    public RoomTypeVO createRoomType(HotelVO hotel, RoomTypeVO roomTypeVO) {
        if (hotel == null) {
            throw new IllegalArgumentException("飯店資訊不存在");
        }

        // 設置房型屬性
        roomTypeVO.setHotel(hotel); // 綁定飯店
        roomTypeVO.setStatus((byte) 0); // 預設狀態為待審核

        // 儲存房型
        return roomTypeRepository.save(roomTypeVO);
    }

    @Transactional
    public void deleteRoomType(Integer roomTypeId) {
        roomTypeRepository.deleteById(roomTypeId);
    }
}