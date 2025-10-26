package api.example.SimpleNotes.domain.friend_request.dto.response;

import api.example.SimpleNotes.domain.friend_request.FriendRequest;
import api.example.SimpleNotes.domain.user.User;

public record FriendRequestResponse(
        Long id,
        Long senderId,
        Long receiverId,
        String status
) {
    public FriendRequestResponse(FriendRequest friendRequest) {
        this(friendRequest.getId(), friendRequest.getUserSender().getId(), friendRequest.getUserReceiver().getId(), friendRequest.getStatus().name());
    }
}
