package com.example.springboot.repository;

import com.example.springboot.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // 🔧 新增方法：查询需要发送提醒的活动
    @Query("SELECT a FROM Activity a WHERE a.status = 'ACTIVE' AND a.reminderEnabled = true " +
            "AND a.startTime BETWEEN :startTime AND :endTime " +
            "AND (a.lastReminderSent IS NULL OR a.lastReminderSent < :reminderThreshold)")
    List<Activity> findActivitiesNeedingReminder(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("reminderThreshold") LocalDateTime reminderThreshold);

    // 保持现有方法
    List<Activity> findByStatus(String status);
}