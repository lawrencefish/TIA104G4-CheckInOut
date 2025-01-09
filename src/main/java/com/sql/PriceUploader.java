package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PriceUploader {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String selectRoomTypes = "SELECT room_type_id FROM room_type ORDER BY room_type_id";
        String insertPrice = "INSERT INTO price (room_type_id, start_date, end_date, price_type, breakfast_price, price) VALUES (?, ?, ?, ?, ?, ?)";

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

            Random random = new Random();

            for (int roomTypeId : roomTypeIds) {
                // 為每個 room_type_id 插入三筆資料
                for (int priceType = 1; priceType <= 3; priceType++) {
                    Timestamp startDate = null;
                    Timestamp endDate = null;

                    if (priceType == 3) {
                        // 產生隨機的日期範圍 (start_date 和 end_date)
                        long startMillis = System.currentTimeMillis() + random.nextInt(30) * 24 * 60 * 60 * 1000L; // 隨機 0~30 天後
                        long endMillis = startMillis + random.nextInt(10) * 24 * 60 * 60 * 1000L; // 持續 0~10 天
                        startDate = new Timestamp(startMillis);
                        endDate = new Timestamp(endMillis);
                    }

                    int breakfastPrice = random.nextInt(500) + 50; // 早餐價格隨機生成 50~550
                    int price = random.nextInt(2000) + 500; // 房間價格隨機生成 500~2500

                    pstmt = con.prepareStatement(insertPrice);
                    pstmt.setInt(1, roomTypeId);
                    pstmt.setTimestamp(2, startDate);
                    pstmt.setTimestamp(3, endDate);
                    pstmt.setInt(4, priceType);
                    pstmt.setInt(5, breakfastPrice);
                    pstmt.setInt(6, price);
                    pstmt.executeUpdate();

                    System.out.printf("Inserted price for room_type_id: %d, price_type: %d%n", roomTypeId, priceType);
                }
            }

            System.out.println("All prices uploaded successfully.");
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
