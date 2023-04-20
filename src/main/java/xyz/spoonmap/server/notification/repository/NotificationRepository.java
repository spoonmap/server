package xyz.spoonmap.server.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.spoonmap.server.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}
