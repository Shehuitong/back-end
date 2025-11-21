package com.example.springboot.service;

import com.example.springboot.entity.Notification;
import java.util.List;

public interface NotificationService {

    // 发送活动开始提醒
    void sendActivityReminders();

    // 发送活动取消通知
    void sendActivityCancellationNotice(Long activityId, String reason);

    // 发送状态变更通知
    void sendStatusChangeNotice(Long userId, String oldStatus, String newStatus);

    // 获取用户通知
    List<Notification> getUserNotifications(Long userId);
}