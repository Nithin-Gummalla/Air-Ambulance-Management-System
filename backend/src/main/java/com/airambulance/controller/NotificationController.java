package com.airambulance.controller;

import com.airambulance.dto.MessageResponse;
import com.airambulance.dto.NotificationResponse;
import com.airambulance.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Module", description = "Endpoints for real-time user notifications and system alerts")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get User Notifications", description = "Fetches notification alerts for the currently logged in user")
    public ResponseEntity<List<NotificationResponse>> getNotifications(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        if (email != null) {
            return ResponseEntity.ok(notificationService.getNotificationsForUser(email));
        }
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark Notification as Read", description = "Marks a single notification as read")
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(new MessageResponse("Notification marked as read"));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark All Notifications Read", description = "Marks all notifications for current user as read")
    public ResponseEntity<MessageResponse> markAllAsRead(Authentication authentication) {
        if (authentication != null) {
            notificationService.markAllAsReadForUser(authentication.getName());
        }
        return ResponseEntity.ok(new MessageResponse("All notifications marked as read"));
    }
}
