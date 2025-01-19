package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateRoomInventory {
    public static void run() {
        // JDBC 資料庫連線資訊
        String url = "jdbc:mysql://localhost:3306/checkinout?serverTimezone=Asia/Taipei";
        String userid = "root";
        String passwd = "123456";

        // SQL 查詢
        String selectOrderDetailsSQL = "SELECT order_id, room_type_id, room_num FROM order_detail";
        String selectOrderSQL = "SELECT check_in_date, status FROM orders WHERE order_id = ?";
        String updateRoomInventorySQL = "UPDATE room_inventory " +
                "SET available_quantity = available_quantity - ? " +
                "WHERE room_type_id = ? AND date = ?";

        try (Connection conn = DriverManager.getConnection(url, userid, passwd)) {
            // 1. 查詢所有 order_detail 資料
            try (PreparedStatement psOrderDetails = conn.prepareStatement(selectOrderDetailsSQL);
                 ResultSet rsOrderDetails = psOrderDetails.executeQuery()) {

                while (rsOrderDetails.next()) {
                    int orderId = rsOrderDetails.getInt("order_id");
                    int roomTypeId = rsOrderDetails.getInt("room_type_id");
                    int roomNum = rsOrderDetails.getInt("room_num"); // 獲取房間數量

                    // 2. 根據 order_id 查詢對應的 check_in_date 和 status
                    try (PreparedStatement psOrder = conn.prepareStatement(selectOrderSQL)) {
                        psOrder.setInt(1, orderId);
                        try (ResultSet rsOrder = psOrder.executeQuery()) {
                            if (rsOrder.next()) {
                                String checkInDate = rsOrder.getString("check_in_date");
                                int orderStatus = rsOrder.getInt("status");

                                // 3. 檢查訂單狀態是否為無效 (status = 3)
                                if (orderStatus == 3) {
                                    System.out.println("訂單 ID " + orderId + " 狀態為無效，跳過庫存更新！");
                                    continue;
                                }

                                // 4. 更新 room_inventory 的 available_quantity
                                try (PreparedStatement psUpdateInventory = conn.prepareStatement(updateRoomInventorySQL)) {
                                    psUpdateInventory.setInt(1, roomNum); // 減少的數量
                                    psUpdateInventory.setInt(2, roomTypeId);
                                    psUpdateInventory.setString(3, checkInDate);

                                    int updatedRows = psUpdateInventory.executeUpdate();
                                    if (updatedRows > 0) {
                                        System.out.println("成功更新房型 ID " + roomTypeId +
                                                " 在日期 " + checkInDate + " 的庫存數量！");
                                    } else {
                                        System.out.println("未找到房型 ID " + roomTypeId +
                                                " 在日期 " + checkInDate + " 的庫存資料，無法更新！");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
