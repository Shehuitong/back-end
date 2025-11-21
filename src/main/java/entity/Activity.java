package com.example.springboot.entity; // 确保包名正确

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity") // 确保表名与数据库一致
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键ID

    private String name; // 活动名称
    private String description; // 活动描述

    @Column(name = "start_time")
    private LocalDateTime startTime; // 开始时间

    @Column(name = "end_time")
    private LocalDateTime endTime; // 结束时间

    @Column(name = "max_participants")
    private Integer maxParticipants; // 最大参与人数

    @Column(name = "current_participants")
    private Integer currentParticipants = 0; // 当前报名人数

    private String status = "ACTIVE"; // 活动状态

    // 🔧 新增字段：提醒功能相关
    @Column(name = "reminder_enabled")
    private Boolean reminderEnabled = true; // 是否启用提醒

    @Column(name = "reminder_time_before")
    private Integer reminderTimeBefore = 60; // 提前多少分钟提醒

    @Column(name = "last_reminder_sent")
    private LocalDateTime lastReminderSent; // 最后发送提醒时间

    // 必须有无参构造函数
    public Activity() {
    }

    // 带参构造函数
    public Activity(String name, String description, LocalDateTime startTime,
                    LocalDateTime endTime, Integer maxParticipants) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxParticipants = maxParticipants;
    }
}