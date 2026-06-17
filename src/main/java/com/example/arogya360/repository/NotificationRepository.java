package com.example.arogya360.repository;

import com.example.arogya360.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByIsReadFalseOrderByCreatedAtDesc();
    List<Notification> findTop20ByOrderByCreatedAtDesc();
}
