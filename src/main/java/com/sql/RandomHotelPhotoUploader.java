package com.sql;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomHotelPhotoUploader {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        InputStream fin = null;

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String photos = "src/main/resources/static/fakeHotelImgs"; // 照片資料夾路徑
        String selectHotels = "SELECT hotel_id FROM hotel ORDER BY hotel_id";
        String insert = "INSERT INTO hotel_img (hotel_id, picture) VALUES (?, ?)";

        try {
            con = DriverManager.getConnection(url, userid, passwd);

            // 查詢所有 hotel_id，並按照 hotel_id 順序
            pstmt = con.prepareStatement(selectHotels);
            rs = pstmt.executeQuery();

            List<Integer> hotelIds = new ArrayList<>();
            while (rs.next()) {
                hotelIds.add(rs.getInt("hotel_id"));
            }
            rs.close();
            pstmt.close();

            if (hotelIds.isEmpty()) {
                System.out.println("No hotels found in the database.");
                return;
            }

            // 獲取所有圖片檔案
            File[] photoFiles = new File(photos).listFiles();
            if (photoFiles == null || photoFiles.length < 3) {
                System.out.println("Insufficient photos in folder: " + photos);
                return;
            }

            // 按檔名排序
            List<File> photoList = new ArrayList<>();
            Collections.addAll(photoList, photoFiles);
            photoList.sort((f1, f2) -> {
                int num1 = extractNumber(f1.getName());
                int num2 = extractNumber(f2.getName());
                return Integer.compare(num1, num2);
            });

            // 按 hotel_id 順序插入 3 張圖片
            for (int hotelId : hotelIds) {
                List<File> selectedPhotos = photoList.subList(0, 3); // 每次取前三張

                for (File photo : selectedPhotos) {
                    fin = new FileInputStream(photo);
                    pstmt = con.prepareStatement(insert);
                    pstmt.setInt(1, hotelId);
                    pstmt.setBinaryStream(2, fin);
                    pstmt.executeUpdate();

                    System.out.println("Inserted photo " + photo.getName() + " for hotel_id: " + hotelId);
                    fin.close();
                }

                // 將插入的照片移到最後
                photoList.addAll(photoList.subList(0, 3));
                photoList.subList(0, 3).clear();
            }

            System.out.println("All photos uploaded successfully.");
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
