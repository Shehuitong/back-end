package com.example.springboot.service.impl;

import com.example.springboot.entity.ActivityRegistration;
import com.example.springboot.repository.ActivityRegistrationRepository;
import com.example.springboot.service.ActivityRegistrationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityRegistrationServiceImpl implements ActivityRegistrationService {

    private final ActivityRegistrationRepository activityRegistrationRepository;

    // 使用构造器注入替代字段注入
    public ActivityRegistrationServiceImpl(ActivityRegistrationRepository activityRegistrationRepository) {
        this.activityRegistrationRepository = activityRegistrationRepository;
    }

    @Override
    public String registerForActivity(Long userId, Long activityId) {
        // 检查是否重复报名
        if (checkDuplicateRegistration(userId, activityId)) {
            return "您已经报名过此活动";
        }

        // 创建报名记录
        ActivityRegistration registration = new ActivityRegistration();
        registration.setUserId(userId);
        registration.setActivityId(activityId);
        registration.setStatus("SUCCESS");

        activityRegistrationRepository.save(registration);
        return "报名成功";
    }

    @Override
    public String cancelRegistration(Long userId, Long activityId) {
        Optional<ActivityRegistration> registrationOpt = activityRegistrationRepository.findByUserIdAndActivityId(userId, activityId);

        if (registrationOpt.isPresent()) {
            ActivityRegistration registration = registrationOpt.get();
            registration.setStatus("CANCELLED");
            activityRegistrationRepository.save(registration);
            return "取消报名成功";
        } else {
            return "未找到报名记录";
        }
    }

    @Override
    public List<ActivityRegistration> getUserRegistrations(Long userId) {
        return activityRegistrationRepository.findByUserId(userId);
    }

    @Override
    public List<ActivityRegistration> getActivityRegistrations(Long activityId) {
        return activityRegistrationRepository.findByActivityId(activityId);
    }

    @Override
    public String updateRegistrationStatus(Long registrationId, String status) {
        Optional<ActivityRegistration> registrationOpt = activityRegistrationRepository.findById(registrationId);

        if (registrationOpt.isPresent()) {
            ActivityRegistration registration = registrationOpt.get();
            registration.setStatus(status);
            activityRegistrationRepository.save(registration);
            return "状态更新成功";
        } else {
            return "报名记录不存在";
        }
    }

    @Override
    public boolean checkDuplicateRegistration(Long userId, Long activityId) {
        Optional<ActivityRegistration> existingOpt = activityRegistrationRepository.findByUserIdAndActivityId(userId, activityId);

        if (existingOpt.isPresent()) {
            ActivityRegistration existing = existingOpt.get();
            return !"CANCELLED".equals(existing.getStatus());
        }
        return false;
    }

    @Override
    public String notifyActivityOffline(Long activityId) {
        List<ActivityRegistration> registrations = activityRegistrationRepository.findByActivityId(activityId);

        // 模拟发送通知
        for (ActivityRegistration reg : registrations) {
            if (!"CANCELLED".equals(reg.getStatus())) {
                System.out.println("发送活动下架通知给用户: " + reg.getUserId());
            }
        }

        return "已向" + registrations.size() + "名用户发送活动下架通知";
    }

    @Override
    public String notifyStatusChange(Long userId, String status) {
        List<ActivityRegistration> userRegistrations = activityRegistrationRepository.findByUserId(userId);

        for (ActivityRegistration reg : userRegistrations) {
            if (!"CANCELLED".equals(reg.getStatus())) {
                System.out.println("用户" + userId + "的活动" + reg.getActivityId() + "状态变更为: " + status);
            }
        }

        return "状态变更通知发送成功";
    }
}