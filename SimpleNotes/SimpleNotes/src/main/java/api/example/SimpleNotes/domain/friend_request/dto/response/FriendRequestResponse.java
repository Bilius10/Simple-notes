package api.example.SimpleNotes.domain.friend_request.dto.response;

import api.example.SimpleNotes.domain.friend_request.FriendRequest;

import java.time.LocalDateTime;

public record FriendRequestResponse(
        Long id,
        Long senderId,
        Long receiverId,
        String status,
        LocalDateTime createdAt
) {
    public FriendRequestResponse(FriendRequest friendRequest) {
        this(friendRequest.getId(), friendRequest.getUserSender().getId(), friendRequest.getUserReceiver().getId(), friendRequest.getStatus().name(), friendRequest.getCreatedAt());
    }
}
