package com.example.springboot.controller;

import com.example.springboot.common.ApiResult;
import com.example.springboot.entity.ActivityRegistration;
import com.example.springboot.service.ActivityRegistrationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class ActivityRegistrationController {

    private final ActivityRegistrationService registrationService;

    // 使用构造器注入替代字段注入
    @Autowired
    public ActivityRegistrationController(ActivityRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // 报名活动接口
    @PostMapping("/register")
    public ApiResult<String> register(@RequestParam Long activityId, @RequestHeader Long userId) {
        try {
            String result = registrationService.registerForActivity(userId, activityId);
            if ("报名成功".equals(result)) {
                return ApiResult.ok(result);
            } else {
                return ApiResult.fail(result);
            }
        } catch (Exception e) {
            return ApiResult.fail("系统错误: " + e.getMessage());
        }
    }

    // 取消报名接口
    @PostMapping("/cancel")
    public ApiResult<String> cancelRegistration(@RequestParam Long activityId, @RequestHeader Long userId) {
        try {
            String result = registrationService.cancelRegistration(userId, activityId);
            if ("取消报名成功".equals(result)) {
                return ApiResult.ok(result);
            } else {
                return ApiResult.fail(result);
            }
        } catch (Exception e) {
            return ApiResult.fail("取消报名失败: " + e.getMessage());
        }
    }

    // 查询用户报名记录
    @GetMapping("/my")
    public ApiResult<List<ActivityRegistration>> getMyRegistrations(@RequestHeader Long userId) {
        try {
            List<ActivityRegistration> registrations = registrationService.getUserRegistrations(userId);
            return ApiResult.ok(registrations);
        } catch (Exception e) {
            return ApiResult.fail("查询失败: " + e.getMessage());
        }
    }

    // 查询活动报名列表
    @GetMapping("/activity/{activityId}")
    public ApiResult<List<ActivityRegistration>> getActivityRegistrations(@PathVariable Long activityId) {
        try {
            List<ActivityRegistration> registrations = registrationService.getActivityRegistrations(activityId);
            return ApiResult.ok(registrations);
        } catch (Exception e) {
            return ApiResult.fail("查询失败: " + e.getMessage());
        }
    }

    // 重复报名检查接口
    @PostMapping("/check-duplicate")
    public ApiResult<Boolean> checkDuplicateRegistration(@RequestBody DuplicateCheckRequest request) {
        try {
            boolean isDuplicate = registrationService.checkDuplicateRegistration(
                    Long.parseLong(request.getUserId()),
                    Long.parseLong(request.getActivityId())
            );
            return ApiResult.ok(isDuplicate);
        } catch (Exception e) {
            return ApiResult.fail("检查失败: " + e.getMessage());
        }
    }

    // 活动下架通知接口
    @PostMapping("/activity/offline")
    public ApiResult<String> notifyActivityOffline(@RequestBody ActivityOfflineRequest request) {
        try {
            String result = registrationService.notifyActivityOffline(
                    Long.parseLong(request.getActivityId())
            );
            return ApiResult.ok(result);
        } catch (Exception e) {
            return ApiResult.fail("通知发送失败: " + e.getMessage());
        }
    }

    // 状态变更通知接口
    @PostMapping("/status/notify")
    public ApiResult<String> notifyStatusChange(@RequestBody StatusChangeRequest request) {
        try {
            String result = registrationService.notifyStatusChange(
                    Long.parseLong(request.getUserId()),
                    request.getStatus()
            );
            return ApiResult.ok(result);
        } catch (Exception e) {
            return ApiResult.fail("状态通知失败: " + e.getMessage());
        }
    }

    // DTO类
    @Getter
    @Setter
    public static class DuplicateCheckRequest {
        private String userId;
        private String activityId;
    }

    @Getter
    @Setter
    public static class ActivityOfflineRequest {
        private String activityId;
    }

    @Getter
    @Setter
    public static class StatusChangeRequest {
        private String userId;
        private String status;
    }
}