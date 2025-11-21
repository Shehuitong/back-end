package com.example.springboot.service.impl;

import com.example.springboot.entity.ActivityRegistration;
import com.example.springboot.service.ActivityRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Primary
public class MockActivityRegistrationServiceImpl implements ActivityRegistrationService {

    // 模拟数据存储
    private List<ActivityRegistration> mockRegistrations = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong(1);

    // 模拟活动数据
    private List<MockActivity> mockActivities = new ArrayList<>();

    // 初始化模拟数据
    public MockActivityRegistrationServiceImpl() {
        // 创建模拟活动
        mockActivities.add(new MockActivity(1L, "校园迎新晚会", 100, 0, "ACTIVE"));
        mockActivities.add(new MockActivity(2L, "篮球比赛", 50, 0, "ACTIVE"));
        mockActivities.add(new MockActivity(3L, "学术讲座", 200, 0, "ACTIVE"));
    }

    @Override
    public String registerForActivity(Long userId, Long activityId) {
        System.out.println("=== 模拟报名调用 ===");
        System.out.println("用户ID: " + userId + ", 活动ID: " + activityId);

        // 1. 检查活动是否存在
        MockActivity activity = findActivityById(activityId);
        if (activity == null) {
            return "活动不存在";
        }

        // 2. 检查活动状态
        if (!"ACTIVE".equals(activity.getStatus())) {
            return "活动已结束或未开始";
        }

        // 3. 检查是否重复报名
        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getUserId().equals(userId) && reg.getActivityId().equals(activityId)) {
                return "您已经报名过此活动";
            }
        }

        // 4. 检查名额是否已满
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return "活动名额已满";
        }

        // 5. 创建报名记录
        ActivityRegistration registration = new ActivityRegistration();
        registration.setId(idCounter.getAndIncrement());
        registration.setUserId(userId);
        registration.setActivityId(activityId);
        registration.setStatus("SUCCESS");
        registration.setRegistrationTime(LocalDateTime.now());

        mockRegistrations.add(registration);

        // 6. 更新活动报名人数
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);

        System.out.println("报名成功！当前报名人数: " + activity.getCurrentParticipants());
        return "报名成功";
    }

    @Override
    public String cancelRegistration(Long userId, Long activityId) {
        System.out.println("=== 模拟取消报名调用 ===");
        System.out.println("用户ID: " + userId + ", 活动ID: " + activityId);

        boolean removed = mockRegistrations.removeIf(reg ->
                reg.getUserId().equals(userId) && reg.getActivityId().equals(activityId));

        if (removed) {
            // 更新活动人数
            MockActivity activity = findActivityById(activityId);
            if (activity != null) {
                activity.setCurrentParticipants(Math.max(0, activity.getCurrentParticipants() - 1));
            }
            return "取消报名成功";
        } else {
            return "未找到报名记录";
        }
    }

    @Override
    public List<ActivityRegistration> getUserRegistrations(Long userId) {
        System.out.println("=== 模拟查询用户报名记录 ===");
        System.out.println("用户ID: " + userId);

        List<ActivityRegistration> result = new ArrayList<>();
        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getUserId().equals(userId)) {
                result.add(reg);
            }
        }

        System.out.println("找到 " + result.size() + " 条报名记录");
        return result;
    }

    @Override
    public List<ActivityRegistration> getActivityRegistrations(Long activityId) {
        List<ActivityRegistration> result = new ArrayList<>();
        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getActivityId().equals(activityId)) {
                result.add(reg);
            }
        }
        return result;
    }

    @Override
    public String updateRegistrationStatus(Long registrationId, String status) {
        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getId().equals(registrationId)) {
                reg.setStatus(status);
                return "状态更新成功";
            }
        }
        return "报名记录不存在";
    }

    // 🔧 新增方法1：重复报名检查接口
    @Override
    public boolean checkDuplicateRegistration(Long userId, Long activityId) {
        System.out.println("=== 模拟重复报名检查 ===");
        System.out.println("用户ID: " + userId + ", 活动ID: " + activityId);

        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getUserId().equals(userId) && reg.getActivityId().equals(activityId)) {
                boolean isDuplicate = !"CANCELLED".equals(reg.getStatus());
                System.out.println("重复报名检查结果: " + isDuplicate);
                return isDuplicate;
            }
        }

        System.out.println("重复报名检查结果: false");
        return false;
    }

    // 🔧 新增方法2：活动下架通知推送接口
    @Override
    public String notifyActivityOffline(Long activityId) {
        System.out.println("=== 模拟活动下架通知 ===");
        System.out.println("活动ID: " + activityId);

        // 查找该活动的所有报名记录
        List<ActivityRegistration> activityRegistrations = new ArrayList<>();
        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getActivityId().equals(activityId)) {
                activityRegistrations.add(reg);
            }
        }

        // 模拟发送通知
        for (ActivityRegistration reg : activityRegistrations) {
            System.out.println("发送活动下架通知给用户: " + reg.getUserId() + ", 活动ID: " + activityId);
        }

        String result = "已向" + activityRegistrations.size() + "名用户发送活动下架通知";
        System.out.println(result);
        return result;
    }

    // 🔧 新增方法3：报名状态变更通知接口
    @Override
    public String notifyStatusChange(Long userId, String status) {
        System.out.println("=== 模拟状态变更通知 ===");
        System.out.println("用户ID: " + userId + ", 新状态: " + status);

        // 查找用户的最新报名记录
        ActivityRegistration userRegistration = null;
        for (ActivityRegistration reg : mockRegistrations) {
            if (reg.getUserId().equals(userId)) {
                userRegistration = reg;
                break;
            }
        }

        if (userRegistration != null) {
            // 更新状态并发送通知
            String oldStatus = userRegistration.getStatus();
            userRegistration.setStatus(status);

            String message = "用户 " + userId + " 的报名状态从 " + oldStatus + " 变更为 " + status;
            System.out.println("发送状态变更通知: " + message);
            return "状态变更通知发送成功: " + message;
        } else {
            return "未找到用户的报名记录";
        }
    }

    private MockActivity findActivityById(Long activityId) {
        for (MockActivity activity : mockActivities) {
            if (activity.getId().equals(activityId)) {
                return activity;
            }
        }
        return null;
    }

    // 内部类用于模拟活动数据
    private static class MockActivity {
        private Long id;
        private String name;
        private Integer maxParticipants;
        private Integer currentParticipants;
        private String status;

        public MockActivity(Long id, String name, Integer maxParticipants, Integer currentParticipants, String status) {
            this.id = id;
            this.name = name;
            this.maxParticipants = maxParticipants;
            this.currentParticipants = currentParticipants;
            this.status = status;
        }

        // getter 和 setter 方法
        public Long getId() { return id; }
        public String getName() { return name; }
        public Integer getMaxParticipants() { return maxParticipants; }
        public Integer getCurrentParticipants() { return currentParticipants; }
        public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
        public String getStatus() { return status; }
    }
}