package net.doughughes.testifier.controller;

import net.doughughes.testifier.entity.Notification;
import net.doughughes.testifier.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationRestController {

    @Autowired
    private NotificationService service;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(path = "/notify", method = RequestMethod.POST)
    public String receiveNotification(@RequestBody Notification notification){
        service.saveNotification(notification);

        this.template.convertAndSend("/topic/notifications", notification);

        return "OK";
    }
}
