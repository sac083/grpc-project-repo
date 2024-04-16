package com.inspiron.labs.app.repository;

import com.inspiron.labs.app.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Optional<Notification> findByNotificationTypeAndCreatedOn(String notificationType, String createdOn);

    Optional<Notification> findNotificationByNotificationId(String notificationId);
}
