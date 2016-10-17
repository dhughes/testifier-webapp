package net.doughughes.testifier.service;

import net.doughughes.testifier.entity.Notification;
import net.doughughes.testifier.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public void saveNotification(Notification notification){
        repository.save(notification);
    }
}
