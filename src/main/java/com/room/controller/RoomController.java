package com.room.controller;

import com.room.model.RoomService;
import com.room.model.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/byRoomType/{roomTypeId}")
    public ResponseEntity<List<RoomVO>> getRoomsByRoomType(@PathVariable Integer roomTypeId) {
        try {
            List<RoomVO> rooms = roomService.getRoomsByRoomType(roomTypeId);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
