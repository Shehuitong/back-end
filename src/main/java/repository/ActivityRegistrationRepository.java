package com.example.springboot.repository;

import com.example.springboot.entity.ActivityRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Long> {

    // 根据用户ID和活动ID查询报名记录（用于校验重复报名）
    Optional<ActivityRegistration> findByUserIdAndActivityId(Long userId, Long activityId);

    // 根据用户ID查询所有报名记录
    List<ActivityRegistration> findByUserId(Long userId);

    // 根据活动ID查询所有报名记录
    List<ActivityRegistration> findByActivityId(Long activityId);

    // 统计活动的报名人数
    @Query("SELECT COUNT(r) FROM ActivityRegistration r WHERE r.activityId = :activityId AND r.status = 'SUCCESS'")
    Integer countByActivityId(@Param("activityId") Long activityId);

    // 根据状态查询用户的报名记录
    List<ActivityRegistration> findByUserIdAndStatus(Long userId, String status);
}