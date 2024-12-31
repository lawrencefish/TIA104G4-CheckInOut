package com.sql;


import java.sql.*;
import java.io.*;

public class RoomTypePhotoWrite {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String photosDir = "src/main/resources/static/fakeRoomImgs"; // 測試用圖片存放目錄
        String insert = "INSERT INTO room_type_img (room_type_id, picture) VALUES (?, ?)";

        int roomTypeId = 1; // 假設房型從 1 開始，對應圖片名稱
        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(insert);

            for (int i = 1; i <= 33; i++) {
                File file = new File(photosDir, i + ".jpg"); // 假設檔名為 1.jpg 至 33.jpg

                if (!file.exists()) {
                    System.out.println("找不到圖片檔案：" + file.getAbsolutePath());
                    continue;
                }

                fin = new FileInputStream(file);

                // 設置 SQL 參數
                pstmt.setInt(1, roomTypeId); // 將圖片對應的房型 ID
                pstmt.setBinaryStream(2, fin);

                // 執行 INSERT
                pstmt.executeUpdate();

                System.out.println("新增圖片到資料庫，房型 ID：" + roomTypeId + "，檔案：" + file.getAbsolutePath());

                // 關閉當前圖片流
                fin.close();

                // 更新房型 ID
                roomTypeId++;
                if (roomTypeId > 33) {
                    break; // 確保不超過 33 個房型
                }
            }

            pstmt.close();
            System.out.println("圖片新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}