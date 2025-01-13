package com.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomUploader {

    public static void main(String[] args) {
        StringBuilder sqlBuilder = new StringBuilder();

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 查詢 room_type 表
        String selectRoomTypeQuery = "SELECT room_type_id, room_num FROM room_type ORDER BY room_type_id";

        // 輸出文件的路徑
        String outputFilePath = "room.sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            Connection con = DriverManager.getConnection(url, userid, passwd);

            PreparedStatement pstmt = con.prepareStatement(selectRoomTypeQuery);
            ResultSet rs = pstmt.executeQuery();

            // 起始的 INSERT 語句
            sqlBuilder.append("INSERT INTO room (room_type_id, number, status) VALUES ");

            boolean firstRecord = true;

            while (rs.next()) {
                int roomTypeId = rs.getInt("room_type_id");
                int roomNum = rs.getInt("room_num");

                for (int i = 1; i <= roomNum; i++) {
                    if (!firstRecord) {
                        sqlBuilder.append(", ");
                    }
                    sqlBuilder.append("(")
                            .append(roomTypeId).append(", ")
                            .append(i).append(", ")
                            .append(0).append(")"); // 將 status 改為 0
                    firstRecord = false;

                    // 每累積一段內容寫入文件，避免內存占用過高
                    if (sqlBuilder.length() > 10000) { // 累積超過 10,000 字符後寫入
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

            rs.close();
            pstmt.close();
            con.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
