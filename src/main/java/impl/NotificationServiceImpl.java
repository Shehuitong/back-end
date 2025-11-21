package com.example.springboot.service.impl;

import com.example.springboot.entity.Activity;
import com.example.springboot.entity.ActivityRegistration;
import com.example.springboot.entity.Notification;
import com.example.springboot.repository.ActivityRegistrationRepository;
import com.example.springboot.repository.ActivityRepository;
import com.example.springboot.repository.NotificationRepository;
import com.example.springboot.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityRegistrationRepository registrationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // 🔧 定时任务：每5分钟检查一次需要发送的提醒
    @Scheduled(fixedRate = 300000) // 5分钟
    @Override
    public void sendActivityReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderThreshold = now.plusMinutes(5); // 未来5分钟内需要提醒的活动

        // 查询需要发送提醒的活动
        List<Activity> activities = activityRepository.findActivitiesNeedingReminder(
                now, reminderThreshold, now.minusMinutes(30)); // 30分钟内未发送过提醒

        for (Activity activity : activities) {
            sendReminderForActivity(activity);
            // 更新最后发送时间
            activity.setLastReminderSent(now);
            activityRepository.save(activity);
        }
    }

    private void sendReminderForActivity(Activity activity) {
        // 获取活动的所有报名用户
        List<ActivityRegistration> registrations = registrationRepository.findByActivityId(activity.getId());

        for (ActivityRegistration registration : registrations) {
            if ("SUCCESS".equals(registration.getStatus())) {
                // 创建提醒通知
                Notification notification = new Notification();
                notification.setUserId(registration.getUserId());
                notification.setActivityId(activity.getId());
                notification.setType("REMINDER");
                notification.setTitle("活动即将开始提醒");
                notification.setContent(String.format(
                        "您报名的活动《%s》将于%s开始，请准时参加！",
                        activity.getName(),
                        formatDateTime(activity.getStartTime())
                ));
                notification.setScheduledTime(LocalDateTime.now());

                notificationRepository.save(notification);

                // 🔧 实际发送逻辑
                sendRealTimeNotification(notification);
            }
        }
    }

    @Override
    public void sendActivityCancellationNotice(Long activityId, String reason) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) return;

        List<ActivityRegistration> registrations = registrationRepository.findByActivityId(activityId);

        for (ActivityRegistration registration : registrations) {
            Notification notification = new Notification();
            notification.setUserId(registration.getUserId());
            notification.setActivityId(activityId);
            notification.setType("CANCELLATION");
            notification.setTitle("活动取消通知");
            notification.setContent(String.format(
                    "很抱歉，您报名的活动《%s》已取消。原因：%s",
                    activity.getName(), reason
            ));

            notificationRepository.save(notification);
            sendRealTimeNotification(notification);
        }
    }

    @Override
    public void sendStatusChangeNotice(Long userId, String oldStatus, String newStatus) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("STATUS_CHANGE");
        notification.setTitle("报名状态更新");
        notification.setContent(String.format(
                "您的报名状态已从【%s】变更为【%s】",
                oldStatus, newStatus
        ));

        notificationRepository.save(notification);
        sendRealTimeNotification(notification);
    }

    // 🔧 实际发送通知的方法
    private void sendRealTimeNotification(Notification notification) {
        // 控制台日志（测试用）
        System.out.println("发送通知给用户 " + notification.getUserId() + ": " + notification.getContent());
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderBySendTimeDesc(userId);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.toString().replace("T", " ");
    }
}