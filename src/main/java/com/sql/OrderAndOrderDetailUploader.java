package com.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderAndOrderDetailUploader {
	private static final int HOTEL_NUM = 3;
	private static final int PERHOTEL = 5;
	private static final int TOTAL_DAY = 60;
	static int totalOrder = HOTEL_NUM * PERHOTEL * TOTAL_DAY;

	// 每筆訂單對應幾筆 order_detail (此處示範 2 筆)
	private static final int DETAILS_PER_ORDER = 2;

	public static void main(String[] args) {
		String outputFilePath = "orderAnddetail.sql";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

			// 1) 建表 orders
			writer.write(generateCreateOrdersTable());
			writer.write("\n");

			// 2) 建表 order_detail
			writer.write(generateCreateOrderDetailTable());
			writer.write("\n");

			// 3) 產生 orders (含 INSERT 語句)
			// 並且存下 (orderId, hotelId) 以便生成對應的 order_detail
			List<OrderInfo> orderInfos = new ArrayList<>();
			String ordersInsertSQL = generateInsertOrdersData(totalOrder, orderInfos);
			System.out.println(ordersInsertSQL);
			System.out.println();
			writer.write(ordersInsertSQL);
			writer.write("\n");

			// 4) 產生 order_detail (含 INSERT 語句)，需要依照 orderInfos 來判斷 hotelId
			String orderDetailInsertSQL = generateInsertOrderDetailData(orderInfos, DETAILS_PER_ORDER);
			System.out.println(orderDetailInsertSQL);
			writer.write(orderDetailInsertSQL);
			System.out.println("SQL 語句已成功寫入文件：" + outputFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 建立 orders 表的語句
	 */
	private static String generateCreateOrdersTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS orders;\n");
		sb.append("CREATE TABLE orders (\n");
		sb.append("    order_id INT AUTO_INCREMENT PRIMARY KEY,\n");
		sb.append("    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,\n");
		sb.append("    status TINYINT NOT NULL COMMENT '0=預約,1=已報到,2=已退房,3=取消',\n");
		sb.append("    check_in_date DATE NOT NULL,\n");
		sb.append("    check_out_date DATE NOT NULL,\n");
		sb.append("    hotel_id INT NOT NULL,\n");
		sb.append("    member_id INT NOT NULL,\n");
		sb.append("    creditcard_id INT NOT NULL,\n");
		sb.append("    member_coupon_id INT DEFAULT NULL,\n");
		sb.append("    total_amount INT NOT NULL,\n");
		sb.append("    guest_last_name VARCHAR(20) NOT NULL,\n");
		sb.append("    guest_first_name VARCHAR(20) NOT NULL,\n");
		sb.append("    memo TEXT DEFAULT NULL,\n");
		sb.append("    rating INT DEFAULT NULL COMMENT '評價星數',\n");
		sb.append("    comment_content TEXT DEFAULT NULL,\n");
		sb.append("    comment_reply TEXT DEFAULT NULL,\n");
		sb.append("    comment_create_time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP\n");
		sb.append("    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id),\n");
		sb.append("    FOREIGN KEY (member_id) REFERENCES member(member_id),\n");
		sb.append("    FOREIGN KEY (creditcard_id) REFERENCES creditcard(creditcard_id),\n");
		sb.append("    FOREIGN KEY (member_coupon_id) REFERENCES member_coupon(member_coupon_id);");
		sb.append(");\n");
		return sb.toString();
	}

	/**
	 * 建立 order_detail 表的語句
	 */
	private static String generateCreateOrderDetailTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS order_detail;\n");
		sb.append("CREATE TABLE order_detail (\n");
		sb.append("    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,\n");
		sb.append("    room_type_id INT NOT NULL,\n");
		sb.append("    order_id INT NOT NULL,\n");
		sb.append("    guest_num INT NOT NULL,\n");
		sb.append("    room_num INT NOT NULL,\n");
		sb.append("    breakfast TINYINT NOT NULL COMMENT '1=含早餐,0=不含',\n");
		sb.append("    FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id),\n");
		sb.append("    FOREIGN KEY (order_id) REFERENCES orders(order_id)\n");
		sb.append(");\n");
		return sb.toString();
	}

	/**
	 * 產生 orders 的 INSERT 語句，並順便把 (orderId, hotelId) 放到 orderInfos list 裡
	 *
	 * @param count      要產生多少筆 orders
	 * @param orderInfos 用來回傳 (orderId, hotelId)，後面生成 order_detail 時要用
	 */
	private static String generateInsertOrdersData(int count, List<OrderInfo> orderInfos) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO orders (\n");
		sb.append("    order_id, create_time, status,\n");
		sb.append("    check_in_date, check_out_date,\n");
		sb.append("    hotel_id,\n");
		sb.append("    member_id, creditcard_id, member_coupon_id,\n");
		sb.append("    total_amount,\n");
		sb.append("    guest_last_name, guest_first_name,\n");
		sb.append("    memo,\n");
		sb.append("    rating, comment_content, comment_reply\n");
		sb.append(") VALUES\n");

		Random rand = new Random();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 用來選擇 hotel_id
		int[] possibleHotels = { 2, 21, 47 };

		// 用來選擇中文姓/名 (示範)
		String[] lastNames = { "陳", "林", "黃", "張", "李", "王", "徐", "曾" };
		String[] firstNames = { "冠宇", "怡君", "俊傑", "雅惠", "志明", "嘉玲", "小明", "淑惠", "宜庭", "杰倫" };

		// 讓日期從今天開始，做 count 筆
		LocalDate baseDate = LocalDate.of(2025, 1, 1);

		for (int i = 1; i <= count; i++) {
			int orderId = i;
			LocalDate checkIn = baseDate.plusDays(i / (HOTEL_NUM * PERHOTEL));
			LocalDate checkOut = checkIn.plusDays(1);

			// status: 0=預約,1=已報到,2=已退房,3=取消
			int status;
			if (checkIn.isBefore(LocalDate.of(2025, 1, 10))) {
				int[] possibleStatus = { 2, 3 };
				status = possibleStatus[rand.nextInt(possibleStatus.length)];

			} else if (checkIn.isAfter(LocalDate.of(2025, 1, 10))) {
				int[] possibleStatus = { 0, 1 };
				status = possibleStatus[rand.nextInt(possibleStatus.length)];
			} else {
				status = 0;
			}

			// 隨機選一個 hotel
			int hotelId = possibleHotels[rand.nextInt(possibleHotels.length)];

			// member_id, creditcard_id => 模擬 1~30
			int memberId = rand.nextInt(30) + 1;
			int creditcardId = memberId; // 簡單示範: 令 creditcard_id=member_id

			// coupon -> 偶爾為 NULL
			String memberCouponId = null;

			// total_amount => 3000~20000 (100倍數)
			int totalAmount = (rand.nextInt((20000 - 3000) / 100 + 1) * 100) + 3000;

			// guest_last_name / guest_first_name
			String lastName = lastNames[rand.nextInt(lastNames.length)];
			String firstName = firstNames[rand.nextInt(firstNames.length)];

			// memo -> 偶爾為 NULL
			String memo = (rand.nextInt(10) < 3) ? null : ("備註" + i);

			// 若 status=2 (已退房) => 給一些 rating/comment
			String rating = "NULL";
			String commentContent = "NULL";
			String commentReply = "NULL";
			if (status == 2) {
				rating = String.valueOf(rand.nextInt(3) + 3); // 3,4,5
				commentContent = "'很棒的住宿'";
				commentReply = "'感謝您的回饋'";
			}

			// 組合SQL
			sb.append("(").append(orderId).append(", ").append("CURRENT_TIMESTAMP, ").append(status).append(", '")
					.append(checkIn.format(fmt)).append("', '").append(checkOut.format(fmt)).append("', ")
					.append(hotelId).append(", ").append(memberId).append(", ").append(creditcardId).append(", ")
					.append(memberCouponId).append(", ").append(totalAmount).append(", '").append(lastName)
					.append("', '").append(firstName).append("', ").append(memo == null ? "NULL" : ("'" + memo + "'"))
					.append(", ").append(rating).append(", ").append(commentContent).append(", ").append(commentReply)
					.append(")");

			if (i < count) {
				sb.append(",\n");
			} else {
				sb.append(";\n");
			}

			// 重要: 把 (orderId, hotelId) 存起來
			orderInfos.add(new OrderInfo(orderId, hotelId));
		}

		return sb.toString();
	}

	/**
	 * 依照 (orderId, hotelId) 清單來產生 order_detail， 並按照 hotel_id 不同，random 出對應區間的
	 * room_type_id: hotel_id=2 => [5..9] hotel_id=21 => [75..77] hotel_id=47 =>
	 * [175..179]
	 *
	 * @param orderInfos      由前面生成 orders 時得到的清單
	 * @param detailsPerOrder 每筆訂單要產生幾筆 order_detail
	 */
	private static String generateInsertOrderDetailData(List<OrderInfo> orderInfos, int detailsPerOrder) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO order_detail (\n");
		sb.append("    order_detail_id,\n");
		sb.append("    room_type_id,\n");
		sb.append("    order_id,\n");
		sb.append("    guest_num,\n");
		sb.append("    room_num,\n");
		sb.append("    breakfast\n");
		sb.append(") VALUES\n");

		Random rand = new Random();

		int detailId = 1; // 自行累加

		for (int i = 0; i < orderInfos.size(); i++) {
			OrderInfo info = orderInfos.get(i);
			int orderId = info.getOrderId();
			int hotelId = info.getHotelId();

			// 依 hotel_id 來決定 room_type_id 的區間
			Range range = getRoomTypeRangeByHotel(hotelId);

			// 每筆訂單產生 detailsPerOrder 筆
			for (int k = 0; k < detailsPerOrder; k++) {
				// 在 range 內隨機取值
				int roomTypeId = range.min + rand.nextInt(range.max - range.min + 1);

				// guest_num => 1~4
				int guestNum = rand.nextInt(4) + 1;

				// room_num => 0或1
				int roomNum = rand.nextInt(2);

				// breakfast => 0或1
				int breakfast = rand.nextInt(2);

				sb.append("(").append(detailId).append(", ").append(roomTypeId).append(", ").append(orderId)
						.append(", ").append(guestNum).append(", ").append(roomNum).append(", ").append(breakfast)
						.append(")");

				// 如果是最後一筆，就收尾；否則加逗號換行
				boolean isLast = (i == orderInfos.size() - 1 && k == detailsPerOrder - 1);
				if (isLast) {
					sb.append(";\n");
				} else {
					sb.append(",\n");
				}

				detailId++;
			}
		}

		return sb.toString();
	}

	/**
	 * 依 hotelId 返回房型的範圍
	 */
	private static Range getRoomTypeRangeByHotel(int hotelId) {
		if (hotelId == 2) {
			return new Range(5, 9);
		} else if (hotelId == 21) {
			return new Range(75, 77);
		} else if (hotelId == 47) {
			return new Range(175, 179);
		} else {
			// 預設可傳回 5..9 或拋例外
			return new Range(5, 9);
		}
	}

	/**
	 * 小幫手: 用來保存 (orderId, hotelId)
	 */
	private static class OrderInfo {
		private final int orderId;
		private final int hotelId;

		public OrderInfo(int orderId, int hotelId) {
			this.orderId = orderId;
			this.hotelId = hotelId;
		}

		public int getOrderId() {
			return orderId;
		}

		public int getHotelId() {
			return hotelId;
		}
	}

	/**
	 * 小幫手: 表示一個 [min, max] 區間
	 */
	private static class Range {
		public int min;
		public int max;

		public Range(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}
}
