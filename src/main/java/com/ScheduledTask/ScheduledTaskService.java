package com.ScheduledTask;

import com.roomInventory.model.RoomInventoryService;
import com.roomInventory.model.RoomInventoryVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduledTaskService {

    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private RoomInventoryService roomInventoryService;

    // 每天 0 点执行一次任务
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨 0 点
    public void executeDailyTask() {
        System.out.println("好棒喔!!  半夜12點了!!");

        updateRoomInventory();
    }

    // 每小时执行一次任务
    @Scheduled(cron = "0 0 * * * ?") // 每小时的整点
    public void executeHourlyTask() {
        System.out.println("好棒喔!!  現在是整點了!!");
    }

    // 每10分钟执行一次任务
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟
    public void executePeriodicTask() {
        System.out.println("好棒喔!!  又過10分鐘了!!");
    }

    public void updateRoomInventory() {
        System.out.println("開始執行每日庫存新增任務!!");
        // 1. 獲取所有房型
        List<RoomTypeVO> roomTypes = roomTypeService.findAll();
        if (roomTypes.isEmpty()) {
            System.out.println("沒有可用的房型，任務結束！");
            return;
        }
        System.out.println("步驟1完成");
        // 2. 確定日期範圍（今天到 2 個月後）
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(2);
        System.out.println("步驟2完成");
        // 3. 查詢現有的庫存日期
        List<RoomInventoryVO> existingInventories = roomInventoryService.findByDateRange(today, endDate);
        System.out.println("步驟3完成");
        // 4. 將現有庫存按日期和房型分組
        Set<String> existingKeys = existingInventories.stream()
                .map(inventory -> inventory.getDate() + "-" + inventory.getRoomType().getRoomTypeId())
                .collect(Collectors.toSet());
        System.out.println("步驟4完成");
        // 5. 新增缺少的庫存資料
        for (RoomTypeVO roomType : roomTypes) {
            for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
                String key = date + "-" + roomType.getRoomTypeId();
                if (!existingKeys.contains(key)) {
                    // 如果庫存不存在，新增資料
                    RoomInventoryVO newInventory = new RoomInventoryVO();
                    newInventory.setRoomType(roomType);
                    newInventory.setDate(date);
                    newInventory.setAvailableQuantity(roomType.getRoomNum()); // 使用 roomNum 作為默認庫存數量
                    roomInventoryService.save(newInventory);

                    System.out.println("新增庫存: 日期=" + date + ", 房型ID=" + roomType.getRoomTypeId() + ", 房型=" + roomType.getRoomName() + ", 庫存數量=" + roomType.getRoomNum());
                }
            }
        }
        System.out.println("每日庫存新增任務完成！");
    }
}
