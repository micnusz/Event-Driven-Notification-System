package com.micnusz.edns.notification.repository;

import com.micnusz.edns.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
}
