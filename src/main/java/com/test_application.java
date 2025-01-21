//package com;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import com.googleAPI.GeocodingService;
//import com.order.model.OrderDTO;
//import com.order.model.OrderService;
//import com.price.model.PriceRepository;
//import com.price.model.PriceService;
//import com.price.model.PriceVO;
//import com.roomInventory.model.RoomInventoryDTO;
//import com.roomInventory.model.RoomInventoryRepository;
//import com.roomInventory.model.RoomInventoryService;
//
//@SpringBootApplication
//public class test_application implements CommandLineRunner {
//
//	@Autowired
//	PriceRepository Prepository;
//	@Autowired
//	PriceService Pservice;
//	@Autowired
//	OrderService orderService;
//
//	@Autowired
//	RoomInventoryRepository RIrepository;
//	@Autowired
//	RoomInventoryService RIservice;
//
//	@Autowired
//	GeocodingService gService;
//
//	@Autowired
//	private SessionFactory sessionFactory;
//
//	public static void main(String[] args) {
//		SpringApplication.run(test_application.class);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		List<OrderDTO> order =  orderService.getOrdersWithDetailsByMemberId(1);
//		for (OrderDTO orders : order) {
//			System.out.println(orders.getEmail());
//		}
//		Double ratings = orderService.getAvgRatingAndCommentCounts(1).orElse(null).getAvgRatings();
//		long comments = orderService.getAvgRatingAndCommentCounts(1).orElse(null).getCommentConuts();
//		System.out.println(ratings+","+comments);
//
//		String tibame = "彰化縣";
//		Double[] latLnt = gService.getCoordinatesFromPlace(tibame);
//		System.out.println(latLnt[0]+","+latLnt[1]);

//		LocalDate startDate = LocalDate.parse("2025-01-09");
//		LocalDate endDate = LocalDate.parse("2025-01-13");
//		System.out.println("test");
//		List<RoomInventoryDTO> RI = RIservice.findAvailableRooms(startDate, endDate, 23, 120, 0.5);
//		System.out.println(RI);
//		RI.forEach(ri ->{
//			PriceVO price = Pservice.getPriceOfDay(ri.getRoomTypeId(),startDate);
//			System.out.println(ri.toString());
//			System.out.println(price.getPriceType());
//			System.out.println(price.getPrice());
//		});
//	}
//}
