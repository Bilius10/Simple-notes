package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsByUserSenderAndUserReceiverAndStatus(User userSender, User userReceiver, FriendRequestStatus status);

    Page<FriendRequest> findAllByUserSenderAndStatus(User userSender, FriendRequestStatus status, Pageable pageable);

    Page<FriendRequest> findAllByUserReceiverAndStatus(User userReceiver, FriendRequestStatus status, Pageable pageable);
}
