package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Transactional(readOnly = true)
    boolean existsByUserSenderIdAndUserReceiverIdAndStatus(Long senderId, Long receiverId, FriendRequestStatus status);

    @Transactional(readOnly = true)
    Page<FriendRequest> findAllByUserSenderAndStatus(User userSender, FriendRequestStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    Page<FriendRequest> findAllByUserReceiverAndStatus(User userReceiver, FriendRequestStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<FriendRequest> findByIdAndUserSenderId(Long id, Long senderId);

    @Transactional(readOnly = true)
    Optional<FriendRequest> findByIdAndUserReceiverId(Long id, Long receiverId);
}
