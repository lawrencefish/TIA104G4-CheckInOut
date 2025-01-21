package com.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class MemberAvatarWriter {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream maleAvatarStream = null;
        InputStream femaleAvatarStream = null;

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 預設的圖片路徑
        String maleAvatarPath = "src/main/resources/static/pic/bear.jpg";
        String femaleAvatarPath = "src/main/resources/static/pic/rabbit.png";

        String updateSQL = "UPDATE member SET avatar = ? WHERE member_id = ?";

        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(updateSQL);

            // 檢查圖片文件是否存在
            File maleAvatarFile = new File(maleAvatarPath);
            File femaleAvatarFile = new File(femaleAvatarPath);

            if (!maleAvatarFile.exists() || !femaleAvatarFile.exists()) {
                System.out.println("Avatar圖片文件不存在！");
                return;
            }

            maleAvatarStream = new FileInputStream(maleAvatarFile);
            femaleAvatarStream = new FileInputStream(femaleAvatarFile);

            // 查詢所有會員的 ID 和 gender
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT member_id, gender FROM member");

            while (rs.next()) {
                int memberId = rs.getInt("member_id");
                String gender = rs.getString("gender");

                // 根據性別設定對應的圖片流
                if ("M".equalsIgnoreCase(gender)) {
                    pstmt.setBinaryStream(1, new FileInputStream(maleAvatarFile));
                } else if ("F".equalsIgnoreCase(gender)) {
                    pstmt.setBinaryStream(1, new FileInputStream(femaleAvatarFile));
                } else {
                    System.out.println("未識別的性別，Member ID: " + memberId);
                    continue;
                }

                pstmt.setInt(2, memberId);

                // 執行更新
                pstmt.executeUpdate();
                System.out.println("已更新 Member ID: " + memberId);
            }

            rs.close();
            stmt.close();
            pstmt.close();
            System.out.println("會員Avatar更新成功！");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (maleAvatarStream != null) {
                    maleAvatarStream.close();
                }
                if (femaleAvatarStream != null) {
                    femaleAvatarStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
