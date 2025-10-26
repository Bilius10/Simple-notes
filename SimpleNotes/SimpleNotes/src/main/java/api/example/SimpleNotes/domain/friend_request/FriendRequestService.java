package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestResponse;
import api.example.SimpleNotes.domain.notification.NotificationService;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.infrastructure.exception.ExceptionMessages;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository repository;
    private final NotificationService notificationService;
    private final UserService userService;

    public FriendRequestResponse create(Long senderId, Long receiverId) {
        validateIfSenderAndReceiverIsEquals(senderId, receiverId);

        User userSender = userService.findById(senderId);
        User userReceiver = userService.findById(receiverId);

        validateIfExistsPendingFriendRequest(userSender, userReceiver);

        FriendRequest friendRequest = new FriendRequest(userSender, userReceiver);

        FriendRequest savedFriendRequest = repository.save(friendRequest);

        return new FriendRequestResponse(savedFriendRequest);
    }

    private void validateIfExistsPendingFriendRequest(User sender, User receiver){
        if(repository.existsByUserSenderAndUserReceiverAndStatus(sender, receiver, FriendRequestStatus.WAITING)){
            throw new ServiceException(ExceptionMessages.FRIEND_REQUEST_ALREADY_PENDING.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void validateIfSenderAndReceiverIsEquals(Long senderId, Long receiverId){
        if(senderId.equals(receiverId)){
            throw new ServiceException(ExceptionMessages.CANNOT_SEND_REQUEST_TO_YOURSELF.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
