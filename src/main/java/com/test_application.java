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
//import com.price.model.PriceRepository;
//import com.price.model.PriceService;
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
//
//	@Autowired
//	RoomInventoryRepository RIrepository;
//	@Autowired
//	RoomInventoryService RIservice;
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
//		LocalDate startDate = LocalDate.parse("2025-01-09");
//		LocalDate endDate = LocalDate.parse("2025-01-13");
//		
//		List<RoomInventoryDTO> RI = RIservice.findRoomInventoryByTypeHotelAndPrice(startDate,endDate,25.1,121.1);
//		RI.forEach(ri ->{
//			System.out.println(ri.toString());
//		});
//	}
//}
