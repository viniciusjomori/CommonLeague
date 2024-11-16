package br.com.jrr.apiTest.Notification.DTO;

import br.com.jrr.apiTest.Notification.NotificationType;

import java.util.UUID;

public record NotificationDTO(
        NotificationType type,
        UUID relatedId,
        String idType
) {}
