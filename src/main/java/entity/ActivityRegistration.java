package com.example.springboot.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_registration")
public class ActivityRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "activity_id")
    private Long activityId;

    private String status = "PENDING"; // 设置默认值

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    // 无参构造函数（JPA要求）
    public ActivityRegistration() {
    }

    // 带参构造函数
    public ActivityRegistration(Long userId, Long activityId, String status) {
        this.userId = userId;
        this.activityId = activityId;
        this.status = status;
        this.registrationTime = LocalDateTime.now();
    }

    // 全参构造函数（可选）
    public ActivityRegistration(Long userId, Long activityId, String status, LocalDateTime registrationTime) {
        this.userId = userId;
        this.activityId = activityId;
        this.status = status;
        this.registrationTime = registrationTime;
    }
}