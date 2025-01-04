package com.ScheduledTask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

    // 每天 0 点执行一次任务
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨 0 点
    public void executeDailyTask() {
        System.out.println("好棒喔!!  12點了!!");
        // 执行每日任务逻辑
    }

    // 每小时执行一次任务
    @Scheduled(cron = "0 0 * * * ?") // 每小时的整点
    public void executeHourlyTask() {
        System.out.println("好棒喔!!  現在是整點了!!");
        // 执行每小时任务逻辑
    }

    // 每10分钟执行一次任务
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟
    public void executePeriodicTask() {
        System.out.println("好棒喔!! 又過了10分鐘了!!");
        // 执行每10分钟任务逻辑
    }
}
