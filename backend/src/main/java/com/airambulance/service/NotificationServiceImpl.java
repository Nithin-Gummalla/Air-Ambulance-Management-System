package com.airambulance.service;

import com.airambulance.dto.NotificationResponse;
import com.airambulance.entity.Notification;
import com.airambulance.entity.User;
import com.airambulance.enums.NotificationType;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.NotificationRepository;
import com.airambulance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NotificationResponse createNotification(String title, String message, NotificationType type, Long recipientUserId) {
        User recipient = null;
        if (recipientUserId != null) {
            recipient = userRepository.findById(recipientUserId)
                    .orElse(null);
        }

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .type(type != null ? type : NotificationType.SYSTEM_INFO)
                .recipientUser(recipient)
                .isRead(false)
                .build();

        return mapToResponse(notificationRepository.save(notification));
    }

    @Override
    public List<NotificationResponse> getNotificationsForUser(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return notificationRepository.findByRecipientUserOrderByCreatedAtDesc(userOpt.get()).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return notificationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsReadForUser(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            List<Notification> list = notificationRepository.findByRecipientUserAndIsReadOrderByCreatedAtDesc(userOpt.get(), false);
            list.forEach(n -> n.setIsRead(true));
            notificationRepository.saveAll(list);
        }
    }

    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
