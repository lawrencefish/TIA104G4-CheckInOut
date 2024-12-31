package com.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;

public class HotelPhotoWriter {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String photos = "src/main/resources/static/fakeHotelImgs"; // 照片資料夾路徑
        String insert = "INSERT INTO hotel_img (hotel_id, picture) VALUES (?, ?)";

        int hotelId = 1; // 起始的 hotel_id
        int maxHotelId = 11; // 假資料中的最大 hotel_id
        int imageCount = 0; // 計算每個 hotel 的圖片數量

        try {
            con = DriverManager.getConnection(url, userid, passwd);
            File[] photoFiles = new File(photos).listFiles();

            if (photoFiles == null || photoFiles.length == 0) {
                System.out.println("No photos found in folder: " + photos);
                return;
            }

            // 按檔名中的數字進行排序
            Arrays.sort(photoFiles, (file1, file2) -> {
                int num1 = extractNumber(file1.getName());
                int num2 = extractNumber(file2.getName());
                return Integer.compare(num1, num2);
            });

            for (File f : photoFiles) {
                if (hotelId > maxHotelId) {
                    System.out.println("No more valid hotel IDs to process. Stopping.");
                    break; // 停止處理超出範圍的 hotel_id
                }

                fin = new FileInputStream(f);
                pstmt = con.prepareStatement(insert);
                pstmt.setInt(1, hotelId);
                pstmt.setBinaryStream(2, fin);
                pstmt.executeUpdate();

                imageCount++;
                if (imageCount == 3) { // 每個 hotel 限定 3 張圖片
                    hotelId++;
                    imageCount = 0;
                }

                System.out.print("Inserted into database: ");
                System.out.println(f.toString());
                fin.close();
            }

            pstmt.close();
            System.out.println("圖片插入成功.........");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 從檔名中提取數字
    private static int extractNumber(String fileName) {
        try {
            String number = fileName.replaceAll("\\D", ""); // 移除非數字部分
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0; // 如果解析失敗，默認返回 0
        }
    }
}
