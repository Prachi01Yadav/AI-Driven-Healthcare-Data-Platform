package com.example.arogya360.service;

import com.example.arogya360.model.Notification;
import com.example.arogya360.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                                SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Notification createNotification(Long userId, String type, String title, String message) {
        Notification notif = new Notification(userId, type, title, message);
        Notification saved = notificationRepository.save(notif);

        // Send via WebSocket
        try {
            messagingTemplate.convertAndSend("/topic/notifications", saved);
        } catch (Exception e) {
            // WebSocket may not be connected - that's fine
        }

        return saved;
    }

    public List<Notification> getRecentNotifications() {
        return notificationRepository.findTop20ByOrderByCreatedAtDesc();
    }

    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notif -> {
            notif.setRead(true);
            notificationRepository.save(notif);
        });
    }
}
