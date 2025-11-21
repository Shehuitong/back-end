package com.example.springboot.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "activity_id")
    private Long activityId;

    private String type; // REMINDER, CANCELLATION, STATUS_CHANGE
    private String title;
    private String content;
    private Boolean isRead = false;

    @Column(name = "send_time")
    private LocalDateTime sendTime = LocalDateTime.now();

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime; // 计划发送时间

    // 无参构造函数
    public Notification() {
    }

    // 带参构造函数
    public Notification(Long userId, Long activityId, String type, String title, String content) {
        this.userId = userId;
        this.activityId = activityId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.sendTime = LocalDateTime.now();
    }
}