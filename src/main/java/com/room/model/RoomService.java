package com.room.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public void saveAll(List<RoomVO> rooms) {
        roomRepository.saveAll(rooms);
    }

    public List<RoomVO> getRoomsByRoomType(Integer roomTypeId) {
        return roomRepository.findByRoomType_RoomTypeId(roomTypeId);
    }
}