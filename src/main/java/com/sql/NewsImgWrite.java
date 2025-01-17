package com.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class NewsImgWrite {
	    public static void run() {
	    	
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        InputStream fin = null;
	        
	        String url = "jdbc:mysql://localhost:3306/your_database?serverTimezone=Asia/Taipei";
	        String userid = "root";
	        String passwd = "password";
	        String photosDir = "src/main/resources/static/newsImages"; // 圖片存放目錄
	        String insert = "INSERT INTO news (news_title, description, news_img, post_time) VALUES (?, ?, ?, ?)";

	        try {
	            con = DriverManager.getConnection(url, userid, passwd);
	            pstmt = con.prepareStatement(insert);

	            File photosFolder = new File(photosDir);
	            if (!photosFolder.exists() || !photosFolder.isDirectory()) {
	                System.out.println("圖片目錄不存在：" + photosFolder.getAbsolutePath());
	                return;
	            }

	            // 假設只處理 jpg 檔案
	            File[] photos = photosFolder.listFiles((dir, name) -> 
	                name.toLowerCase().endsWith(".jpg"));
	                
	            if (photos == null || photos.length == 0) {
	                System.out.println("沒有找到圖片");
	                return;
	            }

	            for (File photo : photos) {
	                fin = new FileInputStream(photo);

	                // 設置參數
	                pstmt.setString(1, "新聞標題 " + photo.getName()); // news_title
	                pstmt.setString(2, "新聞描述 " + photo.getName()); // description
	                pstmt.setBinaryStream(3, fin); // news_img
	                pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // post_time

	                pstmt.executeUpdate();

	                System.out.println("新增新聞及圖片到資料庫，檔案：" + photo.getAbsolutePath());
	                
	                fin.close();
	            }

	            System.out.println("新聞圖片新增成功！");

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
	                if (con != null) {
	                    con.close();
	                }
	            } catch (SQLException | IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
