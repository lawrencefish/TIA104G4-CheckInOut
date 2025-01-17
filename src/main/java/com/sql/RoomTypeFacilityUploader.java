package com.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RoomTypeFacilityUploader {

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuilder sqlBuilder = new StringBuilder();

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 查詢 room_type 資料
        String selectRoomTypes = "SELECT room_type_id FROM room_type ORDER BY room_type_id";

        // 輸出文件路徑
        String outputFilePath = "room_type_facility.sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
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

            // 起始的 INSERT 語句
            sqlBuilder.append("INSERT INTO room_type_facility (room_type_id, facility_id) VALUES ");

            boolean firstRecord = true;

            for (int roomTypeId : roomTypeIds) {
                // 隨機選擇 3 個不重複的 facility_id
                Collections.shuffle(facilityIds, random);
                List<Integer> selectedFacilities = facilityIds.subList(0, 3);

                for (int facilityId : selectedFacilities) {
                    if (!firstRecord) {
                        sqlBuilder.append(", ");
                    }
                    sqlBuilder.append("(").append(roomTypeId).append(", ").append(facilityId).append(")");
                    firstRecord = false;

                    // 每累積一段內容寫入文件
                    if (sqlBuilder.length() > 10000) { // 累積超過 10,000 字符時寫入
                        writer.write(sqlBuilder.toString());
                        sqlBuilder.setLength(0);
                    }
                }
            }

            // 寫入最後剩餘的內容
            if (sqlBuilder.length() > 0) {
                writer.write(sqlBuilder.toString());
            }

            // 添加分號結束語句
            writer.write(";\n");

            System.out.println("SQL 語句已成功寫入文件：" + outputFilePath);

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
