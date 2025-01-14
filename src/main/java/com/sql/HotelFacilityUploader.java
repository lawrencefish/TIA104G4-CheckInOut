package com.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HotelFacilityUploader {

    public static void main(String[] args) {
        StringBuilder sqlBuilder = new StringBuilder();

        // 輸出文件路徑
        String outputFilePath = "hotel_facility.sql";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // 生成 hotel_id 和 facility_id 範圍
            List<Integer> hotelIds = new ArrayList<>();
            for (int i = 1; i <= 140; i++) {
                hotelIds.add(i);
            }

            List<Integer> facilityIds = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                facilityIds.add(i);
            }

            Random random = new Random();

            // 起始的 INSERT 語句
            sqlBuilder.append("INSERT INTO hotel_facility (hotel_id, facility_id) VALUES ");

            boolean firstRecord = true;

            for (int hotelId : hotelIds) {
                // 隨機選擇 3 個設施 ID
                Collections.shuffle(facilityIds, random);
                List<Integer> selectedFacilities = facilityIds.subList(0, 3);

                for (int facilityId : selectedFacilities) {
                    if (!firstRecord) {
                        sqlBuilder.append(", ");
                    }
                    sqlBuilder.append("(").append(hotelId).append(", ").append(facilityId).append(")");
                    firstRecord = false;

                    // 定期將內容寫入文件，避免內存占用過高
                    if (sqlBuilder.length() > 10000) { // 每累積 10,000 字符寫入一次
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
