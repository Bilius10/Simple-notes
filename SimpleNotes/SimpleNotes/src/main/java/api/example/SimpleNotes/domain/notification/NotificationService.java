package api.example.SimpleNotes.domain.notification;

import api.example.SimpleNotes.domain.notification.dto.response.NotificationResponse;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import api.example.SimpleNotes.infrastructure.exception.ExceptionMessages;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    @Transactional(readOnly = true)
    public PageDTO<NotificationResponse> findAll(Pageable pageable, Long userId) {
        Page<Notification> notifications = repository.findAllByUserId(userId, pageable);

        Page<NotificationResponse> dtosPage = notifications.map(NotificationResponse::new);

        return new PageDTO<>(
                dtosPage.getContent(),
                notifications.getNumber(),
                notifications.getSize(),
                notifications.getTotalElements(),
                notifications.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Long countUnreadNotifications(Long userId) {
        return repository.countAllByUserIdAndIsReadIsFalse(userId);
    }

    @Transactional
    public void save(User user, String message) {
        Notification newNotification = new Notification(user, message);
        repository.save(newNotification);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = repository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new ServiceException(ExceptionMessages.NOTIFICATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        notification.setIsRead(true);
        repository.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = repository.findAllByUserId(userId);

        notifications.forEach(notification -> notification.setIsRead(true));

        repository.saveAll(notifications);
    }
}
