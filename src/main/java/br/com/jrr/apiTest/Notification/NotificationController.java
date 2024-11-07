package br.com.jrr.apiTest.Notification;

import br.com.jrr.apiTest.Notification.DTO.NotificationListResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping
    public ResponseEntity<NotificationListResponseDTO> getUserNotifications() {
        NotificationListResponseDTO notifications = new NotificationListResponseDTO(notificationService.getCurrentUserNotifications());

        return ResponseEntity.ok(notifications);
    }



}
