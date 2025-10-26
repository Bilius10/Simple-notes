package api.example.SimpleNotes.domain.friend_request.dto.request;

import api.example.SimpleNotes.domain.friend_request.FriendRequestStatus;

public record AddFriendRequest(
        FriendRequestStatus status
) {
}
