package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HotelFacilityUploader {

    public static void run() {
        Connection con = null;
        PreparedStatement pstmt = null;

        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";
        String insert = "INSERT INTO hotel_facility (hotel_id, facility_id) VALUES (?, ?)";

        try {
            con = DriverManager.getConnection(url, userid, passwd);

            // Generate hotel_id range and facility_id range
            List<Integer> hotelIds = new ArrayList<>();
            for (int i = 1; i <= 140; i++) {
                hotelIds.add(i);
            }

            List<Integer> facilityIds = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                facilityIds.add(i);
            }

            Random random = new Random();

            for (int hotelId : hotelIds) {
                // Randomly select 3 unique facility_ids for each hotel_id
                Collections.shuffle(facilityIds, random);
                List<Integer> selectedFacilities = facilityIds.subList(0, 3);

                for (int facilityId : selectedFacilities) {
                    pstmt = con.prepareStatement(insert);
                    pstmt.setInt(1, hotelId);
                    pstmt.setInt(2, facilityId);
                    pstmt.executeUpdate();

                    System.out.println("Inserted hotel_id: " + hotelId + ", facility_id: " + facilityId);
                }
            }

            System.out.println("All facilities uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
