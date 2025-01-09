package com.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PriceUploader {

    public static void main(String[] args) {
        StringBuilder sqlBuilder = new StringBuilder();

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 查詢 room_type 表
        String selectRoomTypes = "SELECT room_type_id FROM room_type ORDER BY room_type_id";

        // 輸出文件的路徑
        String outputFilePath = "price.sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            Connection con = DriverManager.getConnection(url, userid, passwd);

            PreparedStatement pstmt = con.prepareStatement(selectRoomTypes);
            ResultSet rs = pstmt.executeQuery();

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

            // 起始的 INSERT 語句
            sqlBuilder.append("INSERT INTO price (room_type_id, start_date, end_date, price_type, breakfast_price, price) VALUES ");

            boolean firstRecord = true;

            for (int roomTypeId : roomTypeIds) {
                // 為每個 room_type_id 插入三筆資料
                for (int priceType = 1; priceType <= 3; priceType++) {
                    // 隨機生成價格
                    int breakfastPrice = random.nextInt(500) + 50; // 早餐價格隨機生成 50~550
                    int price = random.nextInt(2000) + 500; // 房間價格隨機生成 500~2500

                    // 日期處理
                    String startDate = "NULL";
                    String endDate = "NULL";
                    if (priceType == 3) {
                        // 生成有效的日期範圍
                        long startMillis = System.currentTimeMillis() + random.nextInt(30) * 24 * 60 * 60 * 1000L;
                        long endMillis = startMillis + random.nextInt(10) * 24 * 60 * 60 * 1000L;
                        startDate = "'" + new java.sql.Date(startMillis).toString() + "'";
                        endDate = "'" + new java.sql.Date(endMillis).toString() + "'";
                    }

                    if (!firstRecord) {
                        sqlBuilder.append(", ");
                    }
                    sqlBuilder.append("(")
                            .append(roomTypeId).append(", ")
                            .append(startDate).append(", ")
                            .append(endDate).append(", ")
                            .append(priceType).append(", ")
                            .append(breakfastPrice).append(", ")
                            .append(price).append(")");
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

            con.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
