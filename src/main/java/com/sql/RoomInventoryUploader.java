package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RoomInventoryUploader {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        String selectRoomTypes = "SELECT room_type_id, room_num FROM room_type ORDER BY room_type_id";
        String insertInventory = "INSERT INTO room_inventory (room_type_id, date, available_quantity) VALUES (?, ?, ?)";

        try {
            con = DriverManager.getConnection(url, userid, passwd);

            // 查詢所有房型
            pstmt = con.prepareStatement(selectRoomTypes);
            rs = pstmt.executeQuery();

            LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 2, 28);

            while (rs.next()) {
                int roomTypeId = rs.getInt("room_type_id");
                int roomNum = rs.getInt("room_num");

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    try (PreparedStatement insertStmt = con.prepareStatement(insertInventory)) {
                        insertStmt.setInt(1, roomTypeId);
                        insertStmt.setDate(2, java.sql.Date.valueOf(date));
                        insertStmt.setInt(3, roomNum);
                        insertStmt.executeUpdate();

                        System.out.printf("新增庫存: 日期=%s, 房型ID=%d, 庫存數量=%d%n", date, roomTypeId, roomNum);
                    }
                }
            }

            System.out.println("所有庫存資料已成功新增。");
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
