package api.example.SimpleNotes.domain.notification;

import api.example.SimpleNotes.domain.notification.dto.response.NotificationResponse;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public PageDTO<NotificationResponse> findAll(Pageable pageable, Long userId) {
        Page<Notification> notifications = repository.findAllByUserId(userId, pageable);

        Page<NotificationResponse> record = notifications.map(NotificationResponse::new);

        return new PageDTO<>(record);
    }

    public Long countUnreadNotifications(Long userId) {
        return repository.countAllByUserIdAndIsReadIsFalse(userId);
    }

    @Transactional
    public void save(User user, String message) {
        Notification newNotification = new Notification(user, message);
        repository.save(newNotification);
    }

    public void markAsRead(Long notificationId, Long userId) {
        repository.markAsRead(notificationId, userId);
    }

    public void markAllAsRead(Long userId) {
        repository.markAllAsRead(userId);
    }
}
