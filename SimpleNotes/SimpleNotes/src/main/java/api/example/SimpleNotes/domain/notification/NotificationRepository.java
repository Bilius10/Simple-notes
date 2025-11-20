package api.example.SimpleNotes.domain.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Transactional(readOnly = true)
    Page<Notification> findAllByUserId(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    Long countAllByUserIdAndIsReadIsFalse(Long userId);

    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false AND n.id = :notificationId")
    void markAsRead(Long notificationId, Long userId);

    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    void markAllAsRead(Long userId);
}
