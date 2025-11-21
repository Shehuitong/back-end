package com.example.springboot.controller;

import com.example.springboot.common.ApiResult;
import com.example.springboot.entity.Notification;
import com.example.springboot.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 获取用户通知列表
    @GetMapping("/my")
    public ApiResult<List<Notification>> getMyNotifications(@RequestHeader Long userId) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return ApiResult.ok(notifications);
        } catch (Exception e) {
            return ApiResult.fail("获取通知失败: " + e.getMessage());
        }
    }

    // 手动触发活动取消通知（管理员用）
    @PostMapping("/activity/{activityId}/cancel")
    public ApiResult<String> cancelActivityNotification(@PathVariable Long activityId,
                                                        @RequestParam String reason) {
        try {
            notificationService.sendActivityCancellationNotice(activityId, reason);
            return ApiResult.ok("活动取消通知发送成功");
        } catch (Exception e) {
            return ApiResult.fail("发送失败: " + e.getMessage());
        }
    }
}