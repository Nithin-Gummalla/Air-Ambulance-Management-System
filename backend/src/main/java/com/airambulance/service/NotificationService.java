package com.airambulance.service;

import com.airambulance.dto.NotificationResponse;
import com.airambulance.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    NotificationResponse createNotification(String title, String message, NotificationType type, Long recipientUserId);
    List<NotificationResponse> getNotificationsForUser(String email);
    List<NotificationResponse> getAllNotifications();
    void markAsRead(Long notificationId);
    void markAllAsReadForUser(String email);
}
