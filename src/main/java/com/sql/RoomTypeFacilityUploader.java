package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RoomTypeFacilityUploader {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String selectRoomTypes = "SELECT room_type_id FROM room_type ORDER BY room_type_id";
        String insertRoomTypeFacility = "INSERT INTO room_type_facility (room_type_id, facility_id) VALUES (?, ?)";

        try {
            con = DriverManager.getConnection(url, userid, passwd);

            // 查詢所有 room_type_id
            pstmt = con.prepareStatement(selectRoomTypes);
            rs = pstmt.executeQuery();

            List<Integer> roomTypeIds = new ArrayList<>();
            while (rs.next()) {
                roomTypeIds.add(rs.getInt("room_type_id"));
            }
            rs.close();
            pstmt.close();

            if (roomTypeIds.isEmpty()) {
                System.out.println("No room types found in the database.");
                return;
            }

            List<Integer> facilityIds = new ArrayList<>();
            for (int i = 11; i <= 20; i++) {
                facilityIds.add(i);
            }

            Random random = new Random();

            for (int roomTypeId : roomTypeIds) {
                // 隨機選擇 3 個不重複的 facility_id
                Collections.shuffle(facilityIds, random);
                List<Integer> selectedFacilities = facilityIds.subList(0, 3);

                for (int facilityId : selectedFacilities) {
                    pstmt = con.prepareStatement(insertRoomTypeFacility);
                    pstmt.setInt(1, roomTypeId);
                    pstmt.setInt(2, facilityId);
                    pstmt.executeUpdate();

                    System.out.println("Inserted facility_id: " + facilityId + " for room_type_id: " + roomTypeId);
                }
            }

            System.out.println("All room type facilities uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
