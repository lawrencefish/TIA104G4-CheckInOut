package com.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RoomInventoryUploader {

    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuilder sqlBuilder = new StringBuilder();

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 查詢房型資料
        String selectRoomTypes = "SELECT room_type_id, room_num FROM room_type ORDER BY room_type_id";

        // 輸出文件的路徑
        String outputFilePath = "room_inventory.sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            con = DriverManager.getConnection(url, userid, passwd);

            // 查詢所有房型
            pstmt = con.prepareStatement(selectRoomTypes);
            rs = pstmt.executeQuery();

            // 起始的 INSERT 語句
            sqlBuilder.append("INSERT INTO room_inventory (room_type_id, date, available_quantity) VALUES ");

            // 日期範圍
            LocalDate startDate = LocalDate.of(2024, 12, 1);
            LocalDate endDate = LocalDate.of(2025, 3, 31);

            boolean firstRecord = true;

            while (rs.next()) {
                int roomTypeId = rs.getInt("room_type_id");
                int roomNum = rs.getInt("room_num");

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (!firstRecord) {
                        sqlBuilder.append(", ");
                    }
                    sqlBuilder.append("(")
                            .append(roomTypeId).append(", '")
                            .append(date).append("', ")
                            .append(roomNum).append(")");
                    firstRecord = false;

                    // 定期將內容寫入文件，避免占用過多內存
                    if (sqlBuilder.length() > 10000) { // 每累積 10,000 字符寫入一次
                        writer.write(sqlBuilder.toString());
                        sqlBuilder.setLength(0);
                    }
                }
            }

            // 最後寫入剩餘的內容
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