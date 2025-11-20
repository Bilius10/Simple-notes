package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestListResponse;
import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestResponse;
import api.example.SimpleNotes.domain.notification.NotificationService;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.infrastructure.dto.PageDTO;
import api.example.SimpleNotes.infrastructure.exception.ExceptionMessages;
import api.example.SimpleNotes.infrastructure.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository repository;
    private final NotificationService notificationService;
    private final UserService userService;
    
    public PageDTO<FriendRequestListResponse> findAllFriends(Long userId, Pageable pageable){
        User user = userService.findById(userId);

        Page<FriendRequest> friendsAccepted = repository.findAllByUserSenderAndStatus(user, FriendRequestStatus.ACCEPTED, pageable);

        Page<FriendRequestListResponse> record = friendsAccepted.map(FriendRequestListResponse::fromRequest);

        return new PageDTO<>(record);
    }
    
    public PageDTO<FriendRequestListResponse> findAllPendingFriendRequests(Long userId, Pageable pageable){
        User user = userService.findById(userId);

        Page<FriendRequest> friendsPending = repository.findAllByUserReceiverAndStatus(user, FriendRequestStatus.WAITING, pageable);

        Page<FriendRequestListResponse> record = friendsPending.map(FriendRequestListResponse::fromRequest);

        return new PageDTO<>(record);
    }

    @Transactional
    public FriendRequestResponse create(Long senderId, Long receiverId) {
        validateIfSenderAndReceiverIsEquals(senderId, receiverId);
        validateIfExistsPendingFriendRequest(senderId, receiverId);

        User senderUser = userService.findById(senderId);
        User receiverUser = userService.findById(receiverId);

        FriendRequest friendRequest = new FriendRequest(senderUser, receiverUser);
        friendRequest = repository.save(friendRequest);

        sendNotification(receiverUser, "Voce possui um pedido de amizade pendente");

        return new FriendRequestResponse(friendRequest);
    }

    @Transactional
    public void delete(Long id, Long currentUserId) {
        FriendRequest friendRequestEntity = findByIdAndUserSenderId(id, currentUserId);

        repository.delete(friendRequestEntity);
    }

    @Transactional
    public FriendRequestResponse respondToRequest(Long id, FriendRequestStatus friendRequestStatus, Long currentUserId) {
        FriendRequest friendRequest = findByIdAndUserReceiverId(id, currentUserId);
        validateIfFriendRequestStatusAlreadyAnswered(friendRequest.getStatus());

        friendRequest.setStatus(friendRequestStatus);
        friendRequest = repository.save(friendRequest);

        sendNotification(friendRequest.getUserSender(),
                "O usuario " +friendRequest.getUserReceiver().getUsername()+" respondeu sua solicitação");

        return new FriendRequestResponse(friendRequest);
    }
    
    private void sendNotification(User user, String message){
        notificationService.save(user, message);
    }

    private void validateIfFriendRequestStatusAlreadyAnswered(FriendRequestStatus friendRequestStatus){
        if(!friendRequestStatus.equals(FriendRequestStatus.WAITING)) {
            throw new ServiceException(ExceptionMessages.FRIEND_REQUEST_ALREADY_ANSWERED.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void validateIfExistsPendingFriendRequest(Long senderId, Long receiverId){
        if(repository.existsByUserSenderIdAndUserReceiverIdAndStatus(senderId, receiverId, FriendRequestStatus.WAITING)){
            throw new ServiceException(ExceptionMessages.FRIEND_REQUEST_ALREADY_PENDING.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void validateIfSenderAndReceiverIsEquals(Long senderId, Long receiverId){
        if(senderId.equals(receiverId)){
            throw new ServiceException(ExceptionMessages.CANNOT_SEND_REQUEST_TO_YOURSELF.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    private FriendRequest findByIdAndUserSenderId(Long id, Long currentUserId) {
        return repository.findByIdAndUserSenderId(id, currentUserId).orElseThrow(() ->
                new ServiceException(ExceptionMessages.FRIEND_REQUEST_NOT_FOUND.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }

    private FriendRequest findByIdAndUserReceiverId(Long id, Long currentUserId) {
        return repository.findByIdAndUserReceiverId(id, currentUserId).orElseThrow(() ->
                new ServiceException(ExceptionMessages.FRIEND_REQUEST_NOT_FOUND.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }
}
