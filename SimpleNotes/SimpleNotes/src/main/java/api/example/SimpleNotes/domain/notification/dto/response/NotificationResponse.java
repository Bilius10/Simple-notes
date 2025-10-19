package api.example.SimpleNotes.domain.notification.dto.response;

import api.example.SimpleNotes.domain.notification.Notification;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String message,
        LocalDateTime createdAt,
        Boolean isRead
) {

    public NotificationResponse(Notification notification) {
        this(notification.getId() ,notification.getMessage(), notification.getCreatedAt(), notification.getIsRead());
    }
}
