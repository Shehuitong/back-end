package com.example.springboot.entity;

import lombok.Data;
import jakarta.persistence.*; // 修改这里

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    private String name;
    private String phone;
    private String email;

    public User() {}
}