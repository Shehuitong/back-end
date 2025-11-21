package com.example.springboot.service;

import com.example.springboot.entity.ActivityRegistration;
import java.util.List;

public interface ActivityRegistrationService {

    // 报名活动
    String registerForActivity(Long userId, Long activityId);

    // 取消报名
    String cancelRegistration(Long userId, Long activityId);

    // 获取用户的所有报名记录
    List<ActivityRegistration> getUserRegistrations(Long userId);

    // 获取活动的所有报名记录
    List<ActivityRegistration> getActivityRegistrations(Long activityId);

    // 更新报名状态（审核通过/不通过）
    String updateRegistrationStatus(Long registrationId, String status);

    // 🔧 新增方法1：重复报名检查接口
    boolean checkDuplicateRegistration(Long userId, Long activityId);

    // 🔧 新增方法2：活动下架通知推送接口
    String notifyActivityOffline(Long activityId);

    // 🔧 新增方法3：报名状态变更通知接口
    String notifyStatusChange(Long userId, String status);
}