package com.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class HotelLicensePhotoWrite {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;
        InputStream idFrontStream = null;
        InputStream idBackStream = null;
        InputStream licenseStream = null;
        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // 假设图片的路径
        String idFrontDir = "src/main/resources/static/fakeIdLicense/1.jpg";
        String idBackDir = "src/main/resources/static/fakeIdLicense/2.jpg";
        String licenseDir = "src/main/resources/static/fakeIdLicense/3.jpg";

        String updateSQL = "UPDATE hotel SET id_front = ?, id_back = ?, license = ? WHERE hotel_id = ?";

        try {
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(updateSQL);

            // 打开图片文件流
            File idFrontFile = new File(idFrontDir);
            File idBackFile = new File(idBackDir);
            File licenseFile = new File(licenseDir);

            if (!idFrontFile.exists() || !idBackFile.exists() || !licenseFile.exists()) {
                System.out.println("图片文件不存在！");
                return;
            }

            idFrontStream = new FileInputStream(idFrontFile);
            idBackStream = new FileInputStream(idBackFile);
            licenseStream = new FileInputStream(licenseFile);

            // 查询所有的酒店 ID
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT hotel_id FROM hotel");

            while (rs.next()) {
                int hotelId = rs.getInt("hotel_id");

                // 设置图片流参数
                pstmt.setBinaryStream(1, idFrontStream);
                pstmt.setBinaryStream(2, idBackStream);
                pstmt.setBinaryStream(3, licenseStream);
                pstmt.setInt(4, hotelId);

                // 执行更新
                pstmt.executeUpdate();
                System.out.println("已更新 Hotel ID: " + hotelId);

                // 重置文件流以支持下一个更新
                idFrontStream.close();
                idBackStream.close();
                licenseStream.close();
                idFrontStream = new FileInputStream(idFrontFile);
                idBackStream = new FileInputStream(idBackFile);
                licenseStream = new FileInputStream(licenseFile);
            }

            rs.close();
            stmt.close();
            pstmt.close();
            System.out.println("所有酒店图片更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (idFrontStream != null) {
                    idFrontStream.close();
                }
                if (idBackStream != null) {
                    idBackStream.close();
                }
                if (licenseStream != null) {
                    licenseStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
