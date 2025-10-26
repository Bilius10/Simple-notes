package api.example.SimpleNotes.domain.friend_request;

import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestListResponse;
import api.example.SimpleNotes.domain.friend_request.dto.response.FriendRequestResponse;
import api.example.SimpleNotes.domain.notification.NotificationService;
import api.example.SimpleNotes.domain.user.User;
import api.example.SimpleNotes.domain.user.UserService;
import api.example.SimpleNotes.domain.user.dto.response.UserResponse;
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
public class FriendRequestService {

    private final FriendRequestRepository repository;
    private final NotificationService notificationService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public PageDTO<FriendRequestListResponse> findAllFriends(Long userId, Pageable pageable){
        User userEntity = userService.findById(userId);

        Page<FriendRequest> friendsPage = repository.findAllByUserSenderAndStatus(
                userEntity, FriendRequestStatus.ACCEPTED, pageable
        );

        Page<FriendRequestListResponse> responsePage = friendsPage.map(friendRequest ->
                new FriendRequestListResponse(friendRequest.getId(), friendRequest.getUserReceiver())
        );

        return new PageDTO<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages()
        );
    }

    @Transactional(readOnly = true)
    public PageDTO<FriendRequestListResponse> findAllPendingFriendRequests(Long userId, Pageable pageable){
        User userEntity = userService.findById(userId);

        Page<FriendRequest> pendingFriendRequests
                = repository.findAllByUserReceiverAndStatus(userEntity, FriendRequestStatus.WAITING, pageable);

        Page<FriendRequestListResponse> responsePage = pendingFriendRequests.map(friendRequest ->
                new FriendRequestListResponse(friendRequest.getId(), friendRequest.getUserReceiver())
        );

        return new PageDTO<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages()
        );
    }

    @Transactional
    public FriendRequestResponse create(Long senderId, Long receiverId) {
        validateIfSenderAndReceiverIsEquals(senderId, receiverId);

        User userSender = userService.findById(senderId);
        User userReceiver = userService.findById(receiverId);

        validateIfExistsPendingFriendRequest(userSender, userReceiver);

        FriendRequest friendRequest = new FriendRequest(userSender, userReceiver);

        FriendRequest savedFriendRequest = repository.save(friendRequest);

        notificationService.save(userReceiver, "Voce possui um pedido de amizade pendente");

        return new FriendRequestResponse(savedFriendRequest);
    }

    @Transactional
    public void delete(Long id) {
        FriendRequest friendRequestEntity = findById(id);

        repository.delete(friendRequestEntity);
    }

    @Transactional(readOnly = true)
    public FriendRequest findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ServiceException(ExceptionMessages.FRIEND_REQUEST_NOT_FOUND.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }

    @Transactional
    public FriendRequestResponse addFriend(Long id, FriendRequestStatus friendRequestStatus) {
        FriendRequest friendRequestEntity = findById(id);
        validateIfFriendRequestStatusAlreadyAnswered(friendRequestEntity.getStatus());

        friendRequestEntity.setStatus(friendRequestStatus);
        FriendRequest savedFriendRequest = repository.save(friendRequestEntity);

        notificationService.save(friendRequestEntity.getUserSender(),
                "O usuario " +friendRequestEntity.getUserReceiver().getUsername()+" respondeu sua solicitação");

        return new FriendRequestResponse(savedFriendRequest);
    }

    private void validateIfFriendRequestStatusAlreadyAnswered(FriendRequestStatus friendRequestStatus){
        if(!friendRequestStatus.equals(FriendRequestStatus.WAITING)) {
            throw new ServiceException(ExceptionMessages.FRIEND_REQUEST_ALREADY_ANSWERED.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
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
