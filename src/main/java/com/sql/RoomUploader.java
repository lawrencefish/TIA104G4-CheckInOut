package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomUploader {

    public static void run() {
        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 查詢 room_type 表
        String selectRoomTypeQuery = "SELECT room_type_id, room_num FROM room_type ORDER BY room_type_id";
        // 插入 room 表
        String insertRoomQuery = "INSERT INTO room (room_type_id, number, status) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, userid, passwd);

            // 查詢所有 room_type 資料
            pstmt = con.prepareStatement(selectRoomTypeQuery);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int roomTypeId = rs.getInt("room_type_id");
                int roomNum = rs.getInt("room_num");

                // 插入與 room_num 對應的房間數據
                for (int i = 1; i <= roomNum; i++) {
                    PreparedStatement insertStmt = con.prepareStatement(insertRoomQuery);
                    insertStmt.setInt(1, roomTypeId);
                    insertStmt.setInt(2, i); // 房間號碼 (1, 2, 3, ...)
                    insertStmt.setInt(3, 1); // 預設狀態為 1 (啟用)
                    insertStmt.executeUpdate();
                    insertStmt.close();
                }
                System.out.printf("Inserted %d rooms for room_type_id: %d%n", roomNum, roomTypeId);
            }

            System.out.println("All rooms inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
