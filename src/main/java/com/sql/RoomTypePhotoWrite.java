package com.sql;

import java.sql.*;
import java.io.*;
import java.util.*;

public class RoomTypePhotoWrite {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        InputStream fin = null;
        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String photosDir = "src/main/resources/static/fakeRoomImgs"; // 測試用圖片存放目錄
        String selectRoomTypes = "SELECT room_type_id FROM room_type";
        String insert = "INSERT INTO room_type_img (room_type_id, picture) VALUES (?, ?)";

        try {
            con = DriverManager.getConnection(url, userid, passwd);
            stmt = con.createStatement();
            rs = stmt.executeQuery(selectRoomTypes);

            List<Integer> roomTypeIds = new ArrayList<>();
            while (rs.next()) {
                roomTypeIds.add(rs.getInt("room_type_id"));
            }

            File photosFolder = new File(photosDir);
            if (!photosFolder.exists() || !photosFolder.isDirectory()) {
                System.out.println("圖片目錄不存在：" + photosFolder.getAbsolutePath());
                return;
            }

            File[] photos = photosFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
            if (photos == null || photos.length < 3) {
                System.out.println("圖片數量不足");
                return;
            }

            pstmt = con.prepareStatement(insert);

            Random random = new Random();

            for (int roomTypeId : roomTypeIds) {
                // 每個房型插入 3 張隨機圖片
                Set<Integer> usedIndices = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    int randomIndex;
                    do {
                        randomIndex = random.nextInt(photos.length);
                    } while (usedIndices.contains(randomIndex));
                    usedIndices.add(randomIndex);

                    File photo = photos[randomIndex];
                    fin = new FileInputStream(photo);

                    pstmt.setInt(1, roomTypeId);
                    pstmt.setBinaryStream(2, fin);
                    pstmt.executeUpdate();

                    System.out.println("新增圖片到資料庫，房型 ID：" + roomTypeId + "，檔案：" + photo.getAbsolutePath());

                    fin.close();
                }
            }

            System.out.println("房型圖片新增成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
