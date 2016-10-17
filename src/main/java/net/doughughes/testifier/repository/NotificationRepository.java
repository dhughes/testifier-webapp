package net.doughughes.testifier.repository;


import net.doughughes.testifier.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
