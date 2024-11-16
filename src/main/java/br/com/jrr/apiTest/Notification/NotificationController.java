package br.com.jrr.apiTest.Notification;

import br.com.jrr.apiTest.Notification.DTO.NotificationDTO;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping
    @RolesAllowed("CLIENT")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications() {
        return ResponseEntity.ok(notificationService.getCurrentUserNotifications());
    }
}
