package com.airambulance.repository;

import com.airambulance.entity.Notification;
import com.airambulance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientUserOrderByCreatedAtDesc(User user);
    List<Notification> findByRecipientUserAndIsReadOrderByCreatedAtDesc(User user, Boolean isRead);
    List<Notification> findAllByOrderByCreatedAtDesc();
}
