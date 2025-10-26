package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsByUserSenderAndUserReceiverAndStatus(User userSender, User userReceiver, FriendRequestStatus status);
}
